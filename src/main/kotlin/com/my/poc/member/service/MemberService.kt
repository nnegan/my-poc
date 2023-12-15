package com.my.poc.member.service

import com.my.poc.framework.EncryptionManager
import com.my.poc.framework.ResourceNotFoundException
import com.my.poc.framework.TokenProvider
import com.my.poc.member.*
import com.my.poc.member.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.regex.Pattern


@Service
class MemberService(
        private val memberRepository: MemberRepository,
        private val encryptionManager: EncryptionManager,
        private val tokenProvider: TokenProvider,
        private val commonCodeReader: CommonCodeReader,

        ) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 입력된 값에 대한 체크 로직
     */
    fun validCheck(memberSignDto: MemberSignDto, passwordChkFlag: Boolean): CommonReqDto{
        //validation 로직
        if (!Pattern.matches("(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])", memberSignDto.birthDay) ) {
            log.error("생년월일 형식이 맞지 않음 {}", memberSignDto.birthDay)
            return CommonReqDto( "fail","생년월일 형식이 맞지 않습니다.")
        }
        if (!Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+\$", memberSignDto.email) ) {
            log.error("이메일 형식이 맞지 않음 {}", memberSignDto.email)
            return CommonReqDto("fail","이메일 형식이 맞지 않습니다.")
        }
        // 최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자
        if ( passwordChkFlag  && !Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", memberSignDto.password) ) {
            log.error("비밀번호 형식이 맞지 않음 {}", memberSignDto.password)
            return CommonReqDto( "fail","비밀번호 형식이 맞지 않습니다.")
        }
        var validBoolean = true
        commonCodeReader.getCommonCodeList("age").forEach { c -> if ( c.code.equals(memberSignDto.ageCode)) validBoolean = false }
        if ( validBoolean ) {
            log.info("연령코드가 형식이 맞지 않음 {}", memberSignDto.ageCode)
            return CommonReqDto("fail", "연령코드가 형식이 맞지 않습니다.")
        }

        validBoolean = true
        commonCodeReader.getCommonCodeList("gender").forEach { c -> if (  c.code.equals(memberSignDto.gender)) validBoolean = false }
        if ( validBoolean ) {
            log.info("성별코드가 형식이 맞지 않음 {}", memberSignDto.gender)
            return CommonReqDto("fail","성별코드가 형식이 맞지 않습니다.")
        }

        return CommonReqDto( "success","성공")
    }

    /**
     *  회원 가입을 진행한다
     */
    fun createMember(memberSignDto: MemberSignDto) : SignUpReqDto {

        if ( memberRepository.findMemberLoginCnt(memberSignDto.memberId) > 0 ) {
            return SignUpReqDto(0, memberSignDto.memberId, "fail", "이미 사용되고 있는 아이디 입니다.", "")
        }
        val resultReq = this.validCheck(memberSignDto, true)

        if ( resultReq.resultCode == "fail" )
            return SignUpReqDto(0, memberSignDto.memberId, resultReq.resultCode, resultReq.resultMessage, "")

        val member = memberRepository.save(
                Member(
                        memberNo = 0,
                        name = memberSignDto.name,
                        birthDay = memberSignDto.birthDay,
                        gender = memberSignDto.gender,
                        memberId = memberSignDto.memberId,
                        password = encryptionManager.encrypt(memberSignDto.password),
                        email = memberSignDto.email,
                        childInfo = memberSignDto.childInfo,
                        requestInfo = memberSignDto.requestInfo,
                        ageCode = memberSignDto.ageCode,
                )
        )

        log.debug("memberNo {}", member.memberNo)

        val token = tokenProvider.createToken("${member.memberNo}")

        return SignUpReqDto(member.memberNo, member.memberId, "success", "성공", token)
    }

    /**
     * 회원 정보를 수정한다 (아이디는 변경 불가)
     */
    fun updateMember(memberSignDto: MemberSignDto, memberNo: Long) : CommonReqDto {

        val member = memberRepository.findById(memberNo).orElseThrow { ResourceNotFoundException("[$memberSignDto.memberNo] not found") }
                ?: return CommonReqDto("fail", "존재하지 않는 회원번호입니다.")

        // 비밀번호 null, "" 이면 체크하지 않음
        val resultReq = this.validCheck(memberSignDto, memberSignDto.password.isNullOrEmpty())

        if ( resultReq.resultCode == "fail" )
            return resultReq


        var newPassword = member.password

        // 패스워드가 입력되었고 기존 패스워드와 틀릴 경우에만 업데이트
        if ( !memberSignDto.password.isNullOrEmpty() && member.password != encryptionManager.encrypt(memberSignDto.password)){
            newPassword = encryptionManager.encrypt(memberSignDto.password)
        }

        memberRepository.save(
                Member(
                        memberNo = memberNo,
                        name = memberSignDto.name,
                        birthDay = memberSignDto.birthDay,
                        gender = memberSignDto.gender,
                        memberId = member.memberId,
                        password = newPassword,
                        email = memberSignDto.email,
                        childInfo = memberSignDto.childInfo,
                        requestInfo = memberSignDto.requestInfo,
                        ageCode = memberSignDto.ageCode,
                )
        )

        log.debug("memberNo {}", memberNo)

        return CommonReqDto("success", "정상적으로 수정되었습니다.")
    }

    /**
     * 회원정보를 조회한다
     */

    fun findMember(memberNo: Long) : MemberInfoDto {
        val member =  memberRepository.findById(memberNo).orElseThrow { ResourceNotFoundException("[$memberNo] not found") }
        return MemberInfoDto(
                memberId = member.memberId,
                name =  member.name,
                birthDay = member.birthDay,
                gender = member.gender,
                email = member.email,
                ageCode = member.ageCode,
                childInfo = member.childInfo,
                requestInfo = member.requestInfo
        )
    }

    /**
     * 로그인 기능을 수행한다
     */
    fun signInMember(signInReqDto: SignInReqDto) : SignInResDto {
        val member = memberRepository.findMemberLogin(signInReqDto.memberId)
            //    ?.takeIf { it.password == memberSignDto.password } ?: throw IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.")

        log.debug("encryptionManager.encrypt(memberSignDto.password) => {}", encryptionManager.encrypt(signInReqDto.password))
        log.debug("member.password => {}", member.password)

        if (encryptionManager.encrypt(signInReqDto.password) == member.password){
            // login

            val token = tokenProvider.createToken("${member.memberNo}")
            return SignInResDto(member.memberNo, member.memberId, token)
        }else{
            throw IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.")
        }

    }
}
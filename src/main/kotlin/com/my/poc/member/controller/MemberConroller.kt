package com.my.poc.member.controller

import com.my.poc.member.*
import com.my.poc.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberConroller (
        private val memberService: MemberService,
        private val commonCodeReader: CommonCodeReader,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Operation( summary = "회원가입", description = "회원가입을 진행하고 성공시 token을 발급받음")
    @PostMapping("/sign-up")
    fun signUpMember(@RequestBody @Valid memberSignDto: MemberSignDto) : SignUpReqDto {
        return memberService.createMember(memberSignDto);
    }

    @Operation( summary = "회원정보수정", description = "회원정보수정을 진행한다 (아이디는 변경 불가)")
    @PutMapping("/info")
    fun updateMember(authentication: Authentication, @RequestBody @Valid memberSignDto: MemberSignDto) : CommonReqDto {
        return memberService.updateMember(memberSignDto, authentication.name.toLong());
    }

    @Operation( summary = "내정보보기", description = "내정보보기를 진행한다")
    @GetMapping("/info")
    fun findMember(authentication: Authentication) : MemberInfoDto {
        return memberService.findMember(authentication.name.toLong())
    }

    @Operation( summary = "로그인", description = "로그인 진행하고 성공시 token을 발급받음")
    @PostMapping("/sign-in")
    fun signInMember(@RequestBody @Valid signInReqDto: SignInReqDto) : SignInResDto {
        return memberService.signInMember(signInReqDto);
    }

    @Operation( summary = "공통코드 조회", description = "화면에서 사용할 공통코드를 조회한다")
    @GetMapping("/code/{codeGroup}")
    fun codeList(@PathVariable codeGroup: String) : List<CommonCode> {
        return commonCodeReader.getCommonCodeList(codeGroup)
    }
}
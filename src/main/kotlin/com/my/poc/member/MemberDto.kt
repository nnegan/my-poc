package com.my.poc.member

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원가입")
data class MemberSignDto (

        @Schema(description = "회원가입 ID")
        val memberId: String,

        @Schema(description = "이름")
        val name: String,
        @Schema(description = "생년월일 yyyymmdd")
        val birthDay: String,
        @Schema(description = "고객성별 F:여성, M:남성 -> 공통코드그룹 gender")
        val gender: String,
        @Schema(description = "비밀번호 : 최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자")
        val password: String,
        @Schema(description = "이메일 주소 (형식체크)")
        val email:String,

        @Schema(description = "연령 코드 : A01, A23, A45 --> 공통코드그룹 age")
        val ageCode: String,

        @Schema(description = "자녀정보")
        val childInfo: String,
        @Schema(description = "요청사항")
        val requestInfo: String,
)

@Schema(description = "내정보")
data class MemberInfoDto (

        @Schema(description = "회원가입 ID")
        val memberId: String,

        @Schema(description = "이름")
        val name: String,
        @Schema(description = "생년월일 yyyymmdd")
        val birthDay: String,
        @Schema(description = "고객성별 F:여성, M:남성 -> 공통코드그룹 gender")
        val gender: String,

        @Schema(description = "이메일 주소 (형식체크)")
        val email:String,

        @Schema(description = "연령 코드 : A01, A23, A45 --> 공통코드그룹 age")
        val ageCode: String,

        @Schema(description = "자녀정보")
        val childInfo: String,

        @Schema(description = "요청사항")
        val requestInfo: String,
)

@Schema(description = "로그인 정보")
data class SignInReqDto(
        @Schema(description = "아이디")
        val memberId: String,
        @Schema(description = "비밀번호")
        val password: String
)

@Schema(description = "공통코드")
data class CommonCode(
        @Schema(description = "공통코드 그룹")
        val groupCd: String,
        @Schema(description = "코드")
        val code: String,
        @Schema(description = "코드 값(명칭)")
        val codeValue : String
){
        constructor() : this("", "", "")
}

@Schema(description = "로그인 결과")
data class SignInResDto(
        @Schema(description = "회원번호")
        val memberNo: Long,
        @Schema(description = "아이디")
        val memberId: String,
        @Schema(description = "생성된 토큰값")
        val token: String
)
@Schema(description = "가입 결과")
data class SignUpReqDto(
        @Schema(description = "회원번호")
        val memberNo: Long,
        @Schema(description = "아이디")
        val memberId: String,
        @Schema(description = "결과 코드 성공: success, 실패: fail")
        val resultCode: String,
        @Schema(description = "결과 메시지")
        val resultMessage: String,
        @Schema(description = "생성된 토큰값")
        val token: String
)
@Schema(description = "공통처리 결과")
data class CommonReqDto(
        @Schema(description = "결과 코드 성공: success, 실패: fail")
        val resultCode: String,
        @Schema(description = "결과 메시지")
        val resultMessage: String,
)

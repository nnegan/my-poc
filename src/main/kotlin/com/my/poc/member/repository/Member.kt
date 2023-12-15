package com.my.poc.member.repository

import com.my.poc.framework.AbstractEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Entity
@Table(name = "member")
data class Member(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "member_no", updatable = false)
        val memberNo: Long,

        @Schema(description = "성명")
        @Column(length = 100)
        val name: String,

        @Schema(description = "생년월일 yyyymmdd")
        @Column(length = 8)
        val birthDay: String,

        @Schema(description = "성별")
        @Column(length = 20)
        val gender: String,

        @Schema(description = "아이디")
        val memberId: String,

        @Schema(description = "비밀번호")
        val password: String,

        @Schema(description = "이메일")
        val email:String,

        @Schema(description = "아이 정보")
        val childInfo: String,

        @Schema(description = "신청내용")
        val requestInfo: String,

        @Schema(description = "연령대 코드")
        @Column(length = 20)
        val ageCode: String,

        ) : AbstractEntity(){

}

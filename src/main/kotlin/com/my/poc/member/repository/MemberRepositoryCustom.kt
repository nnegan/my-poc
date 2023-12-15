package com.my.poc.member.repository

import org.springframework.stereotype.Repository

@Repository
interface MemberRepositoryCustom {
    fun findMemberLogin(memberId: String): Member

    fun findMemberLoginCnt(memberId: String): Int
}
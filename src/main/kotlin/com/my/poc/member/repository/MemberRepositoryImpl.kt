package com.my.poc.member.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory

class MemberRepositoryImpl(

        private val jpaQueryFactory: JPAQueryFactory,
        private val qMember: QMember = QMember.member


) : MemberRepositoryCustom{

    override fun findMemberLogin(memberId: String): Member {
        return jpaQueryFactory.selectFrom(qMember).where(BooleanBuilder().and(qMember.memberId.eq(memberId))).fetchFirst()
    }

    override fun findMemberLoginCnt(memberId: String): Int {
        return jpaQueryFactory.selectFrom(qMember).where(BooleanBuilder().and(qMember.memberId.eq(memberId))).fetch().size
    }

}
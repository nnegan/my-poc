package com.my.poc.framework

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractEntity(

        @CreatedBy
        @Column(updatable = false)
        var createdBy: String? = "unknown",

        @CreatedDate
        var createdTime: LocalDateTime = LocalDateTime.now(),

        @LastModifiedBy
        var modifiedBy: String? = "unknown",

        @LastModifiedDate
        var modifiedTime: LocalDateTime = LocalDateTime.now()
) {
}
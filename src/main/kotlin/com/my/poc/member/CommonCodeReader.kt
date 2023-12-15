package com.my.poc.member

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class CommonCodeReader {

    fun getCommonCodeListAll():  List<CommonCode>{
        val classPathResource = ClassPathResource("commonCode.json")
        val objectMapper = ObjectMapper()
        return  objectMapper.readValue(classPathResource.getInputStream(), objectMapper.typeFactory.constructCollectionType(MutableList::class.java, CommonCode::class.java))
    }

    fun getCommonCodeList(groupCode: String) : List<CommonCode>{
        return this.getCommonCodeListAll().filter { code -> code.groupCd.equals(groupCode) }
    }
}
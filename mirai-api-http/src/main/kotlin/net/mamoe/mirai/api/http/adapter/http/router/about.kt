/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package net.mamoe.mirai.api.http.adapter.http.router

import io.ktor.application.*
import io.ktor.routing.*
import net.mamoe.mirai.api.http.HttpApiPluginBase
import net.mamoe.mirai.api.http.adapter.internal.dto.StringMapRestfulResult

private val mahVersion by lazy {
    val desc = HttpApiPluginBase.description
    desc.javaClass.fields.first { it.name == "version" }.get(desc).toString()
}

/**
 * 配置路由
 */
internal fun Application.aboutRouter() = routing {

    /**
     * 获取API-HTTP插件信息
     */
    get("/about") {
        val data = mapOf("version" to mahVersion)
        call.respondDTO(StringMapRestfulResult(data = data))
    }
}

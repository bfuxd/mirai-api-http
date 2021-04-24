package net.mamoe.mirai.api.http.adapter.internal.serializer

import net.mamoe.mirai.api.http.context.serializer.InternalSerializerHolder

/**
 * 序列化 object
 */
internal inline fun <reified T : Any> T.toJson(): String =
    InternalSerializerHolder.serializer.encode(this)


/**
 * 序列化列表
 */
internal inline fun <reified T : Any> List<T>.toJson(): String =
    InternalSerializerHolder.serializer.encode(this)


/**
 * 解析 object，可指定序列化器
 *
 * 异常时返回 null
 */
internal inline fun <reified T : Any> String.jsonParseOrNull(): T? = runCatching<T> {
    InternalSerializerHolder.serializer.decode(this)
}.getOrNull()

package net.mamoe.mirai.api.http.adapter.internal.serializer

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import net.mamoe.mirai.api.http.adapter.common.StateCode
import net.mamoe.mirai.api.http.adapter.internal.dto.*
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
internal class JsonSerializer : InternalSerializer {

    /**
     * Json解析规则，需要注册支持的多态的类
     */
    private val json by lazy {
        Json {
            encodeDefaults = true
            isLenient = true
            ignoreUnknownKeys = true

            @Suppress("UNCHECKED_CAST")
            serializersModule = SerializersModule {
                polymorphicSealedClass(EventDTO::class, MessagePacketDTO::class)
                polymorphicSealedClass(EventDTO::class, BotEventDTO::class)
//                polymorphicSealedClass(StateCode::class, StateCode::class)
            }
        }
    }

    /**
     * 从 sealed class 里注册到多态序列化
     */
    @InternalSerializationApi
    @Suppress("UNCHECKED_CAST")
    private fun <B : Any, S : B> SerializersModuleBuilder.polymorphicSealedClass(
        baseClass: KClass<B>,
        sealedClass: KClass<S>
    ) {
        sealedClass.sealedSubclasses.forEach {
            val c = it as KClass<S>
            polymorphic(baseClass, c, c.serializer())
        }
    }

    override fun <T : Any> encode(dto: T, clazz: KClass<T>): String = when (dto) {
        is StateCode -> json.encodeToString(StateCode.serializer(), dto)
        else -> json.encodeToString(clazz.serializer(), dto)
    }

    override fun <T : Any> encode(list: List<T>, clazz: KClass<T>): String {
        return json.encodeToString(ListSerializer(clazz.serializer()), list)
    }

    override fun <T : Any> decode(content: String, clazz: KClass<T>): T {
        return json.decodeFromString(clazz.serializer(), content)
    }

}

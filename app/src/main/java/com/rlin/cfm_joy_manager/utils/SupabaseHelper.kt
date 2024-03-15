package com.rlin.cfm_joy_manager.utils

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import kotlin.time.Duration

object SupabaseHelper {
    val client = createSupabaseClient(
        // 项目URL
        supabaseUrl = "https://gymoylnnmwrmytztfwjl.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd5bW95bG5ubXdybXl0enRmd2psIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTAzMDk0NjksImV4cCI6MjAyNTg4NTQ2OX0.HbW3QdELrzZyOBTDMXtRpu5cSJS1nf1zivSMhTI4OY8",
    ) {
        // 用于配置客户端，比如加载Database的依赖之类的
        // 配置序列化器
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
        })
        // 安装Database插件
        install(Postgrest)
        requestTimeout = Duration.parse("15s")
    }
}
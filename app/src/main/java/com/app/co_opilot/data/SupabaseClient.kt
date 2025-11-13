package com.app.co_opilot.data

import com.app.co_opilot.BuildConfig
import com.app.co_opilot.data.provider.SupabaseProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient: SupabaseProvider {
    override val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Realtime)
        install(Auth)
    }
}

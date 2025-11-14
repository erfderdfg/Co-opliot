package com.app.co_opilot.data.provider

import io.github.jan.supabase.SupabaseClient

interface SupabaseProvider {
    val client: SupabaseClient

}

package com.app.co_opilot.data.provider

import io.github.jan.supabase.SupabaseClient

class MockSupabaseProvider: SupabaseProvider {
    override val client: SupabaseClient
        get() = throw UnsupportedOperationException("Not used in fake repos")
}
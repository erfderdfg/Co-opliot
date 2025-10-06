package com.app.co_opilot.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://afoagwkcdvynqukpjtbk.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFmb2Fnd2tjZHZ5bnF1a3BqdGJrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk2OTM3MDQsImV4cCI6MjA3NTI2OTcwNH0.q0ID4D7ukLGsPV8qUftKjBcH7SWi1xW83Tez_Ph6ALI"
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Realtime)
    }
}

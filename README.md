# Co-opilot

A social + study-matching platform designed for University of Waterloo students. Built with Kotlin, Jetpack Compose, and Supabase.

# 🚀 Overview

Co-opilot helps Waterloo students connect with classmates, find study partners, and join small groups based on shared interests, schedules, and personality preferences.
The app provides fast onboarding, profile matching using Postgres queries, chat features, and a clean modern UI powered by Compose.

Originally created as a BET350 project, the focus is delivering a polished end-to-end product that feels production-ready.

# ✨ Features
## 🔐 Authentication & Profiles

Email login/signup via Supabase Auth

Editable user profile (name, program, term, personality tags, interests)

Profile images stored via Supabase Storage

## 🤝 Matching Engine

Postgres-powered matching using:

Shared interests

Program/year compatibility

Personality alignment

Availability comparison

Ranked results returned through Supabase PostgREST filters

## 💬 In-App Chat

Real-time chat using Supabase realtime channels

One-to-one and group message flow

Typing indicators + message timestamps

## 📱 UI/UX

Fully Jetpack Compose UI

Responsive layouts, dark mode support

Smooth navigation with Navigation Compose

Modern, minimal visual identity

# 🛠️ Tech Stack
### Frontend (Android)

Kotlin

Jetpack Compose

Navigation Compose

ViewModel + Coroutines

Ktor Client

### Backend

Supabase

PostgreSQL Database

PostgREST (auto-generated APIs)

Realtime WebSockets

Auth & Storage

## 📂 Project Structure
```
app/
└── src/
    ├── androidTest/
    ├── main/
    │   ├── java/
    │   │   └── com.app.co_opilot/
    │   │       ├── data/
    │   │       │   ├── provider/
    │   │       │   ├── repository/
    │   │       │   ├── AuthState.kt
    │   │       │   └── SupabaseClient.kt
    │   │       ├── di/
    │   │       ├── domain/
    │   │       │   ├── enums/
    │   │       │   └── profile/
    │   │       ├── service/
    │   │       ├── ui/
    │   │       │   ├── components/
    │   │       │   ├── screens/
    │   │       │   │   ├── auth/
    │   │       │   │   ├── chats/
    │   │       │   │   ├── discovery/
    │   │       │   │   ├── explore/
    │   │       │   │   ├── leaderboard/
    │   │       │   │   ├── profile/
    │   │       │   │   └── setting/
    │   │       │   └── theme/
    │   │       └── util/
    │   ├── res/
    │   └── AndroidManifest.xml
    └── test/
```

## ⚙️ Setup & Installation
### 1. Clone the repo
git clone https://github.com/your-username/co-opilot.git
cd co-opilot

### 2. Add Supabase credentials

Create a local.properties file with:

SUPABASE_URL=your_url
SUPABASE_KEY=your_anon_key

### 3. Build the project

Open Android Studio → Sync Gradle → Run on device/emulator.

# 🧪 Testing

Unit tests for onboarding, profile creation, chat repositories

Integration tests for Supabase providers

Fake/mock repositories for offline testing

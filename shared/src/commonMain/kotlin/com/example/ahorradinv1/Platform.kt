package com.example.ahorradinv1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
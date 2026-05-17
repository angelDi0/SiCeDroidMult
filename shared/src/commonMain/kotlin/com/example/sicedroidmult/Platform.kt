package com.example.sicedroidmult

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
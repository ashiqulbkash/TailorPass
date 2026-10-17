package com.tech_hunt.tailorpass

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
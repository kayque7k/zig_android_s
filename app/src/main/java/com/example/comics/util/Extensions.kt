package com.example.comics.util

inline fun <T> T?.orNull(result: () -> T): T = this ?: result.invoke()
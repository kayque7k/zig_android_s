package com.example.comics.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface IEvent<T : Any> {
    fun event(event: T)

    var event: (T) -> Unit

    fun CoroutineScope.sendEvent(event: T): Job

    fun CoroutineScope.containsEvent(type: Class<*>): Boolean
}
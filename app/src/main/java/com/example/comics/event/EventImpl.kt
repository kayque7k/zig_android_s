package com.example.comics.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class EventImpl<T : Any> : IEvent<T> {

    private var processEvent: MutableList<Pair<T, Boolean>> = mutableListOf()

    override lateinit var event: (T) -> Unit

    override fun CoroutineScope.sendEvent(event: T): Job {
        processEvent.add(Pair(event, true))
        return launch(Dispatchers.IO) {
            val indexEvent = processEvent.indexOfLast { it.first == event }
            val indexEventOld = indexEvent - 1

            while (processEvent.getOrNull(indexEventOld)?.second == true);

            withContext(Dispatchers.Main) {
                event(processEvent.last().first)
                processEvent[indexEvent] = Pair(event, false)
            }
        }
    }

    override fun CoroutineScope.containsEvent(type: Class<*>): Boolean {
        return processEvent.any { it.first == type }
    }

    override fun event(event: T) {
        this.event.invoke(event)
    }
}
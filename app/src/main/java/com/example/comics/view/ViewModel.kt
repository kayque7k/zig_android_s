package com.example.comics.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comics.repository.IRepository
import com.example.comics.repository.toListItemVO
import com.example.comics.event.EventImpl
import com.example.comics.event.IEvent
import com.example.comics.util.Result
import com.example.comics.util.safeRunDispatcher
import com.example.comics.view.ViewModel.Event
import com.example.comics.view.ViewModel.Event.SetupList
import com.example.comics.view.ViewModel.Event.Error
import com.example.comics.view.ViewModel.Event.LoadingIndicator
import com.example.comics.view.ViewModel.Event.CloseIndicator
import com.example.comics.view.ViewModel.Event.Empty
import com.example.comics.view.ViewModel.Event.UpdateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(
    private val repository: IRepository
) : ViewModel(), IEvent<Event> by EventImpl() {

    private var update: Int = 0

    fun getComics() = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.sendEvent(Empty)
        viewModelScope.sendEvent(LoadingIndicator)
        viewModelScope.sendEvent(
            when (val result = safeRunDispatcher {
                repository.getComics()
            }) {
                is Result.Success -> {
                    viewModelScope.run {
                        if (!containsEvent(SetupList::class.java)) {
                            SetupList(result.data.toListItemVO())
                        } else {
                            update += 1
                            UpdateList(result.data.toListItemVO(), update)
                        }
                    }
                }

                is Result.Failure -> Error
            })
        viewModelScope.sendEvent(CloseIndicator)
    }

    sealed interface Event {

        data object LoadingIndicator : Event

        data object CloseIndicator : Event

        data class SetupList(val item: List<ItemVO>) : Event

        data class UpdateList(val item: List<ItemVO>,val countUpdate: Int) : Event

        data object Error : Event

        data object Empty : Event

    }
}
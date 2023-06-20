package com.devs.imgur.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.imgur.data.repository.ImageRepository
import com.devs.imgur.data.repository.modal.Gallery
import com.devs.imgur.data.repository.resource.NetworkError
import com.devs.imgur.data.repository.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    var isOffline = false
    private val imageState = MutableLiveData<Resource<Gallery>>()

    fun imageState() = imageState

    fun searchImage(query : String) {
        if (isOffline) {
            // No network connection
            imageState.value =
                Resource.error(null, networkError = NetworkError.NO_CONNECTIVITY)
            return
        }
        imageState.value = Resource.loading(null)
        viewModelScope.launch {
            val resource = imageRepository.searchImage(query)
            imageState.postValue(resource)
        }
    }
}
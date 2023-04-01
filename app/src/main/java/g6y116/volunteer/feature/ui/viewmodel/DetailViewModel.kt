package g6y116.volunteer.feature.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.feature.data.model.*
import g6y116.volunteer.feature.data.repository.DetailRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel() {

    private val _detailUiState = MutableLiveData<DetailUiState>()
    val detailUiState: LiveData<DetailUiState>
        get() = _detailUiState

    private val _coordinate = MutableLiveData<Coordinate>()
    val coordinate: LiveData<Coordinate>
        get() = _coordinate

    fun setDetailUiState(detailUiState: DetailUiState) {
        viewModelScope.launch {
            _detailUiState.value = detailUiState
            if (detailUiState.address.isNotEmpty()) {
                setCoordinate(detailUiState.address)
            }
        }
    }

    private fun setCoordinate(address: String) {
        viewModelScope.launch {
            repository.loadCoordinate(address)?.let {
                _coordinate.value = it
            }
        }
    }

    fun clickBookMark() {
        detailUiState.value?.let {
            viewModelScope.launch {
                if (it.isBookmark) {
                    repository.deleteBookmark(it.pID)
                    it.isBookmark = false
                } else {
                    repository.insertBookmark(Info(it.pID, it.title, it.host, it.sDate, it.eDate, it.state, it.url))
                    it.isBookmark = true
                }
            }
        }
    }
}
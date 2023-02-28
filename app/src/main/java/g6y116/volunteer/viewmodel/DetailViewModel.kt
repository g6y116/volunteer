package g6y116.volunteer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.data.Visit
import g6y116.volunteer.data.Volunteer
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    val volunteerLiveData = MutableLiveData<Volunteer>()
    val isBookmarkLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Boolean>()

    var pID = ""
    var url = ""
    var from = ""

    fun getVolunteer(pID: String) {
        if (pID.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val v = repository.getVolunteer(pID)
                    if (v != null) {
                        volunteerLiveData.postValue(v!!)
                        isBookmarkLiveData.postValue(repository.getBookmarkList().any { it.pID == v.pID })
                    } else {
                        repository.removeBookmark(pID)
                        errorLiveData.postValue(true)
                    }

                } catch (e: Exception) {
                    repository.removeBookmark(pID)
                    errorLiveData.postValue(true)
                }
            }
        }
    }

    fun clickBookMark() {
        viewModelScope.launch {
            if (volunteerLiveData.value != null && isBookmarkLiveData.value != null) {
                if (isBookmarkLiveData.value!!) {
                    repository.removeBookmark(volunteerLiveData.value!!.pID)
                    isBookmarkLiveData.postValue(false)
                } else {
                    repository.addBookmark(volunteerLiveData.value!!.toInfo(url))
                    isBookmarkLiveData.postValue(true)
                }
            }
        }
    }

    fun addRead(pID: String) {
        viewModelScope.launch {
            repository.addVisit(Visit(pID = pID))
        }
    }
}
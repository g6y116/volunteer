package g6y116.volunteer.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.repository.Volunteer
import g6y116.volunteer.repository.VolunteerInfo
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    val volunteer = MutableLiveData<Volunteer>()
    private val bookMarkList = repository.getBookMarkInfoList()
    val pID = MutableLiveData<String>()
    val url = MutableLiveData<String>()
    val from = MutableLiveData<String>()

    fun getVolunteerFromHome(pID: String) {
        viewModelScope.launch {
            volunteer.value = repository.getHomeDetail(pID)
        }
    }

    fun getVolunteerFromBookMark(pID: String) {
        viewModelScope.launch {
            volunteer.value = repository.getBookMark(pID).value
        }
    }

    fun clickBookMark() {
        viewModelScope.launch {
            volunteer.value?.let {
                if (isBookMark()) {
                    Log.d("성준", "북마크 맞음 -> 삭제")
                    repository.removeBookMark(VolunteerInfo(it.pID, it.title, it.host, it.sDate, it.eDate, it.state, url.value ?: ""), it)
                } else {
                    Log.d("성준", "북마크 아님 -> 추가")
                    repository.addBookMark(VolunteerInfo(it.pID, it.title, it.host, it.sDate, it.eDate, it.state, url.value ?: ""), it)
                }
            }
        }

    }

    private fun isBookMark(): Boolean {
        return bookMarkList.value?.any { it.pID == pID.value } == true
    }
}
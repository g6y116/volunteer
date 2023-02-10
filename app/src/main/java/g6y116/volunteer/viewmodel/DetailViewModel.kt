package g6y116.volunteer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.data.Volunteer
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    val volunteer = MutableLiveData<Volunteer>()
    val isBookMark = MutableLiveData<Boolean>(false)
    private val bookMarkList = repository.getBookMarkList()

    var pID = ""
    var url = ""
    var from = ""

    fun getVolunteer(pID: String) {
        viewModelScope.launch {
            volunteer.value = repository.getDetail(pID)
            volunteer.value?.let {
                isBookMark.value = it.isBookMark(bookMarkList.value)
            }
        }
    }

    fun clickBookMark() {
        viewModelScope.launch {
            volunteer.value?.run {
                if (isBookMark.value == null || isBookMark.value == false) {
                    repository.addBookMark(VolunteerInfo(pID, title, host, sDate, eDate, state, url))
                    isBookMark.value = true
                } else if (isBookMark.value == true) {
                    repository.removeBookMark(pID)
                    isBookMark.value = false
                }
            }
        }
    }
}
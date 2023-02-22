package g6y116.volunteer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.data.Volunteer
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    val volunteer = MutableLiveData<Volunteer>()
    val isBookMark = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Boolean>()

    var pID = ""
    var url = ""
    var from = ""

    fun getVolunteer(pID: String) {
        if (pID.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val v = repository.getDetail(pID)
                    if (v != null) {
                        volunteer.postValue(v!!)
                        isBookMark.postValue(repository.getBookMarkList().any { it.pID == v.pID })
                    } else {
                        repository.removeBookMark(pID)
                        errorLiveData.postValue(true)
                    }

                } catch (e: Exception) {
                    repository.removeBookMark(pID)
                    errorLiveData.postValue(true)
                }
            }
        }
    }

    fun clickBookMark() {
        Logger.addLogAdapter(AndroidLogAdapter())
        viewModelScope.launch {
            if (volunteer.value != null && isBookMark.value != null) {
                if (isBookMark.value!!) {
                    repository.removeBookMark(volunteer.value!!.pID)
                    isBookMark.postValue(false)
                } else {
                    repository.addBookMark(volunteer.value!!.toVolunteerInfo(url))
                    isBookMark.postValue(true)
                }
            }
        }
    }
}
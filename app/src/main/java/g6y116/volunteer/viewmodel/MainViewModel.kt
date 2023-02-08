package g6y116.volunteer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.Const
import g6y116.volunteer.repository.Volunteer
import g6y116.volunteer.repository.VolunteerRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    val homeList = repository.getHomeList(
        pageNum = 1,
        siDoCode = "",
        gooGunCode = "",
        sDate = "",
        eDate = "",
        keyWord = "",
        isAdultPossible = Const.ALL,
        isYoungPossible = Const.ALL,
    ).cachedIn(viewModelScope)

    val bookMarkList = repository.getBookMarkInfoList()

    suspend fun getDetailFromHome(pID: String): Volunteer {
        return repository.getHomeDetail(pID)
    }

    fun getDetailFromBookMark(pID: String): LiveData<Volunteer> {
        return repository.getBookMark(pID)
    }
}
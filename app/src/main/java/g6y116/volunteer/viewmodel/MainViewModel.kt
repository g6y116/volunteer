package g6y116.volunteer.viewmodel

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
        keyWord = "",
        sDate = "",
        eDate = "",
        siDoCode = "",
        gooGunCode = "",
        isAdultPossible = Const.ALL,
        isYoungPossible = Const.ALL,
    ).cachedIn(viewModelScope)

    suspend fun getVolunteerDetail(pID: String): Volunteer {
        return repository.getHomeDetail(pID)
    }
}
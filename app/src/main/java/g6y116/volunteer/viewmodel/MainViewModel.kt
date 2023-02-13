package g6y116.volunteer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    val homeList: Flow<PagingData<VolunteerInfo>> = repository.getHomeList()
    val bookMarkList: LiveData<List<VolunteerInfo>> = repository.getBookMarkLiveList()
}
package g6y116.volunteer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.data.RecentSearch
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    var homeList: MutableStateFlow<PagingData<VolunteerInfo>> = MutableStateFlow(PagingData.empty())
    val bookMarkList: LiveData<List<VolunteerInfo>> = repository.getBookMarkLiveList()
    val recentSearch: MutableLiveData<RecentSearch> = MutableLiveData()

    init {
        viewModelScope.launch {
            recentSearch.postValue(loadRecentSearch() ?: RecentSearch.reset())
            changeOption(loadRecentSearch())
        }
    }

    private fun changeOption(recentSearch: RecentSearch) {
        viewModelScope.launch {
            repository.getHomeList(recentSearch).cachedIn(viewModelScope).collect {
                homeList.value = it
            }
        }
    }

    fun resetRecentSearch() = viewModelScope.launch(Dispatchers.IO) {
        repository.resetRecentSearch()
        recentSearch.postValue(RecentSearch.reset())
        changeOption(RecentSearch.reset())
    }

    fun saveRecentSearch(save: RecentSearch) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveRecentSearch(save)
        recentSearch.postValue(loadRecentSearch() ?: RecentSearch.reset())
        changeOption(save)
    }

    private suspend fun loadRecentSearch() = withContext(Dispatchers.IO) {
        repository.loadRecentSearch().first()
    }
}
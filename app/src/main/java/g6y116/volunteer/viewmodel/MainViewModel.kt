package g6y116.volunteer.viewmodel

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.Const
import g6y116.volunteer.data.Read
import g6y116.volunteer.data.RecentSearch
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    var homeList: MutableStateFlow<PagingData<VolunteerInfo>> = MutableStateFlow(PagingData.empty())
    val bookMarkList: LiveData<List<VolunteerInfo>> = repository.getBookMarkLiveList()
    val readList: LiveData<List<Read>> = repository.getReadLiveList()
    val recentSearchLiveData: MutableLiveData<RecentSearch> = MutableLiveData()
    val modeLiveData: MutableLiveData<String> = MutableLiveData()
    val localeLiveData: MutableLiveData<String> = MutableLiveData()
    val stateLiveData: MutableLiveData<String> = MutableLiveData()
    val readLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        viewModelScope.launch {
            recentSearchLiveData.postValue(getRecentSearch())
            changeOption(getRecentSearch())
            getState()
            getRead()
        }
    }

    private fun changeOption(recentSearch: RecentSearch) {
        viewModelScope.launch {
            repository.getHomeList(recentSearch).cachedIn(viewModelScope).collect {
                homeList.value = it
            }
        }
    }

    fun removeRecentSearch() = viewModelScope.launch(Dispatchers.IO) {
        val recentSearch = RecentSearch.reset()
        repository.setRecentSearch(recentSearch)
        recentSearchLiveData.postValue(recentSearch)
        changeOption(recentSearch)
    }

    fun setRecentSearch(recentSearch: RecentSearch) = viewModelScope.launch(Dispatchers.IO) {
        repository.setRecentSearch(recentSearch)
        recentSearchLiveData.postValue(recentSearch)
        changeOption(recentSearch)
    }

    private suspend fun getRecentSearch() = withContext(Dispatchers.IO) {
        repository.getRecentSearch().first()
    }

    fun getMode() {
        viewModelScope.launch(Dispatchers.Main) {
            val mode = repository.getMode()
            modeLiveData.value = mode
            applyMode(mode)
        }
    }

    fun setMode(mode: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setMode(mode)
            modeLiveData.value = mode
            applyMode(mode)
        }
    }

    private fun applyMode(mode: String) {
        when(mode) {
            Const.MODE.LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Const.MODE.DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Const.MODE.SYSTEM_MODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }
    }

    fun getLocale() {
        viewModelScope.launch(Dispatchers.Main) {
            localeLiveData.value = repository.getLocale()
        }
    }

    fun setLocale(locale: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setLocale(locale)
            localeLiveData.value = locale
            applyLocale(locale)
        }
    }

    private fun applyLocale(locale: String) {
        val enLocale = LocaleListCompat.forLanguageTags("en-US")
        val koLocale = LocaleListCompat.forLanguageTags("ko-rKR")
        AppCompatDelegate.setApplicationLocales(
            when(locale) {
                Const.LOCALE.EN -> enLocale
                else -> koLocale
            }
        )
    }

    fun removeRead() {
        viewModelScope.launch {
            repository.removeRead()
        }
    }

    fun getRead() {
        viewModelScope.launch(Dispatchers.Main) {
            readLiveData.value = repository.getReadOption()
        }
    }

    fun setRead(mode: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setReadOption(mode)
            readLiveData.value = mode
        }
    }

    fun getState() {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = repository.getStateOption()
        }
    }

    fun setState(mode: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setStateOption(mode)
            stateLiveData.value = mode
        }
    }
}
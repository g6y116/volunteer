package g6y116.volunteer.feature.ui.viewmodel

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.base.consts.Const
import g6y116.volunteer.base.utils.log
import g6y116.volunteer.feature.data.model.*
import g6y116.volunteer.feature.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var infoList: MutableStateFlow<PagingData<Info>> = MutableStateFlow(PagingData.empty())
    private val bookMarkList: Flow<List<Info>> = repository.loadBookMarkInfo()
    private val visitList: Flow<List<Visit>> = repository.loadVisit()

    val searchOption: LiveData<SearchOption> = repository.loadSearchOption().asLiveData()
    val appOption: LiveData<AppOption> = repository.loadAppOption().asLiveData()

    val homeUiState: StateFlow<PagingData<MainUiState>> =
        combine(infoList, bookMarkList, visitList) { infoList, bookMarkInfoList, visitList ->
            infoList.map { info ->
                MainUiState(
                    info.pID,
                    info.title,
                    info.host,
                    info.sDate,
                    info.eDate,
                    info.state,
                    info.url,
                    info.isBookmark(bookMarkInfoList),
                    if (appOption.value?.visit == Const.VISIT.VISIBLE) info.isVisit(visitList) else false
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000),
            PagingData.empty(),
        )

    val bookMarkUiState: StateFlow<List<MainUiState>> = bookMarkList.map { it.map { info ->
        MainUiState(info.pID, info.title, info.host, info.sDate, info.eDate, info.state, info.url)
    } }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10000),
        emptyList(),
    )

    fun search(searchOption: SearchOption, action: () -> Unit) {
        viewModelScope.launch {
            repository.loadInfo(searchOption).cachedIn(viewModelScope).collect {
                infoList.value = it
                action.invoke()
            }
        }
    }

    fun clickVolunteer(mainUiState: MainUiState, action: (DetailUiState) -> Unit, error: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val volunteer = repository.loadVolunteer(mainUiState.pID)?.let {
                    repository.insertVisit(Visit(mainUiState.pID))
                    action.invoke(
                        DetailUiState(
                            it.pID,
                            it.title,
                            it.area,
                            it.host,
                            it.sDate,
                            it.eDate,
                            it.state,
                            it.isAdult,
                            it.isYoung,
                            it.field,
                            it.place,
                            it.sHour,
                            it.eHour,
                            it.nsDate,
                            it.neDate,
                            it.person,
                            it.manager,
                            it.tel,
                            it.email,
                            it.contents,
                            it.address,
                            mainUiState.isBookmark,
                            mainUiState.url,
                        )
                    )
                }

                if (volunteer == null) {
                    repository.deleteBookMark(mainUiState.pID)
                    error.invoke()
                }
            } catch (e: Exception) {
                repository.deleteBookMark(mainUiState.pID)
                error.invoke()
            }
        }
    }

    suspend fun setOption(option: String?, type: String) {
        viewModelScope.launch {
            repository.updateOption(option, type)
        }
    }

    suspend fun setOption(option1: String?, type1: String, option2: String?, type2: String) {
        viewModelScope.launch {
            repository.updateOption(option1, type1, option2, type2)
        }
    }

    suspend fun resetOption() {
        viewModelScope.launch {
            repository.resetOption()
        }
    }

    fun deleteVisitHistory() {
        viewModelScope.launch {
            repository.deleteAllVisit()
        }
    }

    fun applyTheme(option: String) {
        when(option) {
            Const.THEME.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Const.THEME.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Const.THEME.SYSTEM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }
    }

    fun applyLanguage(language: String) {
        AppCompatDelegate.setApplicationLocales(
            when(language) {
                Const.LANGUAGE.ENGLISH -> LocaleListCompat.forLanguageTags(Const.LANGUAGE.ENGLISH)
                else -> LocaleListCompat.forLanguageTags(Const.LANGUAGE.KOREAN)
            }
        )
    }
}
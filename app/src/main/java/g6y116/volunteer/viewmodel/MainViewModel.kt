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
import g6y116.volunteer.data.Info
import g6y116.volunteer.data.Visit
import g6y116.volunteer.data.SearchOption
import g6y116.volunteer.repository.VolunteerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    var infoFlowList: MutableStateFlow<PagingData<Info>> = MutableStateFlow(PagingData.empty())
    val bookmarkLiveList: LiveData<List<Info>> = repository.getBookmarkLiveList()
    val visitLiveList: LiveData<List<Visit>> = repository.getVisitLiveList()

    val searchOptionLiveData: MutableLiveData<SearchOption> = MutableLiveData()
    val themeOptionLiveData: MutableLiveData<String> = MutableLiveData()
    val languageOptionLiveData: MutableLiveData<String> = MutableLiveData()
    val visitOptionLiveData: MutableLiveData<String> = MutableLiveData()

    var contextMenuClickPosition: MutableLiveData<Int> = MutableLiveData()

    init {
        getSearchOption()
        getThemeOption()
        getLanguageOption()
        getVisitOption()
    }

    private fun getSearchOption() {
        viewModelScope.launch(Dispatchers.Main) {
            val searchOption = repository.getSearchOption()
            searchOptionLiveData.value = searchOption
            repository.getVolunteerFlowList(searchOption).cachedIn(viewModelScope).collect {
                infoFlowList.value = it
            }
        }
    }

    fun setSearchOption(searchOption: SearchOption) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setSearchOption(searchOption)
            searchOptionLiveData.value = searchOption
            repository.getVolunteerFlowList(searchOption).cachedIn(viewModelScope).collect {
                infoFlowList.value = it
            }
        }
    }

    private fun getThemeOption() {
        viewModelScope.launch(Dispatchers.Main) {
            val themeOption = repository.getThemeOption()
            themeOptionLiveData.value = themeOption
            applyTheme(themeOption)
        }
    }

    fun setThemeOption(option: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setThemeOption(option)
            themeOptionLiveData.value = option
            applyTheme(option)
        }
    }

    private fun applyTheme(option: String) {
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

    private fun getLanguageOption() {
        viewModelScope.launch(Dispatchers.Main) {
            languageOptionLiveData.value = repository.getLanguageOption()
        }
    }

    fun setLanguageOption(language: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setLanguageOption(language)
            languageOptionLiveData.value = language
            applyLanguage(language)
        }
    }

    private fun applyLanguage(language: String) {
        AppCompatDelegate.setApplicationLocales(
            when(language) {
                Const.LANGUAGE.ENGLISH -> LocaleListCompat.forLanguageTags(Const.LANGUAGE.ENGLISH)
                else -> LocaleListCompat.forLanguageTags(Const.LANGUAGE.KOREAN)
            }
        )
    }

    private fun getVisitOption() {
        viewModelScope.launch(Dispatchers.Main) {
            visitOptionLiveData.value = repository.getVisitOption()
        }
    }

    fun setVisitOption(option: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.setVisitOption(option)
            visitOptionLiveData.value = option
        }
    }

    fun deleteVisitHistory() {
        viewModelScope.launch {
            repository.removeVisit()
        }
    }

    fun clickContextMenu(pID: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.removeBookmark(pID)
        }
    }

    fun clickContextMenu(pID: String, infoList: List<Info>) {
        val info = infoList.find { it.pID == pID }
        val isBookmark = bookmarkLiveList.value?.any { it.pID == pID } ?: false
        info?.let {
            viewModelScope.launch(Dispatchers.Main) {
                if (isBookmark) repository.removeBookmark(pID)
                else repository.addBookmark(it)
            }
        }
    }
}
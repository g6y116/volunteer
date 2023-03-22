package g6y116.volunteer.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import g6y116.volunteer.Const
import g6y116.volunteer.api.KakaoApi
import g6y116.volunteer.api.VolunteerApi
import g6y116.volunteer.dao.BookmarkDao
import g6y116.volunteer.dao.VisitDao
import g6y116.volunteer.data.*
import g6y116.volunteer.datasource.HomePagingSource
import g6y116.volunteer.di.AppModule
import g6y116.volunteer.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface VolunteerRepository {
    suspend fun getCoordinate(address: String): Coordinate?

    suspend fun getVolunteer(pID: String): Volunteer?
    fun getVolunteerFlowList(searchOption: SearchOption): Flow<PagingData<Info>>

    fun getBookmarkLiveList(): LiveData<List<Info>>
    suspend fun getBookmarkList(): List<Info>
    fun getVisitLiveList(): LiveData<List<Visit>>
    suspend fun getVisitList(): List<Visit>

    suspend fun addBookmark(info: Info)
    suspend fun removeBookmark(pID: String)
    suspend fun addVisit(visit: Visit)
    suspend fun removeVisit()

    suspend fun setSearchOption(searchOption: SearchOption)
    suspend fun getSearchOption(): SearchOption
    suspend fun setThemeOption(option: String)
    suspend fun getThemeOption(): String
    suspend fun setLanguageOption(language: String)
    suspend fun getLanguageOption(): String
    suspend fun setVisitOption(option: String)
    suspend fun getVisitOption(): String
}

class VolunteerRepositoryImpl @Inject constructor(
    private val api: VolunteerApi,
    private val kakoApi: KakaoApi,
    private val bookmarkDao: BookmarkDao,
    private val visitDao: VisitDao,
    private val dataStore: DataStore<Preferences>
) : VolunteerRepository {
    override suspend fun getCoordinate(address: String): Coordinate? =
        (kakoApi.getAddress(address).body() as? KakaoResponse)?.documents?.get(0)?.run {
            log("${road_address.toString()}")
            Coordinate(x, y)
        }

    override suspend fun getVolunteer(pID: String): Volunteer? = (api.getVolunteer(pID).body() as DetailResponse).body.items.item?.toVolunteer()
    override fun getVolunteerFlowList(searchOption: SearchOption): Flow<PagingData<Info>> =
        Pager(
            config = PagingConfig(pageSize = Const.LOAD_SIZE, enablePlaceholders = true),
            pagingSourceFactory = { HomePagingSource(api, searchOption) },
        ).flow

    override fun getBookmarkLiveList(): LiveData<List<Info>> = bookmarkDao.getLiveList()
    override suspend fun getBookmarkList(): List<Info> = bookmarkDao.getList()
    override fun getVisitLiveList(): LiveData<List<Visit>> = visitDao.getLiveList()
    override suspend fun getVisitList(): List<Visit> = visitDao.getList()

    override suspend fun addBookmark(info: Info) = bookmarkDao.add(info)
    override suspend fun removeBookmark(pID: String) = bookmarkDao.remove(pID)
    override suspend fun addVisit(visit: Visit) = visitDao.add(visit)
    override suspend fun removeVisit() = visitDao.remove()

    override suspend fun setSearchOption(searchOption: SearchOption) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.SIDO)] = searchOption.siDoCode ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.GOOGUN)] = searchOption.gooGunCode ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.START_DATE)] = searchOption.sDate ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.END_DATE)] = searchOption.eDate ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.ADULT)] = searchOption.isAdult ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.YOUNG)] = searchOption.isYoung ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.STATE)] = searchOption.state ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.KEY_WORD)] = searchOption.keyWord ?: ""
        }
    }

    override suspend fun getSearchOption(): SearchOption =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else { throw exception }
        }
        .map { prefs ->
            SearchOption(
                prefs[stringPreferencesKey(Const.PrefKey.SIDO)].let { if (it != "") it else null},
                prefs[stringPreferencesKey(Const.PrefKey.GOOGUN)].let { if (it != "") it else null},
                prefs[stringPreferencesKey(Const.PrefKey.START_DATE)].let { if (it != "") it else null},
                prefs[stringPreferencesKey(Const.PrefKey.END_DATE)].let { if (it != "") it else null},
                prefs[stringPreferencesKey(Const.PrefKey.ADULT)].let { if (it != "") it else null},
                prefs[stringPreferencesKey(Const.PrefKey.YOUNG)].let { if (it != "") it else null},
                prefs[stringPreferencesKey(Const.PrefKey.KEY_WORD)].let { if (it != "") it else null},
                prefs[stringPreferencesKey(Const.PrefKey.STATE)].let { if (it != "") it else null},
            )
        }.first()


    override suspend fun setThemeOption(option: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.THEME)] = option
        }
    }

    override suspend fun getThemeOption(): String =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else { throw exception }
        }
        .map { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.THEME)] ?: Const.THEME.SYSTEM
        }.first()

    override suspend fun setLanguageOption(language: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.LANGUAGE)] = language
        }
    }

    override suspend fun getLanguageOption(): String =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else { throw exception }
        }
        .map { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.LANGUAGE)] ?: Const.LANGUAGE.KOREAN
        }.first()

    override suspend fun setVisitOption(option: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.VISIT)] = option
        }
    }

    override suspend fun getVisitOption(): String =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else { throw exception }
        }
        .map { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.VISIT)] ?: Const.VISIT.VISIBLE
        }.first()
}
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
import g6y116.volunteer.api.VolunteerApi
import g6y116.volunteer.dao.VolunteerDao
import g6y116.volunteer.data.DetailResponse
import g6y116.volunteer.data.RecentSearch
import g6y116.volunteer.data.Volunteer
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.datasource.HomePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface VolunteerRepository {
    fun getHomeList(recentSearch: RecentSearch): Flow<PagingData<VolunteerInfo>>
    fun getBookMarkLiveList(): LiveData<List<VolunteerInfo>>
    suspend fun getBookMarkList(): List<VolunteerInfo>
    suspend fun getDetail(pID: String): Volunteer
    suspend fun addBookMark(volunteerInfo: VolunteerInfo)
    suspend fun removeBookMark(pID: String)
    suspend fun resetRecentSearch()
    suspend fun saveRecentSearch(recentSearch: RecentSearch)
    suspend fun loadRecentSearch(): Flow<RecentSearch>
}

class VolunteerRepositoryImpl @Inject constructor(
    private val api: VolunteerApi,
    private val volunteerDao: VolunteerDao,
    private val dataStore: DataStore<Preferences>
) : VolunteerRepository {
    override fun getHomeList(recentSearch: RecentSearch): Flow<PagingData<VolunteerInfo>> {
        return Pager(
            config = PagingConfig(pageSize = Const.LOAD_SIZE, enablePlaceholders = true),
            pagingSourceFactory = { HomePagingSource(api, recentSearch) },
        ).flow
    }

    override fun getBookMarkLiveList(): LiveData<List<VolunteerInfo>> = volunteerDao.getVolunteerLiveList()
    override suspend fun getBookMarkList(): List<VolunteerInfo> = volunteerDao.getVolunteerList()
    override suspend fun getDetail(pID: String): Volunteer = (api.getVolunteerDetail(pID).body() as DetailResponse).body.items.item.toVolunteer()
    override suspend fun addBookMark(volunteerInfo: VolunteerInfo) = volunteerDao.addVolunteer(volunteerInfo)
    override suspend fun removeBookMark(pID: String) = volunteerDao.removeVolunteer(pID)
    override suspend fun resetRecentSearch() {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("siDoCode")] = ""
            prefs[stringPreferencesKey("gooGunCode")] = ""
            prefs[stringPreferencesKey("sDate")] = ""
            prefs[stringPreferencesKey("eDate")] = ""
            prefs[stringPreferencesKey("isAdultPossible")] = ""
            prefs[stringPreferencesKey("isYoungPossible")] = ""
            prefs[stringPreferencesKey("keyWord")] = ""
        }
    }
    override suspend fun saveRecentSearch(recentSearch: RecentSearch) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("siDoCode")] = recentSearch.siDoCode
            prefs[stringPreferencesKey("gooGunCode")] = recentSearch.gooGunCode
            prefs[stringPreferencesKey("sDate")] = recentSearch.sDate
            prefs[stringPreferencesKey("eDate")] = recentSearch.eDate
            prefs[stringPreferencesKey("isAdultPossible")] = recentSearch.isAdultPossible
            prefs[stringPreferencesKey("isYoungPossible")] = recentSearch.isYoungPossible
            prefs[stringPreferencesKey("keyWord")] = recentSearch.keyWord
        }
    }
    override suspend fun loadRecentSearch(): Flow<RecentSearch> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else { throw exception }
        }
        .map { prefs ->
            RecentSearch(
                prefs[stringPreferencesKey("siDoCode")] ?: "",
                prefs[stringPreferencesKey("gooGunCode")] ?: "",
                prefs[stringPreferencesKey("sDate")] ?: "",
                prefs[stringPreferencesKey("eDate")] ?: "",
                prefs[stringPreferencesKey("isAdultPossible")] ?: Const.TRUE,
                prefs[stringPreferencesKey("isYoungPossible")] ?: Const.TRUE,
                prefs[stringPreferencesKey("keyWord")] ?: "",

            )
        }
    }
}
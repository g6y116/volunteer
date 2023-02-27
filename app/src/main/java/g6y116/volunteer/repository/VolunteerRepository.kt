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
import g6y116.volunteer.dao.BookMarkDao
import g6y116.volunteer.dao.ReadDao
import g6y116.volunteer.data.*
import g6y116.volunteer.datasource.HomePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface VolunteerRepository {
    fun getHomeList(recentSearch: RecentSearch): Flow<PagingData<VolunteerInfo>>
    fun getBookMarkLiveList(): LiveData<List<VolunteerInfo>>
    suspend fun getBookMarkList(): List<VolunteerInfo>
    fun getReadLiveList(): LiveData<List<Read>>
    suspend fun getReadList(): List<Read>
    suspend fun getDetail(pID: String): Volunteer?
    suspend fun addBookMark(volunteerInfo: VolunteerInfo)
    suspend fun removeBookMark(pID: String)
    suspend fun addRead(read: Read)
    suspend fun removeRead()
    suspend fun setRecentSearch(recentSearch: RecentSearch)
    suspend fun getRecentSearch(): Flow<RecentSearch>
    suspend fun setMode(mode: String)
    suspend fun getMode(): String
    suspend fun setLocale(mode: String)
    suspend fun getLocale(): String
    suspend fun setStateOption(mode: String)
    suspend fun getStateOption(): String
    suspend fun setReadOption(mode: String)
    suspend fun getReadOption(): String
}

class VolunteerRepositoryImpl @Inject constructor(
    private val api: VolunteerApi,
    private val bookMarkDao: BookMarkDao,
    private val readDao: ReadDao,
    private val dataStore: DataStore<Preferences>
) : VolunteerRepository {

    override fun getHomeList(recentSearch: RecentSearch): Flow<PagingData<VolunteerInfo>> {
        return Pager(
            config = PagingConfig(pageSize = Const.LOAD_SIZE, enablePlaceholders = true),
            pagingSourceFactory = { HomePagingSource(api, recentSearch) },
        ).flow
    }

    override fun getBookMarkLiveList(): LiveData<List<VolunteerInfo>> = bookMarkDao.getBookMarkLiveList()
    override suspend fun getBookMarkList(): List<VolunteerInfo> = bookMarkDao.getBookMarkList()
    override fun getReadLiveList(): LiveData<List<Read>> = readDao.getReadLiveList()
    override suspend fun getReadList(): List<Read> = readDao.getReadList()

    override suspend fun getDetail(pID: String): Volunteer? = (api.getVolunteerDetail(pID).body() as DetailResponse).body.items.item?.toVolunteer()
    override suspend fun addBookMark(volunteerInfo: VolunteerInfo) = bookMarkDao.addBookMark(volunteerInfo)
    override suspend fun removeBookMark(pID: String) = bookMarkDao.removeBookMark(pID)
    override suspend fun addRead(read: Read) = readDao.addRead(read)
    override suspend fun removeRead() = readDao.removeRead()

    override suspend fun setRecentSearch(recentSearch: RecentSearch) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.SI_DO_CODE)] = recentSearch.siDoCode ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.GOO_GUN_CODE)] = recentSearch.gooGunCode ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.S_DATE)] = recentSearch.sDate ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.E_DATE)] = recentSearch.eDate ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.IS_ADULT_POSSIBLE)] = recentSearch.isAdultPossible ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.IS_YOUNG_POSSIBLE)] = recentSearch.isYoungPossible ?: ""
            prefs[stringPreferencesKey(Const.PrefKey.KEY_WORD)] = recentSearch.keyWord ?: ""
        }
    }
    override suspend fun getRecentSearch(): Flow<RecentSearch> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else { throw exception }
        }
            .map { prefs ->
                RecentSearch(
                    prefs[stringPreferencesKey(Const.PrefKey.SI_DO_CODE)].let { if (it != "") it else null},
                    prefs[stringPreferencesKey(Const.PrefKey.GOO_GUN_CODE)].let { if (it != "") it else null},
                    prefs[stringPreferencesKey(Const.PrefKey.S_DATE)].let { if (it != "") it else null},
                    prefs[stringPreferencesKey(Const.PrefKey.E_DATE)].let { if (it != "") it else null},
                    prefs[stringPreferencesKey(Const.PrefKey.IS_ADULT_POSSIBLE)].let { if (it != "") it else null},
                    prefs[stringPreferencesKey(Const.PrefKey.IS_YOUNG_POSSIBLE)].let { if (it != "") it else null},
                    prefs[stringPreferencesKey(Const.PrefKey.KEY_WORD)].let { if (it != "") it else null},
                )
            }
    }

    override suspend fun setMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.MODE)] = mode
        }
    }

    override suspend fun getMode(): String =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else { throw exception }
            }
            .map { prefs ->
                prefs[stringPreferencesKey(Const.PrefKey.MODE)] ?: Const.MODE.SYSTEM_MODE
            }
            .first()

    override suspend fun setLocale(mode: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.LOCALE)] = mode
        }
    }

    override suspend fun getLocale(): String =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else { throw exception }
            }
            .map { prefs ->
                prefs[stringPreferencesKey(Const.PrefKey.LOCALE)] ?: Const.LOCALE.KO
            }
            .first()

    override suspend fun setStateOption(mode: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.STATE)] = mode
        }
    }

    override suspend fun getStateOption(): String =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else { throw exception }
            }
            .map { prefs ->
                prefs[stringPreferencesKey(Const.PrefKey.STATE)] ?: Const.STATE.ALL
            }
            .first()


    override suspend fun setReadOption(mode: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.READ)] = mode
        }
    }

    override suspend fun getReadOption(): String =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else { throw exception }
            }
            .map { prefs ->
                prefs[stringPreferencesKey(Const.PrefKey.READ)] ?: Const.READ.VISIBLE
            }
            .first()
}
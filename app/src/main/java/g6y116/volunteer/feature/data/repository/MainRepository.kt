package g6y116.volunteer.feature.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import g6y116.volunteer.base.consts.Const
import g6y116.volunteer.feature.data.datasource.*
import g6y116.volunteer.feature.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MainRepository {
    fun loadInfo(searchOption: SearchOption): Flow<PagingData<Info>>
    fun loadBookMarkInfo(): Flow<List<Info>>
    fun loadVisit(): Flow<List<Visit>>
    suspend fun loadVolunteer(pID: String): Volunteer?
    fun loadSearchOption(): Flow<SearchOption>
    fun loadAppOption(): Flow<AppOption>
    suspend fun deleteBookMark(pID: String)
    suspend fun insertVisit(visit: Visit)
    suspend fun deleteAllVisit()
    suspend fun updateOption(option: String?, type: String)
    suspend fun updateOption(option1: String?, type1: String, option2: String?, type2: String)
    suspend fun resetOption()
}

class MainRepositoryImpl @Inject constructor(
    private val openApiRemoteSource: OpenApiRemoteSource,
    private val kakaoRemoteSource: KakaoRemoteSource,
    private val infoLocalSource: InfoLocalSource,
    private val visitLocalSource: VisitLocalSource,
    private val dataStore: DataStore<Preferences>
) : MainRepository {

    override fun loadInfo(searchOption: SearchOption): Flow<PagingData<Info>> =
        Pager(
            config = PagingConfig(pageSize = Const.LOAD_SIZE, enablePlaceholders = true),
            pagingSourceFactory = { OpenApiPagingSource(openApiRemoteSource, searchOption) },
        ).flow

    override fun loadBookMarkInfo(): Flow<List<Info>> = infoLocalSource.load()

    override fun loadVisit(): Flow<List<Visit>> = visitLocalSource.load()

    override suspend fun loadVolunteer(pID: String): Volunteer? =
        (openApiRemoteSource.loadVolunteer(pID).body() as DetailResponse).body.items.item?.toVolunteer()

    override fun loadSearchOption(): Flow<SearchOption> =
        dataStore.data.map { prefs ->
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
        }

    override fun loadAppOption(): Flow<AppOption> =
        dataStore.data.map { prefs ->
            AppOption(
                prefs[stringPreferencesKey(Const.PrefKey.THEME)].let { if (it != "") it else Const.THEME.SYSTEM } ?: Const.THEME.SYSTEM,
                prefs[stringPreferencesKey(Const.PrefKey.LANGUAGE)].let { if (it != "") it else Const.LANGUAGE.KOREAN } ?: Const.LANGUAGE.KOREAN,
                prefs[stringPreferencesKey(Const.PrefKey.VISIT)].let { if (it != "") it else Const.VISIT.VISIBLE } ?: Const.VISIT.VISIBLE,
            )
        }

    override suspend fun deleteBookMark(pID: String) = infoLocalSource.delete(pID)

    override suspend fun insertVisit(visit: Visit) = visitLocalSource.insert(visit)

    override suspend fun deleteAllVisit() = visitLocalSource.deleteAll()

    override suspend fun updateOption(option: String?, type: String) {
        dataStore.edit { prefs -> prefs[stringPreferencesKey(type)] = option ?: "" }
    }

    override suspend fun updateOption(option1: String?, type1: String, option2: String?, type2: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(type1)] = option1 ?: ""
            prefs[stringPreferencesKey(type2)] = option2 ?: ""
        }
    }

    override suspend fun resetOption() {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(Const.PrefKey.SIDO)] = ""
            prefs[stringPreferencesKey(Const.PrefKey.GOOGUN)] = ""
            prefs[stringPreferencesKey(Const.PrefKey.START_DATE)] = ""
            prefs[stringPreferencesKey(Const.PrefKey.END_DATE)] = ""
            prefs[stringPreferencesKey(Const.PrefKey.ADULT)] = ""
            prefs[stringPreferencesKey(Const.PrefKey.YOUNG)] = ""
            prefs[stringPreferencesKey(Const.PrefKey.STATE)] = ""
            prefs[stringPreferencesKey(Const.PrefKey.KEY_WORD)] = ""
        }
    }
}
package g6y116.volunteer.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.*
import androidx.room.Dao
import g6y116.volunteer.Const
import g6y116.volunteer.data.DetailResponse
import g6y116.volunteer.data.HomeResponse
import g6y116.volunteer.data.Volunteer
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.datasource.HomePagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface VolunteerRepository {
    fun getHomeList(
        pageNum: Int = 1,
        siDoCode: String = "",
        gooGunCode: String = "",
        sDate: String = "",
        eDate: String = "",
        keyWord: String = "",
        isAdultPossible: String = Const.ALL,
        isYoungPossible: String = Const.ALL,
    ): Flow<PagingData<VolunteerInfo>>
    fun getBookMarkList(): LiveData<List<VolunteerInfo>>
    suspend fun getDetail(pID: String): Volunteer
    suspend fun addBookMark(volunteerInfo: VolunteerInfo)
    suspend fun removeBookMark(pID: String)
}

class VolunteerRepositoryImpl @Inject constructor(
    private val api: VolunteerApi,
    private val volunteerInfoDao: VolunteerInfoDao,
) : VolunteerRepository {
    override fun getHomeList(
        pageNum: Int,
        siDoCode: String,
        gooGunCode: String,
        sDate: String,
        eDate: String,
        keyWord: String,
        isAdultPossible: String,
        isYoungPossible: String,
    ): Flow<PagingData<VolunteerInfo>> {
        return Pager(
            config = PagingConfig(pageSize = Const.LOAD_SIZE, enablePlaceholders = true),
            pagingSourceFactory = { HomePagingSource(
                api,
                pageNum,
                keyWord,
                sDate,
                eDate,
                siDoCode,
                gooGunCode,
                isAdultPossible,
                isYoungPossible,
                Const.SERVICE_KEY
            ) },
        ).flow
    }

    override fun getBookMarkList(): LiveData<List<VolunteerInfo>> {
        return volunteerInfoDao.getVolunteerInfoList()
    }

    override suspend fun getDetail(pID: String): Volunteer {
        return (api.getVolunteer(pID).body() as DetailResponse).body.items.item.toVolunteer()
    }

    override suspend fun addBookMark(volunteerInfo: VolunteerInfo) {
        volunteerInfoDao.addVolunteerInfo(volunteerInfo)
    }

    override suspend fun removeBookMark(pID: String) {
        volunteerInfoDao.removeVolunteerInfo(pID)
    }
}

interface VolunteerApi {
    @GET("getVltrSearchWordList")
    suspend fun getVolunteerInfoList(
        @Query("pageNo") pageNum: Int,
        @Query("schSido") siDoCode: String,
        @Query("schSign1") gooGunCode: String,
        @Query("progrmBgnde") sDate: String,
        @Query("progrmEndde") eDate: String,
        @Query("keyword") keyWord: String,
        @Query("adultPosblAt") isAdultPossible: String,
        @Query("yngbgsPosblAt") isYoungPossible: String,
        @Query("serviceKey") serviceKey: String = Const.SERVICE_KEY,
        @Query("numOfRows") numOfRows: Int = Const.LOAD_SIZE,
    ) : Response<HomeResponse>

    @GET("getVltrPartcptnItem")
    suspend fun getVolunteer(
        @Query("progrmRegistNo") pID: String,
        @Query("serviceKey") serviceKey: String = Const.SERVICE_KEY,
    ) : Response<DetailResponse>
}

@Dao
interface VolunteerInfoDao {
    @androidx.room.Query("SELECT * FROM volunteer_info")
    fun getVolunteerInfoList(): LiveData<List<VolunteerInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addVolunteerInfo(volunteerInfo: VolunteerInfo)

    @androidx.room.Query("DELETE FROM volunteer_info WHERE p_id = :pID")
    suspend fun removeVolunteerInfo(pID: String)
}
package g6y116.volunteer.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import g6y116.volunteer.Const
import g6y116.volunteer.api.VolunteerApi
import g6y116.volunteer.dao.VolunteerDao
import g6y116.volunteer.data.DetailResponse
import g6y116.volunteer.data.Volunteer
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.datasource.HomePagingSource
import kotlinx.coroutines.flow.Flow
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

    fun getBookMarkLiveList(): LiveData<List<VolunteerInfo>>
    suspend fun getBookMarkList(): List<VolunteerInfo>
    suspend fun getDetail(pID: String): Volunteer
    suspend fun addBookMark(volunteerInfo: VolunteerInfo)
    suspend fun removeBookMark(pID: String)
}

class VolunteerRepositoryImpl @Inject constructor(
    private val api: VolunteerApi,
    private val volunteerDao: VolunteerDao,
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
            pagingSourceFactory = { HomePagingSource(api, pageNum, keyWord, sDate, eDate, siDoCode, gooGunCode, isAdultPossible, isYoungPossible) },
        ).flow
    }

    override fun getBookMarkLiveList(): LiveData<List<VolunteerInfo>> = volunteerDao.getVolunteerLiveList()
    override suspend fun getBookMarkList(): List<VolunteerInfo> = volunteerDao.getVolunteerList()
    override suspend fun getDetail(pID: String): Volunteer = (api.getVolunteerDetail(pID).body() as DetailResponse).body.items.item.toVolunteer()
    override suspend fun addBookMark(volunteerInfo: VolunteerInfo) = volunteerDao.addVolunteer(volunteerInfo)
    override suspend fun removeBookMark(pID: String) = volunteerDao.removeVolunteer(pID)
}
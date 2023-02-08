package g6y116.volunteer.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.*
import androidx.room.Dao
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import g6y116.volunteer.Const
import g6y116.volunteer.datasource.HomePagingSource
import kotlinx.coroutines.flow.Flow
import org.apache.commons.text.StringEscapeUtils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

@Dao
interface VolunteerInfoDao {
    @androidx.room.Query("SELECT * FROM volunteer_info ORDER BY pID DESC")
    fun getBookMarkInfoList(): LiveData<List<VolunteerInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookMarkInfo(item: VolunteerInfo)

    @androidx.room.Query("DELETE FROM volunteer_info WHERE pID = :pID")
    suspend fun removeBookMarkInfo(pID: String)
}

@Dao
interface VolunteerDao {
    @androidx.room.Query("SELECT * FROM volunteer WHERE pID = :pID ORDER BY pID DESC")
    fun getBookMark(pID: String): LiveData<Volunteer>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookMark(item: Volunteer)

    @androidx.room.Query("DELETE FROM volunteer WHERE pID = :pID")
    suspend fun removeBookMark(pID: String)
}

interface VolunteerApi {
    @GET("getVltrSearchWordList")
    suspend fun getVolunteerList(
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
    suspend fun getVolunteerDetail(
        @Query("progrmRegistNo") pID: String,
        @Query("serviceKey") serviceKey: String = Const.SERVICE_KEY,
    ) : Response<DetailResponse>
}

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
    suspend fun getHomeDetail(pID: String): Volunteer
    fun getBookMarkInfoList(): LiveData<List<VolunteerInfo>>
    fun getBookMark(pID: String): LiveData<Volunteer>
    suspend fun addBookMark(volunteerInfo: VolunteerInfo, volunteer: Volunteer)
    suspend fun removeBookMark(volunteerInfo: VolunteerInfo, volunteer: Volunteer)
}

class VolunteerRepositoryImpl @Inject constructor(
    private val api: VolunteerApi,
    private val volunteerInfoDao: VolunteerInfoDao,
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

    override suspend fun getHomeDetail(pID: String): Volunteer {
        return (api.getVolunteerDetail(pID).body() as DetailResponse).body.items.item.toVolunteer()
    }

    override fun getBookMarkInfoList(): LiveData<List<VolunteerInfo>> {
        return volunteerInfoDao.getBookMarkInfoList()
    }

    override fun getBookMark(pID: String): LiveData<Volunteer> {
        return volunteerDao.getBookMark(pID)
    }

    override suspend fun addBookMark(volunteerInfo: VolunteerInfo, volunteer: Volunteer) {
        volunteerInfoDao.addBookMarkInfo(volunteerInfo)
        volunteerDao.addBookMark(volunteer)
    }

    override suspend fun removeBookMark(volunteerInfo: VolunteerInfo, volunteer: Volunteer) {
        volunteerInfoDao.removeBookMarkInfo(volunteerInfo.pID)
        volunteerDao.removeBookMark(volunteer.pID)
    }
}

@Xml(name="response")
data class HomeResponse(
    @Element(name="header")
    val header: Header,
    @Element(name = "body")
    val body: HomeBody,
)

@Xml(name="response")
data class DetailResponse(
    @Element(name="header")
    val header: Header,
    @Element(name = "body")
    val body: DetailBody,
)

@Xml(name = "header")
data class Header(
    @PropertyElement(name="resultCode")
    val resultCode: Int,
    @PropertyElement(name="resultMsg")
    val resultMsg: String,
)

@Xml(name = "body")
data class HomeBody(
    @Element(name="items")
    val items: HomeItems,
    @PropertyElement(name="numOfRows")
    val numOfRows: Int,
    @PropertyElement(name="pageNo")
    val pageNo: Int,
    @PropertyElement(name="totalCount")
    val totalCount: Int,
)

@Xml(name = "body")
data class DetailBody(
    @Element(name="items")
    val items: DetailItems,
    @PropertyElement(name="numOfRows")
    val numOfRows: Int,
    @PropertyElement(name="pageNo")
    val pageNo: Int,
    @PropertyElement(name="totalCount")
    val totalCount: Int,
)

@Xml(name= "items")
data class HomeItems(
    @Element(name="item")
    val item: List<VolunteerInfoRes>
)

@Xml(name= "items")
data class DetailItems(
    @Element(name="item")
    val item: VolunteerRes
)

@Xml(name= "item")
data class VolunteerInfoRes(
    @PropertyElement(name="progrmRegistNo") var pID: String,
    @PropertyElement(name="progrmSj") var title: String,
    @PropertyElement(name="nanmmbyNm") var host: String,
    @PropertyElement(name="progrmBgnde") var sDate: String,
    @PropertyElement(name="progrmEndde") var eDate: String,
    @PropertyElement(name="progrmSttusSe") var state: Int,
    @PropertyElement(name="url") var url: String,
) {
    fun toVolunteerInfo() = this.run {
        VolunteerInfo(
            pID = pID,
            title= StringEscapeUtils.unescapeHtml4(title),
            host = StringEscapeUtils.unescapeHtml4(host),
            sDate = sDate,
            eDate = eDate,
            state = state,
            url = StringEscapeUtils.unescapeHtml4(url)
        )
    }
}

@Xml(name= "item")
data class VolunteerRes(
    @PropertyElement(name="progrmRegistNo") var pID: String,
    @PropertyElement(name="progrmSj") var title: String,
    @PropertyElement(name="nanmmbyNm") var area: String,
    @PropertyElement(name="mnnstNm") val host: String,
    @PropertyElement(name="progrmBgnde") var sDate: String,
    @PropertyElement(name="progrmEndde") var eDate: String,
    @PropertyElement(name="progrmSttusSe") var state: Int,
    @PropertyElement(name="sidoCd") var siDoCode: String,
    @PropertyElement(name="gugunCd") var gooGunCode: String,
    @PropertyElement(name="adultPosblAt") var adultPossible: String,
    @PropertyElement(name="yngbgsPosblAt") var youngPossible: String,
    @PropertyElement(name="srvcClCode") var field: String,
    @PropertyElement(name="actPlace") var place: String,
    @PropertyElement(name="actBeginTm") var sHour: Int,
    @PropertyElement(name="actEndTm") var eHour: Int,
    @PropertyElement(name="noticeBgnde") var nsDate: String,
    @PropertyElement(name="noticeEndde") var neDate: String,
    @PropertyElement(name="rcritNmpr") val num: Int,
    @PropertyElement(name="nanmmbyNmAdmn") val manager: String,
    @PropertyElement(name="telno") val tel: String,
    @PropertyElement(name="email") val email: String,
    @PropertyElement(name="progrmCn") val contents: String,
    @PropertyElement(name="postAdres") val address: String,
) {
    fun toVolunteer() = this.run {
        Volunteer(
            pID = pID,
            title= StringEscapeUtils.unescapeHtml4(title),
            area = StringEscapeUtils.unescapeHtml4(area),
            host = StringEscapeUtils.unescapeHtml4(host),
            sDate = sDate,
            eDate = eDate,
            state = state,
            siDoCode = siDoCode,
            gooGunCode = gooGunCode,
            isAdultPossible = adultPossible,
            isYoungPossible = youngPossible,
            field = StringEscapeUtils.unescapeHtml4(field),
            place = StringEscapeUtils.unescapeHtml4(place),
            sHour = sHour,
            eHour = eHour,
            nsDate = nsDate,
            neDate = neDate,
            num = num,
            manager = StringEscapeUtils.unescapeHtml4(manager),
            tel = StringEscapeUtils.unescapeHtml4(tel),
            email = StringEscapeUtils.unescapeHtml4(email),
            contents = StringEscapeUtils.unescapeHtml4(contents),
            address = StringEscapeUtils.unescapeHtml4(address),
        )
    }
}

@Entity(tableName = "volunteer_info")
data class VolunteerInfo(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pID") val pID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "sDate") val sDate: String,
    @ColumnInfo(name = "eDate") val eDate: String,
    @ColumnInfo(name = "state") val state: Int,
    @ColumnInfo(name = "url") val url: String,
)

@Entity(tableName = "volunteer")
data class Volunteer(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pID") val pID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "area") val area: String,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "sDate") val sDate: String,
    @ColumnInfo(name = "eDate") val eDate: String,
    @ColumnInfo(name = "state") val state: Int,
    @ColumnInfo(name = "siDoCode") val siDoCode: String,
    @ColumnInfo(name = "gooGunCode") val gooGunCode: String,
    @ColumnInfo(name = "isAdultPossible") val isAdultPossible: String, // 성인 가능 여부
    @ColumnInfo(name = "isYoungPossible") val isYoungPossible: String, // 청소년 가능 여부
    @ColumnInfo(name = "field") val field: String, // 분야
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "sHour") val sHour: Int,
    @ColumnInfo(name = "eHour") val eHour: Int,
    @ColumnInfo(name = "nsDate") val nsDate: String, // 모집 시작일
    @ColumnInfo(name = "neDate") val neDate: String, // 모집 종료일
    @ColumnInfo(name = "num") val num: Int,
    @ColumnInfo(name = "manager") val manager: String,
    @ColumnInfo(name = "tel") val tel: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "contents") val contents: String,
    @ColumnInfo(name = "address") val address: String,
)
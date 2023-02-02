package g6y116.volunteer.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import g6y116.volunteer.Const
import g6y116.volunteer.datasource.HomePagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

@Dao
interface VolunteerInfoDao {

}

@Dao
interface VolunteerDao {

}

interface VolunteerApi {
    @GET("getVltrSearchWordList")
    suspend fun getVolunteerList(
        @Query("progrmBgnde") sDate: String,
        @Query("progrmEndde") eDate: String,
        @Query("adultPosblAt") isAdultPossible: String,
        @Query("yngbgsPosblAt") isYoungPossible: String,
        @Query("pageNo") pageNum: Int,
        @Query("keyword") keyWord: String, // 옵션
        @Query("schSido") siDoCode: String,
        @Query("schSign1") gooGunCode: String,
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int = Const.LOAD_SIZE,
    ) : Response<HomeResponse>

    @GET("getVltrPartcptnItem")
    suspend fun getVolunteerDetail(
        @Query("progrmRegistNo") pID: String,
        @Query("serviceKey") serviceKey: String,
    ) : Response<DetailResponse>
}

interface VolunteerRepository {
    fun getHomeList(
        pageNum: Int = 1,
        keyWord: String = "",
        sDate: String = "",
        eDate: String = "",
        siDoCode: String = "",
        gooGunCode: String = "",
        isAdultPossible: String = Const.ALL,
        isYoungPossible: String = Const.ALL,
        serviceKey: String = Const.SERVICE_KEY,
    ): Flow<PagingData<VolunteerInfo>>
    suspend fun getBookMarkList(): Flow<PagingData<VolunteerInfo>>
    fun addBookMark(pID: String)
    fun deleteBookMark(pID: String)
    fun getVolunteerDetail(
        pID: String,
        serviceKey: String = Const.SERVICE_KEY,
    ): Volunteer
}

class VolunteerRepositoryImpl @Inject constructor(
    private val api: VolunteerApi,
    private val volunteerInfoDao: VolunteerInfoDao,
    private val volunteerDao: VolunteerDao,
) : VolunteerRepository {

    override fun getHomeList(
        pageNum: Int,
        keyWord: String,
        sDate: String,
        eDate: String,
        siDoCode: String,
        gooGunCode: String,
        isAdultPossible: String,
        isYoungPossible: String,
        serviceKey: String
    ): Flow<PagingData<VolunteerInfo>> {
        return Pager(
            config = PagingConfig(pageSize = Const.LOAD_SIZE, enablePlaceholders = false),
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
                serviceKey
            ) },
        ).flow
    }

    override suspend fun getBookMarkList(): Flow<PagingData<VolunteerInfo>> {
        TODO("Not yet implemented")
    }

    override fun addBookMark(pID: String) {
        TODO("Not yet implemented")
    }

    override fun deleteBookMark(pID: String) {
        TODO("Not yet implemented")
    }

    override fun getVolunteerDetail(pID: String, serviceKey: String): Volunteer {
        TODO("Not yet implemented")
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
    val item: List<VolunteerRes>
)

@Xml(name= "item")
data class VolunteerInfoRes(
    @PropertyElement(name="progrmRegistNo") var pID: String,
    @PropertyElement(name="progrmSj") var title: String,
    @PropertyElement(name="nanmmbyNm") var host: String,
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
    @PropertyElement(name="url") var url: String,
) {
    fun toVolunteerInfo() = this.run {
        VolunteerInfo(
            pID = pID,
            title= title,
            host = host,
            sDate = sDate,
            eDate = eDate,
            state = state,
            siDoCode = siDoCode,
            gooGunCode = gooGunCode,
            isAdultPossible = adultPossible,
            isYoungPossible = youngPossible,
            field = field,
            place = place,
            sHour = sHour,
            eHour = eHour,
            nsDate = nsDate,
            neDate = neDate,
            url = url
        )
    }
}

@Xml(name= "item")
data class VolunteerRes(
    @PropertyElement(name="progrmRegistNo") var pID: String,
    @PropertyElement(name="progrmSj") var title: String,
    @PropertyElement(name="nanmmbyNm") var host: String,
    @PropertyElement(name="progrmBgnde") var sDate: String,
    @PropertyElement(name="progrmEndde") var eDate: String,
    @PropertyElement(name="progrmSttusSe") var state: Int,
    @PropertyElement(name="sidoCd") var siDoCode: String,
    @PropertyElement(name="gugunCd") var gooGunCode: String,
    @PropertyElement(name="adultPosblAt") var adultPossible: String,
    @PropertyElement(name="yngbgsPosblAt") var youngPossible: String,
    @PropertyElement(name="grpPosblAt") val groupPossible: String,
    @PropertyElement(name="srvcClCode") var field: String,
    @PropertyElement(name="actPlace") var place: String,
    @PropertyElement(name="actBeginTm") var sHour: Int,
    @PropertyElement(name="actEndTm") var eHour: Int,
    @PropertyElement(name="noticeBgnde") var nsDate: String,
    @PropertyElement(name="noticeEndde") var neDate: String,
    @PropertyElement(name="rcritNmpr") val num: Int,
    @PropertyElement(name="nanmmbyNmAdmn") val manager: String,
    @PropertyElement(name="telno") val tel: String,
    @PropertyElement(name="progrmCn") val contents: String,
) {
    fun toVolunteer() = this.run {
        Volunteer(
            pID = pID,
            title= title,
            host = host,
            sDate = sDate,
            eDate = eDate,
            state = state,
            siDoCode = siDoCode,
            gooGunCode = gooGunCode,
            isAdultPossible = adultPossible,
            isYoungPossible = youngPossible,
            isGroupPossible = groupPossible,
            field = field,
            place = place,
            sHour = sHour,
            eHour = eHour,
            nsDate = nsDate,
            neDate = neDate,
            num = num,
            manager = manager,
            tel = tel,
            contents = contents
        )
    }
}

@Entity(tableName = "volunteer_info")
data class VolunteerInfo(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("progrmRegistNo") val pID: String,
    @SerializedName("progrmSj") val title: String,
    @SerializedName("nanmmbyNm") val host: String,
    @SerializedName("progrmBgnde") val sDate: String,
    @SerializedName("progrmEndde") val eDate: String,
    @SerializedName("progrmSttusSe") val state: Int,
    @SerializedName("sidoCd") val siDoCode: String,
    @SerializedName("gugunCd") val gooGunCode: String,
    @SerializedName("adultPosblAt") val isAdultPossible: String, // 성인 가능 여부
    @SerializedName("yngbgsPosblAt") val isYoungPossible: String, // 청소년 가능 여부
    @SerializedName("srvcClCode") val field: String, // 분야
    @SerializedName("actPlace") val place: String,
    @SerializedName("actBeginTm") val sHour: Int,
    @SerializedName("actEndTm") val eHour: Int,
    @SerializedName("noticeBgnde") val nsDate: String, // 모집 시작일
    @SerializedName("noticeEndde") val neDate: String, // 모집 종료일
    @SerializedName("url") val url: String,
) {
    fun isBookMark(bookMarkList: List<BookMark>) = bookMarkList.any { it.pID == pID }
}

@Entity(tableName = "volunteer")
data class Volunteer(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("progrmRegistNo") val pID: String,
    @SerializedName("progrmSj") val title: String,
    @SerializedName("nanmmbyNm") val host: String,
    @SerializedName("progrmBgnde") val sDate: String,
    @SerializedName("progrmEndde") val eDate: String,
    @SerializedName("progrmSttusSe") val state: Int,
    @SerializedName("sidoCd") val siDoCode: String,
    @SerializedName("gugunCd") val gooGunCode: String,
    @SerializedName("adultPosblAt") val isAdultPossible: String, // 성인 가능 여부
    @SerializedName("yngbgsPosblAt") val isYoungPossible: String, // 청소년 가능 여부
    @SerializedName("grpPosblAt") val isGroupPossible: String, // 단체 가능 여부
    @SerializedName("srvcClCode") val field: String, // 분야
    @SerializedName("actPlace") val place: String,
    @SerializedName("actBeginTm") val sHour: Int,
    @SerializedName("actEndTm") val eHour: Int,
    @SerializedName("noticeBgnde") val nsDate: String, // 모집 시작일
    @SerializedName("noticeEndde") val neDate: String, // 모집 종료일
    @SerializedName("rcritNmpr") val num: Int,
    @SerializedName("nanmmbyNmAdmn") val manager: String,
    @SerializedName("telno") val tel: String,
    @SerializedName("progrmCn") val contents: String,
) {
    fun isBookMark(bookMarkList: List<BookMark>) = bookMarkList.any { it.pID == pID }
//    fun isDoingPossible(): {}
}

@Entity(tableName = "book_mark")
data class BookMark(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("pID") val pID: String,
    @SerializedName("url") val url: String,
)
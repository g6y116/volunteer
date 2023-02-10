package g6y116.volunteer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import org.apache.commons.text.StringEscapeUtils

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
    @ColumnInfo(name = "p_id") val pID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "s_date") val sDate: String,
    @ColumnInfo(name = "e_date") val eDate: String,
    @ColumnInfo(name = "state") val state: Int,
    @ColumnInfo(name = "url") val url: String,
//    @Ignore var isBookMark: Boolean = false
) {
    fun isBookMark(items: List<VolunteerInfo>): Boolean = items.any { it.pID == this.pID }
}

data class Volunteer(
    val pID: String,
    val title: String,
    val area: String,
    val host: String,
    val sDate: String,
    val eDate: String,
    val state: Int,
    val siDoCode: String,
    val gooGunCode: String,
    val isAdultPossible: String, // 성인 가능 여부
    val isYoungPossible: String, // 청소년 가능 여부
    val field: String, // 분야
    val place: String,
    val sHour: Int,
    val eHour: Int,
    val nsDate: String, // 모집 시작일
    val neDate: String, // 모집 종료일
    val num: Int,
    val manager: String,
    val tel: String,
    val email: String,
    val contents: String,
    val address: String,
) {
    fun isBookMark(items: List<VolunteerInfo>?): Boolean {
        return if (items.isNullOrEmpty()) false else items.any { it.pID == this.pID }
    }
}
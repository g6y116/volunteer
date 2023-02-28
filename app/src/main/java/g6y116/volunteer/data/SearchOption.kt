package g6y116.volunteer.data

import g6y116.volunteer.Const

data class SearchOption(
    val siDoCode: String?,
    val gooGunCode: String?,
    val sDate: String?,
    val eDate: String?,
    val isAdult: String?,
    val isYoung: String?,
    val keyWord: String?,
    val state: String?,
) {
    companion object {
        fun reset() = SearchOption(
            siDoCode = null,
            gooGunCode = null,
            sDate = null,
            eDate = null,
            isAdult = Const.TYPE.NO_MATTER,
            isYoung = Const.TYPE.NO_MATTER,
            keyWord = null,
            state = Const.STATE.ALL,
        )
    }
}
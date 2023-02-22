package g6y116.volunteer.data

import g6y116.volunteer.Const

data class RecentSearch(
    val siDoCode: String?,
    val gooGunCode: String?,
    val sDate: String?,
    val eDate: String?,
    val isAdultPossible: String?,
    val isYoungPossible: String?,
    val keyWord: String?,
) {
    companion object {
        fun reset() = RecentSearch(
            siDoCode = null,
            gooGunCode = null,
            sDate = null,
            eDate = null,
            isAdultPossible = Const.TYPE.NO_MATTER,
            isYoungPossible = Const.TYPE.NO_MATTER,
            keyWord = null,
        )
    }
}
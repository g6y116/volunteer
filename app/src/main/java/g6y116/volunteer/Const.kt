package g6y116.volunteer

interface Const {
    companion object {
        const val SERVER_URL = "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/"
        const val SERVICE_KEY = "t0LTS5OH1ThRLqQH7cBgaUInxWKKhLhjOQVmrph7P3ZlCaHnOXZ4VXRVDzN%2BXg4hG%2FdtS4V6Xj49ubxjfCRSHA%3D%3D"
        const val DATABASE_NAME = "volunteer_db"
        const val DATASTORE_NAME = "volunteer_db"

        const val BOTH = "전체"
        const val ADULT = "성인"
        const val YOUNG = "청소년"

        const val TRUE = "Y"
        const val FALSE = "N"
        const val ALL = ""

        const val TODO_NUM = 1
        const val DOING_NUM = 2
        const val DONE_NUM = 3

        const val TODO_TEXT = "모집 예정"
        const val DOING_TEXT = "모집 중"
        const val DONE_TEXT = "모집 완료"

        const val INIT_PAGE_NUM = 1
        const val LOAD_SIZE = 40

        const val HOME = "home"
        const val BOOK_MARK = "bookMark"
        const val MORE = "more"

        const val RECENT_SEARCH = "recentSearch"
        const val MODE = "mode"

        const val SYSTEM_MODE = 0
        const val LIGHT_MODE = 1
        const val DARK_MODE = 2
    }
}
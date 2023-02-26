package g6y116.volunteer

interface Const {
    companion object {
        const val SERVER_URL = "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/"
        const val SERVICE_KEY = "t0LTS5OH1ThRLqQH7cBgaUInxWKKhLhjOQVmrph7P3ZlCaHnOXZ4VXRVDzN%2BXg4hG%2FdtS4V6Xj49ubxjfCRSHA%3D%3D"
        const val DATABASE_NAME = "volunteer_db"
        const val DATASTORE_NAME = "volunteer_db"

        const val INIT_PAGE_NUM = 1
        const val LOAD_SIZE = 40

        const val HOME = "home"
        const val BOOK_MARK = "bookMark"
    }

    interface TYPE {
        companion object {
            const val BOTH = "성인/청소년"
            const val ADULT = "성인"
            const val YOUNG = "청소년"

            const val TRUE = "Y"
            const val FALSE = "N"
            val NO_MATTER = null
        }
    }

    interface STATE {
        companion object {
            const val TODO_NUM = 1
            const val DOING_NUM = 2
            const val DONE_NUM = 3
        }
    }

    interface MODE {
        companion object {
            const val SYSTEM_MODE = "system_mode"
            const val LIGHT_MODE = "light_mode"
            const val DARK_MODE = "dark_mode"
        }
    }

    interface LOCALE {
        companion object {
            const val KO = "korean"
            const val EN = "english"
        }
    }

    interface PrefKey {
        companion object {
            const val MODE = "mode"
            const val LOCALE = "locale"
            const val RECENT_SEARCH = "recentSearch"
            const val SI_DO_CODE = "siDoCode"
            const val GOO_GUN_CODE = "gooGunCode"
            const val S_DATE = "sDate"
            const val E_DATE = "eDate"
            const val IS_ADULT_POSSIBLE = "isAdultPossible"
            const val IS_YOUNG_POSSIBLE = "isYoungPossible"
            const val KEY_WORD = "keyWord"
        }
    }
}
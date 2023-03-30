package g6y116.volunteer.base.consts

interface Const {
    companion object {
        const val OPEN_API_URL = "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/"
        const val OPEN_API_KEY = "t0LTS5OH1ThRLqQH7cBgaUInxWKKhLhjOQVmrph7P3ZlCaHnOXZ4VXRVDzN%2BXg4hG%2FdtS4V6Xj49ubxjfCRSHA%3D%3D"

        const val KAKAO_URL = "http://dapi.kakao.com/"
        const val KAKAO_KEY = "KakaoAK 957fc54318653a602a8d4ace8453d111"

        const val DATABASE_NAME = "volunteer_db"
        const val DATASTORE_NAME = "volunteer_db"

        const val LOAD_SIZE = 100
    }

    interface TYPE {
        companion object {
            const val TRUE = "Y"
            const val FALSE = "N"
            val NO_MATTER = null
        }
    }

    interface STATE {
        companion object {
            const val ALL = "ALL"
            const val TODO = "TODO"
            const val DOING = "DOING"
            const val DONE = "DONE"
        }
    }

    interface THEME {
        companion object {
            const val SYSTEM = "SYSTEM"
            const val LIGHT = "LIGHT"
            const val DARK = "DARK"
        }
    }

    interface LANGUAGE {
        companion object {
            const val KOREAN = "ko-rKR"
            const val ENGLISH = "en-US"
        }
    }

    interface VISIT {
        companion object {
            const val VISIBLE = "VISIBLE"
            const val INVISIBLE = "INVISIBLE"
        }
    }

    interface PrefKey {
        companion object {
            const val SIDO = "SIDO"
            const val GOOGUN = "GOOGUN"
            const val START_DATE = "START_DATE"
            const val END_DATE = "END_DATE"
            const val ADULT = "ADULT"
            const val YOUNG = "YOUNG"
            const val STATE = "STATE"
            const val KEY_WORD = "keyWord"

            const val THEME = "THEME"
            const val LANGUAGE = "LANGUAGE"
            const val VISIT = "VISIT"
        }
    }
}
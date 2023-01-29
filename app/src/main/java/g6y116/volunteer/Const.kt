package g6y116.volunteer

interface Const {

    companion object {
        const val SERVER_URL = "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/"
        const val DB_NAME = "volunteer_db"

        const val TRUE = "Y"
        const val FALSE = "N"
    }

    interface STATUS {
        companion object {
            const val TODO = 1
            const val DOING = 2
            const val DONE = 3
        }
    }
}
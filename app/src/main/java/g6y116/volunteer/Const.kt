package g6y116.volunteer

interface Const {
    companion object {
        const val SERVER_URL = "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/"
        const val SERVICE_KEY = "t0LTS5OH1ThRLqQH7cBgaUInxWKKhLhjOQVmrph7P3ZlCaHnOXZ4VXRVDzN%2BXg4hG%2FdtS4V6Xj49ubxjfCRSHA%3D%3D"
        const val DATABASE_NAME = "volunteer_db"
        const val DATASTORE_NAME = "volunteer_db"

        const val TRUE = "Y"
        const val FALSE = "N"
        const val ALL = ""

        const val TODO = 1
        const val DOING = 2
        const val DONE = 3

        const val LOAD_SIZE = 20
    }
}
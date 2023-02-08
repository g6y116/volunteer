package g6y116.volunteer.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import g6y116.volunteer.repository.HomeResponse
import g6y116.volunteer.repository.VolunteerApi
import g6y116.volunteer.repository.VolunteerInfo
import java.io.IOException

class HomePagingSource(
    private val api: VolunteerApi,
    private val pageNum: Int,
    private val siDoCode: String,
    private val gooGunCode: String,
    private val sDate: String,
    private val eDate: String,
    private val keyWord: String,
    private val isAdultPossible: String,
    private val isYoungPossible: String,
    private val serviceKey: String
) : PagingSource<Int, VolunteerInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VolunteerInfo> {
        val page = params.key ?: pageNum

        return try {
            val response = api.getVolunteerList(
                pageNum = page,
                siDoCode = siDoCode,
                gooGunCode = gooGunCode,
                sDate = sDate,
                eDate = eDate,
                keyWord = keyWord,
                isAdultPossible = isAdultPossible,
                isYoungPossible = isYoungPossible,
                serviceKey = serviceKey,
            )

            val totalCount = (response.body() as HomeResponse).body.totalCount
            val repos = (response.body() as HomeResponse).body.items.item.map { it.toVolunteerInfo() }
            Log.d("성준", "paging $page, $totalCount")
            val nextKey = if (repos.isEmpty()) { null } else { page + 1 }
            LoadResult.Page(
                data = repos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VolunteerInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
package g6y116.volunteer.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import g6y116.volunteer.Const
import g6y116.volunteer.api.VolunteerApi
import g6y116.volunteer.data.HomeResponse
import g6y116.volunteer.data.RecentSearch
import g6y116.volunteer.data.VolunteerInfo
import java.io.IOException

class HomePagingSource(
    private val api: VolunteerApi,
    private val recentSearch: RecentSearch,
) : PagingSource<Int, VolunteerInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VolunteerInfo> {
        val page = params.key ?: Const.INIT_PAGE_NUM

        return try {
            val response = api.getVolunteerList(
                pageNum = page,
                siDoCode = recentSearch.siDoCode,
                gooGunCode = recentSearch.gooGunCode,
                sDate = recentSearch.sDate,
                eDate = recentSearch.eDate,
                keyWord = recentSearch.keyWord,
                isAdultPossible = recentSearch.isAdultPossible,
                isYoungPossible = recentSearch.isYoungPossible,
            )

            val totalCount = (response.body() as HomeResponse).body.totalCount
            val repos = (response.body() as HomeResponse).body.items.item?.map { it.toVolunteerInfo() } ?: emptyList()
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
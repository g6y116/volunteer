package g6y116.volunteer.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import g6y116.volunteer.Const
import g6y116.volunteer.api.VolunteerApi
import g6y116.volunteer.data.HomeResponse
import g6y116.volunteer.data.Info
import g6y116.volunteer.data.SearchOption
import java.io.IOException

class HomePagingSource(
    private val api: VolunteerApi,
    private val searchOption: SearchOption,
) : PagingSource<Int, Info>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Info> {
        val page = params.key ?: Const.INITIAL_PAGE_NUM

        return try {
            val response = api.getInfoList(
                pageNum = page,
                siDoCode = searchOption.siDoCode,
                gooGunCode = searchOption.gooGunCode,
                sDate = searchOption.sDate,
                eDate = searchOption.eDate,
                keyWord = searchOption.keyWord,
                isAdult = searchOption.isAdult,
                isYoung = searchOption.isYoung,
            )

//            val totalCount = (response.body() as HomeResponse).body.totalCount
            val repos = (response.body() as HomeResponse).body.items.item?.map { it.toInfo() } ?: emptyList()
            val nextKey = if (repos.isEmpty()) { null } else { page + 1 }

            LoadResult.Page(
                data = when(searchOption.state) {
                    Const.STATE.DOING -> repos.filter { it.state == Const.STATE.DOING }
                    Const.STATE.DONE -> repos.filter { it.state == Const.STATE.DONE }
                    else -> repos
                },
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Info>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
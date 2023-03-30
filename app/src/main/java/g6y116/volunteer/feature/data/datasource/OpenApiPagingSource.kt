package g6y116.volunteer.feature.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import g6y116.volunteer.base.consts.Const
import g6y116.volunteer.feature.data.model.HomeResponse
import g6y116.volunteer.feature.data.model.Info
import g6y116.volunteer.feature.data.model.SearchOption
import java.io.IOException

class OpenApiPagingSource(
    private val openApiRemoteSource: OpenApiRemoteSource,
    private val searchOption: SearchOption,
) : PagingSource<Int, Info>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Info> {
        val page = params.key ?: 1

        return try {
            val response = openApiRemoteSource.loadInfoList(
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
            val items = (response.body() as HomeResponse).body.items.item?.map { it.toInfo() } ?: emptyList()

            LoadResult.Page(
                data = when(searchOption.state) {
                    Const.STATE.DOING -> items.filter { it.state == Const.STATE.DOING }
                    Const.STATE.DONE -> items.filter { it.state == Const.STATE.DONE }
                    else -> items
                },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
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
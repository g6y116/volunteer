package g6y116.volunteer.feature.data.repository

import g6y116.volunteer.feature.data.datasource.InfoLocalSource
import g6y116.volunteer.feature.data.datasource.KakaoRemoteSource
import g6y116.volunteer.feature.data.model.Coordinate
import g6y116.volunteer.feature.data.model.Info
import javax.inject.Inject

interface DetailRepository {
    suspend fun loadCoordinate(address: String): Coordinate?
    suspend fun insertBookmark(info: Info)
    suspend fun deleteBookmark(pID: String)
}

class DetailRepositoryImpl @Inject constructor(
    private val kakaoRemoteSource: KakaoRemoteSource,
    private val infoLocalSource: InfoLocalSource,
) : DetailRepository {

    override suspend fun loadCoordinate(address: String): Coordinate? =
        (kakaoRemoteSource.loadAddress(address).body())?.documents?.get(0)?.run { Coordinate(x, y) }

    override suspend fun insertBookmark(info: Info) = infoLocalSource.insert(info)

    override suspend fun deleteBookmark(pID: String) = infoLocalSource.delete(pID)
}
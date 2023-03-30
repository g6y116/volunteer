package g6y116.volunteer.base.di

import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g6y116.volunteer.feature.data.repository.MainRepository
import g6y116.volunteer.feature.data.repository.MainRepositoryImpl
import g6y116.volunteer.feature.data.repository.DetailRepository
import g6y116.volunteer.feature.data.repository.DetailRepositoryImpl

@Module @InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton @Binds
    abstract fun bindMainRepository(
        repositoryImpl: MainRepositoryImpl
    ): MainRepository

    @Singleton @Binds
    abstract fun bindDetailRepository(
        repositoryImpl: DetailRepositoryImpl
    ): DetailRepository
}
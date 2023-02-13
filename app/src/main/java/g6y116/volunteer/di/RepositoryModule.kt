package g6y116.volunteer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g6y116.volunteer.repository.VolunteerRepository
import g6y116.volunteer.repository.VolunteerRepositoryImpl
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton @Binds
    abstract fun bindVolunteerRepository(
        repositoryImpl: VolunteerRepositoryImpl
    ): VolunteerRepository
}
package g6y116.volunteer.base.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g6y116.volunteer.base.AppDatabase
import g6y116.volunteer.base.consts.Const
import g6y116.volunteer.feature.data.datasource.KakaoRemoteSource
import g6y116.volunteer.feature.data.datasource.OpenApiRemoteSource
import g6y116.volunteer.feature.data.datasource.InfoLocalSource
import g6y116.volunteer.feature.data.datasource.VisitLocalSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier annotation class OpenApi
    @Qualifier annotation class Kakao

    @Singleton @Provides
    fun provideInfoLocalSource(database: AppDatabase): InfoLocalSource =
        database.getInfoLocalSource()

    @Singleton @Provides
    fun provideVisitLocalSource(database: AppDatabase): VisitLocalSource =
        database.getVisitLocalSource()

    @Singleton @Provides
    fun provideOpenApiRemoteSource(@OpenApi retrofit: Retrofit): OpenApiRemoteSource =
        retrofit.create(OpenApiRemoteSource::class.java)

    @Singleton @Provides
    fun provideKakaoRemoteSource(@Kakao retrofit: Retrofit): KakaoRemoteSource =
        retrofit.create(KakaoRemoteSource::class.java)

    @Singleton @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase =
         Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, Const.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton @Provides @OpenApi
    fun provideOpenApiRetrofit(): Retrofit {
        val xmlParser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        return Retrofit
            .Builder()
            .addConverterFactory(TikXmlConverterFactory.create(xmlParser))
            .baseUrl(Const.OPEN_API_URL)
            .build()
    }

    @Singleton @Provides
    fun provideKakaoInterceptor (): Interceptor =
        Interceptor { it.proceed(it.request().newBuilder().addHeader("Authorization", Const.KAKAO_KEY).build()) }

    @Singleton @Provides
    fun provideKakaoOKHttpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(provideKakaoInterceptor()).build()

    @Singleton @Provides @Kakao
    fun provideKakaoRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(Const.KAKAO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(produceFile = { context.preferencesDataStoreFile(Const.DATASTORE_NAME) })
}
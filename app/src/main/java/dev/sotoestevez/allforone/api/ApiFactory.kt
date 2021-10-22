package dev.sotoestevez.allforone.api

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dev.sotoestevez.allforone.BuildConfig
import dev.sotoestevez.allforone.api.services.AuthService
import dev.sotoestevez.allforone.api.services.FeedService
import dev.sotoestevez.allforone.api.services.TaskService
import dev.sotoestevez.allforone.api.services.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Factory of the different services of the API */
object ApiFactory {

    private var service: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_IP)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** @return  Service of the /auth endpoints */
    fun getAuthService(): AuthService = service.create(AuthService::class.java)

    /** @return  Service of the /feed endpoints */
    fun getFeedService(): FeedService = service.create(FeedService::class.java)

    /** @return  Service of the /tasks endpoints */
    fun getTaskService(): TaskService = service.create(TaskService::class.java)

    /** @return  Service of the /user endpoints */
    fun getUserService(): UserService = service.create(UserService::class.java)

}

package lpu.semesterseven.project.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// server
private val SERVER_PORT = "8000"
private val SERVER_URL  = "https://well-bass-above.ngrok-free.app/"

private var retrofit    : Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(SERVER_URL)
    .build()

interface ServerAPIService {
    @GET("pulse")
    fun getResult(): Call<String>
}

interface SendImageService {
    @Multipart
    @POST("upload")
    fun uploadImage(@Part image: MultipartBody.Part): Call<String>
}

object NetworkObjects {
    val diseaseAPIService   : ServerAPIService = retrofit.create(ServerAPIService::class.java)
    val sendImageService    : SendImageService = retrofit.create(SendImageService::class.java)
}
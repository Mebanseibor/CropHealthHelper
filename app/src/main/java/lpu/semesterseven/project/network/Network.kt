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
// private val SERVER_URL  = "http://127.0.0.1:$SERVER_PORT/"
private val SERVER_URL  = "https://lexicostatistic-ectosarcous-renita.ngrok-free.dev/"

interface ServerAPIService {
    @GET("pulse")
    fun getResult(): Call<String>
}

interface SendImageService {
    @Multipart
    @POST("upload")
    fun uploadImage(@Part image: MultipartBody.Part): Call<String>
}

interface EmergencyInfoService {
    @GET("info")
    fun fetchEmergencyInfo(): Call<String>
}

object NetworkObjects {
    val retrofit    : Retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(SERVER_URL)
        .build()

    val diseaseAPIService       : ServerAPIService      = retrofit.create(ServerAPIService::class.java)
    val sendImageService        : SendImageService      = retrofit.create(SendImageService::class.java)
    val emergencyInfoService    : EmergencyInfoService  = retrofit.create(EmergencyInfoService::class.java)
}
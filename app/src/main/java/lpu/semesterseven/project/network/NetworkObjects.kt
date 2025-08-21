package lpu.semesterseven.project.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

// server
private val SERVER_PORT = "8000"
private val SERVER_URL  = "https://well-bass-above.ngrok-free.app/"

private var retrofit    : Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(SERVER_URL)
    .build()

interface ServerAPIService {
    @GET("objects")
    fun getResult(): Call<String>
}

object NetworkObjects {
    val diseaseAPIService   : ServerAPIService = retrofit.create(ServerAPIService::class.java)
}
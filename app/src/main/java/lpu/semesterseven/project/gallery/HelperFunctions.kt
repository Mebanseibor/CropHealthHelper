package lpu.semesterseven.project.gallery

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getRequestBodyFromUri(context: Context, uri: Uri): RequestBody?{
    val contentResolver : ContentResolver   = context.contentResolver
    val mimeType        : String            = contentResolver.getType(uri)?:"image/*"
    return contentResolver.openInputStream(uri)?.use{ inputStream ->
        val bytes       : ByteArray         = inputStream.readBytes()
        RequestBody.create(mimeType.toMediaTypeOrNull(), bytes)
    }
}

fun createMultiPartBody(context: Context, uri: Uri): MultipartBody.Part? {
    val requestBody     : RequestBody?          = getRequestBodyFromUri(context, uri)
    val fileName        : String                = getFileNameFromUri(context, uri)
    val multiPartBody   : MultipartBody.Part?   = requestBody?.let{
        MultipartBody.Part.createFormData("image", fileName, it)
    }
    return multiPartBody
}

fun getResponseFromUploadingImage(context: Context, call: Call<String>, result: (String)->Unit){
    result("[]")
    call.enqueue(object: Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            Log.d("Response", "Response was successful")
            if(response.body()==null){
                Toast.makeText(context, "Connection was established, but server didn't response properly", Toast.LENGTH_SHORT).show()
                return
            }
            Toast.makeText(context, "Image Processed", Toast.LENGTH_SHORT).show()
            result(response.body()!!)
            return
        }
        override fun onFailure(call: Call<String>, t: Throwable) {
            Toast.makeText(context, "Response was unsuccessful", Toast.LENGTH_SHORT).show()
        }
    })
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName = "unknown"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
    }
    return fileName
}

fun parseJsonToDataClass(jsonString: String): List<ProcessedImageJSON> {
    return Json.decodeFromString(jsonString)
}
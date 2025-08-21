package lpu.semesterseven.project.gallery

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
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
    val fileName        : String                = "image"
    val fileFullName    : String                = "$fileName.jpg"
    val multiPartBody   : MultipartBody.Part?   = requestBody?.let{
        MultipartBody.Part.createFormData(fileName, fileFullName, it)
    }
    return multiPartBody
}

fun getResponseFromUploadingImage(context: Context, call: Call<String>) {
    call.enqueue(object: Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            Toast.makeText(context, "Response was successful", Toast.LENGTH_SHORT).show()
        }
        override fun onFailure(call: Call<String>, t: Throwable) {
            Toast.makeText(context, "Response was unsuccessful", Toast.LENGTH_SHORT).show()
        }
    })
}
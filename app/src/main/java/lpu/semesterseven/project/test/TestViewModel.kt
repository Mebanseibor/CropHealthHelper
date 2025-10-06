package lpu.semesterseven.project.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestViewModel: ViewModel() {
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    suspend fun fetch(){
        withContext(Dispatchers.IO){
            delay(1000)
            val url = "https://picsum.photos/200/300"
            _imageUrl.postValue(url)
        }
    }

    fun startLiveImages(){
        viewModelScope.launch{
            while(true){
                withContext(Dispatchers.IO){
                    val url = "https://picsum.photos/200/300"
                    _imageUrl.postValue(url)
                }
                delay(399)
            }
        }
    }
}
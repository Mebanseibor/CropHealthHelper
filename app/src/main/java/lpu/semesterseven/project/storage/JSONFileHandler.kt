package lpu.semesterseven.project.storage

import android.content.Context
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_WARNING
import lpu.semesterseven.project.dialogs.promptAcknowledgement
import java.io.File

open class JSONFileHandler(private var context: Context, protected var fileName: String) {
    protected open lateinit var file: File
    protected open lateinit var dir: File

    init{
        // file = get
    }

    protected open fun save(data: String): Boolean {
        if(!dirExist()){
            promptAcknowledgement(context, "Cannot save the file", "Directory doesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
            return false
        }
        return true
    }

    protected open fun open(){

    }

    private fun dirExist(): Boolean = dir.exists()
}
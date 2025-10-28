package lpu.semesterseven.project.storage

import android.content.Context
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_NEUTRAL
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_WARNING
import lpu.semesterseven.project.dialogs.promptAcknowledgement
import java.io.File

abstract class JSONFileHandler(private var context: Context) {
    protected open lateinit var fileName: String
    protected open var fileExtension    : String    = "json"
    protected open var dir              : File      = File("${context.filesDir.absolutePath}")
    protected open var file             : File      = createFile()

    constructor(context: Context, fileName: String): this(context){
        this.fileName = fileName
    }

    open fun write(): Boolean {
        if(!dirExist()){
            if(!createDirectory()){
                promptAcknowledgement(context, "Cannot write the file", "Directory\n${dir.absolutePath}\ndoesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
                return false
            }
        }
        promptAcknowledgement(context, "Info", "${dir.exists()}\n${file.exists()}", ACKNOWLEDGEMENT_STATUS_NEUTRAL)
        if(!fileExist()){
            promptAcknowledgement(context, "Cannot write the file", "File\n${file.absolutePath}\ndoesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
            return false
        }
        file.writeText(loadPayload())
        return true
    }

    open fun read(): ByteArray?{
        if(!dirExist()){
            promptAcknowledgement(context, "Cannot open the file", "Directory doesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
            return null
        }
        if(!fileExist()){
            promptAcknowledgement(context, "Cannot open the file", "File doesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
            return null
        }
        return file.readBytes()
    }

    open fun delete(): Boolean{
        if(!dirExist()){
            promptAcknowledgement(context, "Cannot delete the file", "Directory doesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
            return false
        }

        if(!fileExist()){
            promptAcknowledgement(context, "Cannot delete the file", "File doesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
            return false
        }
        file.delete()
        return true
    }

    abstract fun loadPayload(): String

    protected fun dirExist(): Boolean     = dir.exists()
    protected fun fileExist(): Boolean    = file.exists()

    protected fun changeDirectoryFromRoot(newDirectory: String){
        dir = File("${context.filesDir.absolutePath}/${newDirectory}")
    }

    protected fun createDirectory() : Boolean   = dir.mkdirs()
    protected fun createFile()      : File      = File(dir, fullFileName())

    protected fun fullFileName(): String = "$fileName.$fileExtension"
}
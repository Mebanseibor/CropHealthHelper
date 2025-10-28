package lpu.semesterseven.project.storage

import android.content.Context
import android.util.Log
import kotlinx.serialization.json.Json
import lpu.semesterseven.project.R
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_WARNING
import lpu.semesterseven.project.dialogs.promptAcknowledgement
import lpu.semesterseven.project.gallery.AnalysedImageJSON
import lpu.semesterseven.project.gallery.Image

class AnalysedImageJSONFileHandler (private var context: Context, override var fileName: String): JSONFileHandler(context) {
    lateinit var image: Image

    init{
        changeDirectoryFromRoot(context.resources.getString(R.string.dir_images_analysis))
        this.fileName = fileName
        this.file = createFile()
    }

    constructor(context: Context, fileName: String, image: Image): this(context, fileName){
        this.image = image
    }

    override fun write(): Boolean{
        if(!dirExist()){
            if(!createDirectory()){
                promptAcknowledgement(context, "Cannot write the file", "Directory doesn't exist", ACKNOWLEDGEMENT_STATUS_WARNING)
                Log.d("Debugging", "${dir.absolutePath}\n${file.absolutePath}")
                return false
            }
        }
        return super.write()
    }

    override fun loadPayload(): String {
        var formattedImage = AnalysedImageJSON(
            this.image.imageDrawable,
            this.image.image.toString(),
            this.image.score,
            this.image.advice,
            this.image.is_healthy
        )

        return Json.encodeToString(formattedImage)
    }
}
package lpu.semesterseven.project.gallery

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import kotlinx.serialization.Serializable
import lpu.semesterseven.project.R

class Image(var title: String){
    var imageDrawable   : Int       = R.drawable.baseline_question_mark_24
    var image           : Bitmap?   = null
    var score           : Float     = 0F
    var advice          : String    = "Default Advice"
    var is_healthy      : Boolean   = false

    constructor(title: String, image: Bitmap): this(title){
        this.image  = image
    }

    constructor(title: String, image: Bitmap, score: Float): this(title, image){
        this.score  = score
    }

    constructor(title: String, image: Bitmap, score: Float, advice: String): this(title, image, score){
        this.advice = advice
    }

    constructor(title: String, image: Bitmap, score: Float, advice: String, is_healthy: Boolean): this(title, image, score, advice){
        this.is_healthy = is_healthy
    }

    constructor(title: String, score: Float): this(title){
        this.score  = score
    }
}

@Serializable
data class ProcessedImageJSON(
    var label: String,
    var score: Float,
    var advice: String,
    var is_healthy: Boolean,
)

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 1
    val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 1
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
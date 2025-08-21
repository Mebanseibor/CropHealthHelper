package lpu.semesterseven.project.gallery

import kotlinx.serialization.Serializable
import lpu.semesterseven.project.R

class Image(var title: String){
    var image: Int      = R.drawable.baseline_question_mark_24
    var score: Float    = 0F

    constructor(title: String, image: Int): this(title){
        this.image  = image
    }

    constructor(title: String, image: Int, score: Float): this(title, image){
        this.score  = score
    }

    constructor(title: String, score: Float): this(title){
        this.score  = score
    }
}

@Serializable
data class ProcessedImageJSON(
    var label: String,
    var score: Float,
)
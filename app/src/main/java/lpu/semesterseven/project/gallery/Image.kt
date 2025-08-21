package lpu.semesterseven.project.gallery

import lpu.semesterseven.project.R

class Image(var title: String){
    var image: Int  = R.drawable.baseline_question_mark_24

    constructor(title: String, image: Int): this(title){
        this.image  = image
    }
}
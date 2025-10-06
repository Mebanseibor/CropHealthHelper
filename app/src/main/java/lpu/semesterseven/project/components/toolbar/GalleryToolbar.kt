package lpu.semesterseven.project.components.toolbar

import android.content.Context
import android.util.AttributeSet

class GalleryToolBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
    ): MainToolBar(context, attrs, defStyleAttr){
    override fun init(): Boolean{
        super.init()
        title = "Gallery"
        return true
    }
}
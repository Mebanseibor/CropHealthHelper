package lpu.semesterseven.project.components.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R

class CreateNotesToolBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.toolbarStyle
    ): MainToolBar(context, attrs, defStyleAttr){
    override fun init(): Boolean{
        super.init()
        title = "Create Notes"
        return true
    }
}
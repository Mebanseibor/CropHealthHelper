package lpu.semesterseven.project.components.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R

class SettingsToolBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.toolbarStyle
    ): MainToolBar(context, attrs, defStyleAttr){
    override fun init(): Boolean{
        super.init()
        title = "Settings"
        return true
    }
}
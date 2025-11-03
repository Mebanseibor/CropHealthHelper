package lpu.semesterseven.project.components.toolbar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import lpu.semesterseven.project.R
import lpu.semesterseven.project.ratingbar.RatingBarActivity
import lpu.semesterseven.project.settings.SettingsActivity

open class MainToolBar @JvmOverloads constructor(
        private var context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
    ): Toolbar(context, attrs, defStyleAttr){
    open fun init(): Boolean{
        setNavigationIcon(R.drawable.baseline_arrow_back_24)
        title       = resources.getString(R.string.app_name)

        setColor()
        initNavigationIcon()

        return true
    }

    private fun setColor(){
        val color = resources.getColor(R.color.white)
        setTitleTextColor(color)
        setSubtitleTextColor(color)

        navigationIcon?.setTint(color)

        setBackgroundColor(resources.getColor(R.color.primary))
        overflowIcon?.setTint(color)

        for (i in 0..menu.size()-1) menu.getItem(i).setIconTintList(ContextCompat.getColorStateList(context, R.color.white))
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean{
        var selectedItem = when(item.itemId){
            R.id.menu_item_settings     ->{
                context.startActivity(Intent(context, SettingsActivity::class.java))
                true
            }
            R.id.menu_item_ratingbar    -> {
                context.startActivity(Intent(context, RatingBarActivity::class.java))
                true
            }
            else -> {
                Toast.makeText(context, "Unrecognized menu item", Toast.LENGTH_SHORT).show()
                false
            }
        }
        return selectedItem
    }

    fun disableNavigationIcon(){
        setNavigationIcon(null)
    }

    private fun initNavigationIcon(){
        setNavigationOnClickListener {
            (context as Activity)?.onBackPressed()
        }
    }
}
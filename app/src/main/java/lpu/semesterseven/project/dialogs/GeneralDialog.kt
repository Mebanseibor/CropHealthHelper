package lpu.semesterseven.project.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import lpu.semesterseven.project.R

const val ACKNOWLEDGEMENT_STATUS_NEUTRAL = 0
const val ACKNOWLEDGEMENT_STATUS_SUCCESS = 1
const val ACKNOWLEDGEMENT_STATUS_FAILURE = 2
const val ACKNOWLEDGEMENT_STATUS_WARNING = 3

fun promptAcknowledgement(context: Context, title: String, subtitle: String, status: Int=ACKNOWLEDGEMENT_STATUS_NEUTRAL){
    var dialog = Dialog(context)
    val view = LayoutInflater.from(context).inflate(R.layout.prompt_acknowledgement, null, false)
    dialog.setContentView(view)

    val titleV      : TextView  = view.findViewById(R.id.title)
    val subtitleV   : TextView  = view.findViewById(R.id.subtitle)
    val symbolV     : ImageView = view.findViewById(R.id.symbol)
    val cardView    : CardView  = view.findViewById(R.id.cardView)
    val btn         : Button    = view.findViewById(R.id.btn)

    titleV.text     = title
    subtitleV.text  = subtitle

    var cardBgColor = R.color.status_info
    val image = when(status){
        ACKNOWLEDGEMENT_STATUS_SUCCESS -> {
            cardBgColor = R.color.status_success
            R.drawable.baseline_check_circle_24
        }
        ACKNOWLEDGEMENT_STATUS_FAILURE -> {
            cardBgColor = R.color.status_error
            R.drawable.baseline_cancel_24
        }
        ACKNOWLEDGEMENT_STATUS_WARNING -> {
            cardBgColor = R.color.status_warning
            R.drawable.baseline_warning_amber_24
        }
        else -> {
            symbolV.visibility = View.GONE
            R.drawable.baseline_question_mark_24
        }
    }
    symbolV.setImageResource(image)
    cardView.setBackgroundColor(context.resources.getColor(cardBgColor))

    btn.setOnClickListener { dialog.dismiss() }

    dialog.show()
}

fun promptPositiveNegative(context: Context, title: String, subtitle: String, result: (Boolean)->Unit){
    var dialog = Dialog(context)
    var view = LayoutInflater.from(context).inflate(R.layout.prompt_positivenegative, null, false)
    dialog.setContentView(view)

    val titleV      : TextView  = view.findViewById(R.id.title)
    val subtitleV   : TextView  = view.findViewById(R.id.subtitle)
    val symbolV     : ImageView = view.findViewById(R.id.symbol)
    val cardView    : CardView  = view.findViewById(R.id.cardView)
    val btnNegative : Button    = view.findViewById(R.id.btnNegative)
    val btnPositive : Button    = view.findViewById(R.id.btnPositive)

    titleV.text     = title
    subtitleV.text  = subtitle

    dialog.setOnDismissListener     { result(false) }
    btnNegative.setOnClickListener  {
        result(false)
        dialog.dismiss()
    }
    btnPositive.setOnClickListener  {
        result(true)
        dialog.dismiss()
    }

    dialog.show()
}
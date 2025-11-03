package lpu.semesterseven.project.ratingbar

import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import lpu.semesterseven.project.R

class RatingBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ratingbar)

        val sharedPreferences = getSharedPreferences("RatingBar", MODE_PRIVATE)
        val key = "rating"

        val ratingBar: RatingBar = findViewById(R.id.ratingBar)
        val ratingBarText: TextView = findViewById(R.id.ratingBarText)
        val btnSubmit: Button = findViewById(R.id.btnSubmit)

        // Load saved rating if exists
        val savedRating = sharedPreferences.getFloat(key, 0f)
        ratingBar.rating = savedRating
        ratingBarText.text = "${savedRating.toInt()}"

        // Update TextView when rating changes
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingBarText.text = "${rating.toInt()}/5"
        }

        // Save rating on button click
        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating
            val editor = sharedPreferences.edit()
            editor.putFloat(key, rating)
            editor.apply()
            finish()
        }
    }
}

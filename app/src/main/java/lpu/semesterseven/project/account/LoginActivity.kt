package lpu.semesterseven.project.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import lpu.semesterseven.project.MainActivity
import lpu.semesterseven.project.R
import lpu.semesterseven.project.dialogs.promptAcknowledgement

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val signUpText = findViewById<TextView>(R.id.signUpRedirect)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)

        loginBtn.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPass = password.text.toString().trim()

            if (userEmail.isEmpty() || userPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Welcome $userEmail!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
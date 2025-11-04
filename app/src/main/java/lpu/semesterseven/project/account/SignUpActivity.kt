package lpu.semesterseven.project.account

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import lpu.semesterseven.project.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val username = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val registerBtn = findViewById<Button>(R.id.registerButton)
        val loginRedirect = findViewById<TextView>(R.id.loginRedirect)

        registerBtn.setOnClickListener {
            if (username.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
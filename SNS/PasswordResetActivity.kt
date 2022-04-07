package org.techtown.trip_app.SNS

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password_reset.*
import org.techtown.trip_app.R

class PasswordResetActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        mAuth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.emailEditText)


        sendButton.setOnClickListener {
            if (email.length() > 0) {
                val loaderLayout = findViewById<RelativeLayout>(R.id.loaderLayout)
                loaderLayout.visibility = View.VISIBLE

                mAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        Toast.makeText(this, "email을 보냈습니다.", Toast.LENGTH_SHORT).show()
                        loaderLayout.visibility = View.GONE

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                                baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        loaderLayout.visibility = View.GONE
                        email?.setText("")
                    }

                }
            } else {
                Toast.makeText(this, "email을 입력하세요. ", Toast.LENGTH_SHORT).show()
            }
        }

        backbutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
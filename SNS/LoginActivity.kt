package org.techtown.trip_app.SNS

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.signupButton
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.techtown.trip_app.Map.MapActivity
import org.techtown.trip_app.R

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()


        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)

        loginButton.setOnClickListener {
            if(email.length() > 0 && password.length() > 0) {
                if (password.text.toString() != password.text.toString()) {
                    Toast.makeText(this, "password가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val loaderLayout = findViewById<RelativeLayout>(R.id.loaderLayout)
                    loaderLayout.visibility = View.VISIBLE

                    mAuth.signInWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                //Log.d("dddd", "createUserWithEmail:success")
                                val user = mAuth.currentUser
                                Toast.makeText(this, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                                loaderLayout.visibility = View.GONE

                                val intent = Intent(this, SNSMainActivity::class.java)
                                startActivity(intent)

                            } else {
                                //Log.w("dddd", "실패", task.exception)
                                Toast.makeText(
                                    baseContext, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                                loaderLayout.visibility = View.GONE
                                email?.setText("")
                                password?.setText("")

                            }
                        }
                }

            }else {
                Toast.makeText(this, "email 또는 password를 입력하세요. ", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        gotoPasswordResetButton.setOnClickListener {
            val intent = Intent(this, PasswordResetActivity::class.java)
            startActivity(intent)
        }

        backButton2.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

    }

}
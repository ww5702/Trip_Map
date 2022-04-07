package org.techtown.trip_app.SNS

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.techtown.trip_app.R

class SignUpActivity : AppCompatActivity() {
    //private var mAuth: FirebaseAuth? = null
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        val user = Firebase.auth.currentUser


        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val passwordcheck = findViewById<EditText>(R.id.passwordCheckEditText)



        signupButton.setOnClickListener {
            if (passwordcheck.text.toString() != password.text.toString()) {
                Toast.makeText(this, "password가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val loaderLayout = findViewById<RelativeLayout>(R.id.loaderLayout)
                loaderLayout.visibility = View.VISIBLE

                mAuth.createUserWithEmailAndPassword(
                    email.text.toString(),
                    password.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //Log.d("dddd", "createUserWithEmail:success")
                            Toast.makeText(this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                            loaderLayout.visibility = View.GONE

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)

                        } else {
                            //Log.w("dddd", "실패", task.exception)
                            Toast.makeText(
                                baseContext, "이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                            loaderLayout.visibility = View.GONE
                            email?.setText("")
                            password?.setText("")
                            passwordcheck?.setText("")

                        }
                    }
            }
        }

        backButton.setOnClickListener {
            finish()
        }

    }
}
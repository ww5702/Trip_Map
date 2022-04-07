package org.techtown.trip_app.SNS

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_member_info.*
import kotlinx.android.synthetic.main.activity_password_reset.*
import kotlinx.android.synthetic.main.activity_password_reset.sendButton
import kotlinx.android.synthetic.main.activity_photo.*
import org.techtown.trip_app.R

class MemberinitActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore


    private val OPEN_GALLERY = 1

    private fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == OPEN_GALLERY) {
                var currentImageUrl: Uri? = data?.data
                Log.d("uripath", currentImageUrl.toString())

                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    profileview.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Log.d("ActivityResult", "something wrong")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_info)


        val name = findViewById<EditText>(R.id.nameEditText)
        val phone = findViewById<EditText>(R.id.phoneEditText)
        val birth = findViewById<EditText>(R.id.birthEditText)

        val docData = hashMapOf(
                "name" to name.text.toString(),
                "phone" to phone.text.toString(),
                "birth" to birth.text.toString()
        )

        mAuth = FirebaseAuth.getInstance()

        Log.d("확인0", "ㅇㅇㅇㅇ")

        sendButton.setOnClickListener {
            if (name.length() > 0 && phone.length() > 9 && birth.length() > 5) {

                val loaderLayout = findViewById<RelativeLayout>(R.id.loaderLayout)
                loaderLayout.visibility = View.VISIBLE


                //Memberinfo 생성자 만드는게 문제임;
                val Memberinfo = Memberinfo(name, phone, birth)
                Log.d("확인1", Memberinfo.toString())
                Log.d("확인2", user.uid)
                //db.collection("users").document(user.uid).set(Memberinfo)
                db.collection("users").document(user.uid).set(docData)
                        .addOnSuccessListener {

                            Toast.makeText(this, "회원정보 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                            loaderLayout.visibility = View.GONE

                            val intent = Intent(this, SNSMainActivity::class.java)
                            startActivity(intent)

                        }
                        .addOnFailureListener {
                            Log.d("실패", "실패")
                            Toast.makeText(this, "회원정보 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            loaderLayout.visibility = View.GONE

                            name?.setText("")
                            phone?.setText("")
                            birth?.setText("")
                        }
            } else {
                Toast.makeText(this, "name 혹은 phonnumber 혹은 birth를 입력하세요. ", Toast.LENGTH_SHORT).show()
            }

        }

        profileButton.setOnClickListener {
            openGallery()
        }



    }
}
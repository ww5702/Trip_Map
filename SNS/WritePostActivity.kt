package org.techtown.trip_app.SNS

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_member_info.*
import kotlinx.android.synthetic.main.activity_write_post.*
import org.techtown.trip_app.R

class WritePostActivity : AppCompatActivity(){
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    var photo : String = "photo"

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
                    imageView.setImageBitmap(bitmap)
                    photo = currentImageUrl.toString()

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
        setContentView(R.layout.activity_write_post)


        val title = findViewById<EditText>(R.id.titleEditText)
        val content = findViewById<EditText>(R.id.contentEditText)

        val data = hashMapOf(
                "Title" to title.text.toString(),
                "Content" to content.text.toString(),
                "id" to user.uid,
                "photourl" to photo
        )


        checkButton.setOnClickListener {
            Log.d("불러오기", title.toString())
            Log.d("불러오기", content.toString())

            val WriteInfo = WriteInfo(title, content, user.uid)

            if (title.length() > 0 && content.length() > 0) {
                val loaderLayout = findViewById<RelativeLayout>(R.id.loaderLayout)
                loaderLayout.visibility = View.VISIBLE

                db.collection("posts").add(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "게시물 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                            loaderLayout.visibility = View.GONE

                            val intent = Intent(this, SNSMainActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Log.d("실패", "실패")
                            Toast.makeText(this, "게시물 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            loaderLayout.visibility = View.GONE
                        }
            } else {
                Toast.makeText(this, "title 혹은 content를 입력하세요. ", Toast.LENGTH_SHORT).show()
            }

        }

        image.setOnClickListener {
            openGallery()
        }

        video.setOnClickListener {
            Toast.makeText(this, "개발중", Toast.LENGTH_SHORT).show()
        }

    }
}
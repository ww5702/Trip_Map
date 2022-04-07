package org.techtown.trip_app.SNS

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_snsmain.*
import org.techtown.trip_app.R

class SNSMainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snsmain)

        /*
        val arrayList = ArrayList<String>()
        arrayList.add("테스트1")
        arrayList.add("테스트2")
        arrayList.add("테스트3")
        arrayList.add("테스트4")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
         */

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 앱 종료
            startActivity(intent)
        }

        userprofile.setOnClickListener {
            val intent = Intent(this, MemberinitActivity::class.java)
            startActivity(intent)
        }

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, WritePostActivity::class.java)
            startActivity(intent)
        }
    }
}
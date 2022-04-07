package org.techtown.trip_app.Recommend

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_recommend.*
import org.techtown.trip_app.Map.MapActivity
import org.techtown.trip_app.Map.RecordActivity
import org.techtown.trip_app.Map.checkMemo
import org.techtown.trip_app.R
import org.techtown.trip_app.SNS.LoginActivity
import java.io.InputStream

class RecommendActivity : AppCompatActivity() {


    var recommendList = arrayListOf<Recommend>(
            //Recommend("1233", 1.1, 2.2, 3.3, 4.4)
    )
    var recommendList_change = arrayListOf<Recommend>(
            //Recommend("1233", 1.1, 2.2, 3.3, 4.4)
    )

    var checklist = arrayListOf<Double>()

    //메뉴판
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId) {
            R.id.mic -> {
                val intent = Intent(this, RecordActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "저장된 위치에 음성 녹음", Toast.LENGTH_SHORT).show()
            }
            R.id.note -> {
                Toast.makeText(this, "지금까지 저장된 메모를 확인해봅니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, checkMemo::class.java)
                startActivity(intent)
            }
            R.id.sns -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "SNS로 이동", Toast.LENGTH_SHORT).show()
            }
            R.id.rating_star -> {
                val intent = Intent(this, RatingActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "평점 매기기", Toast.LENGTH_SHORT).show()
            }

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        //평점 매긴거 가져온 부분
        var putActivity = intent.getStringExtra("putActivity")
        var putFood = intent.getStringExtra("putFood")
        var putShopping = intent.getStringExtra("putShopping")
        var putHistory = intent.getStringExtra("putHistory")





        Log.d("test1", putActivity.toString())
        Log.d("test2", putFood.toString())
        Log.d("test3", putShopping.toString())
        Log.d("test4", putHistory.toString())

        myrating.setText("당신의 중요도를 지정해주세요!")

        val assetManager: AssetManager = resources.assets
        val inputStream: InputStream = assetManager.open("retrip.txt")

        inputStream.bufferedReader().readLines().forEach {
            var token = it.split("\t")
            //Log.d("file_test", token.toString())
            var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
            recommendList.add(input)
        }

        reload.setOnClickListener {
            if (putActivity == "null" || putFood == "null" || putShopping == "null" || putHistory == "null") {
                myrating.setText("우측 상단의 메뉴판에서 평점을 매겨주세요!")
            } else {
                myrating.setText("Activity = $putActivity, Food =$putFood, Shopping =$putShopping, History =$putHistory")
            }
        }

        val recommendAdapter = RecommendAdapter(this, recommendList)
        rv_recommend.adapter = recommendAdapter



        searcharea.setOnClickListener {
            val inputStream_change: InputStream = assetManager.open("retrip.txt")

            if (putActivity == "null" || putFood == "null" || putShopping == "null" || putHistory == "null") {
                Toast.makeText(this, "먼저 평점을 지정해주세요.", Toast.LENGTH_SHORT).show()
            } else {


                if (putActivity != null && putFood != null && putHistory != null && putShopping != null) {

                    //Ac ->
                    if ((putActivity.toDouble() >= putFood.toDouble()) && (putActivity.toDouble() >= putShopping.toDouble()) && (putActivity.toDouble() >= putHistory.toDouble())) {
                        //Ac -> Fo
                        if ((putFood.toDouble() >= putShopping.toDouble()) && (putFood.toDouble() >= putHistory.toDouble())) {
                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[1].toDouble() > token[2].toDouble()) && (token[2].toDouble() > token[3].toDouble()) && (token[3].toDouble() > token[4].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Ac -> Sh
                        else if ((putShopping.toDouble() >= putFood.toDouble()) && (putShopping.toDouble() >= putHistory.toDouble())) {

                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[1].toDouble() > token[3].toDouble()) && (token[3].toDouble() > token[2].toDouble()) && (token[2].toDouble() > token[4].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Ac -> Hi
                        else if ((putHistory.toDouble() >= putFood.toDouble()) && (putHistory.toDouble() >= putShopping.toDouble())) {

                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[1].toDouble() > token[4].toDouble()) && (token[4].toDouble() > token[2].toDouble()) && (token[2].toDouble() > token[3].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }

                    }
                    //Fo->
                    else if ((putFood.toDouble() >= putActivity.toDouble()) && (putFood.toDouble() >= putShopping.toDouble()) && (putFood.toDouble() >= putHistory.toDouble())) {
                        //Fo -> Ac
                        if ((putActivity.toDouble() >= putShopping.toDouble()) && (putActivity.toDouble() >= putHistory.toDouble())) {
                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[2].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[3].toDouble()) && (token[3].toDouble() > token[4].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Fo -> Sh
                        else if ((putShopping.toDouble() >= putActivity.toDouble()) && (putShopping.toDouble() >= putHistory.toDouble())) {

                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[2].toDouble() > token[3].toDouble()) && (token[3].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[4].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Fo -> Hi
                        else if ((putHistory.toDouble() >= putActivity.toDouble()) && (putHistory.toDouble() >= putShopping.toDouble())) {

                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[2].toDouble() > token[4].toDouble()) && (token[4].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[3].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }


                    }
                    //Sh
                    else if ((putShopping.toDouble() >= putActivity.toDouble()) && (putShopping.toDouble() >= putFood.toDouble()) && (putShopping.toDouble() >= putHistory.toDouble())) {
                        //Sh -> Ac
                        if ((putActivity.toDouble() >= putFood.toDouble()) && (putActivity.toDouble() >= putHistory.toDouble())) {
                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[3].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[2].toDouble()) && (token[2].toDouble() > token[4].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Sh -> Fo
                        else if ((putFood.toDouble() >= putActivity.toDouble()) && (putFood.toDouble() >= putHistory.toDouble())) {

                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[3].toDouble() > token[2].toDouble()) && (token[2].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[4].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Sh -> Hi
                        else if ((putHistory.toDouble() >= putActivity.toDouble()) && (putHistory.toDouble() >= putFood.toDouble())) {

                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[3].toDouble() > token[4].toDouble()) && (token[4].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[2].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }


                    }
                    //Hi
                    else if ((putHistory.toDouble() >= putActivity.toDouble()) && (putHistory.toDouble() >= putFood.toDouble()) && (putHistory.toDouble() >= putShopping.toDouble())) {
                        //Hi -> Ac
                        if ((putActivity.toDouble() >= putFood.toDouble()) && (putActivity.toDouble() >= putShopping.toDouble())) {
                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[4].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[2].toDouble()) && (token[2].toDouble() > token[3].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Hi -> Fo
                        else if ((putFood.toDouble() >= putActivity.toDouble()) && (putFood.toDouble() >= putShopping.toDouble())) {
                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[4].toDouble() > token[2].toDouble()) && (token[2].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[3].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }
                        //Hi -> Sh
                        else if ((putShopping.toDouble() >= putActivity.toDouble()) && (putShopping.toDouble() >= putFood.toDouble())) {
                            inputStream_change.bufferedReader().readLines().forEach {
                                var token = it.split("\t")
                                //Log.d("file_test", token.toString())
                                if ((token[4].toDouble() > token[3].toDouble()) && (token[3].toDouble() > token[1].toDouble()) && (token[1].toDouble() > token[2].toDouble())) {
                                    var input = Recommend(token[0], token[1].toDouble(), token[2].toDouble(), token[3].toDouble(), token[4].toDouble())
                                    recommendList_change.add(input)
                                }
                            }
                        }

                    }
                }

                Log.d("test0", checklist.toString())


                var recommendList2 = recommendList.reversed()

                val recommendAdapter_change = RecommendAdapter(this, recommendList_change)
                rv_recommend.adapter = recommendAdapter_change

            }
        }



        returnmap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            finish()

            Toast.makeText(this, "지도로 돌아갑니다!", Toast.LENGTH_SHORT).show()

        }

    }

}
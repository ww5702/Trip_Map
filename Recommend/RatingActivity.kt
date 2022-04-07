package org.techtown.trip_app.Recommend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_rating.*
import org.techtown.trip_app.Map.MapActivity
import org.techtown.trip_app.R

class RatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        Activity_ratingBar.setOnRatingBarChangeListener{ ratingar, rating, fromUser ->
            ActivityText.text ="${rating}점"
        }
        Food_ratingBar.setOnRatingBarChangeListener{ ratingar, rating, fromUser ->
            FoodText.text ="${rating}점"
        }
        Shopping_ratingBar.setOnRatingBarChangeListener{ ratingar, rating, fromUser ->
            ShoppingText.text ="${rating}점"
        }
        History_ratingBar.setOnRatingBarChangeListener{ ratingar, rating, fromUser ->
            HistoryText.text ="${rating}점"
        }
        /*
        var putActivityText = "${Activity_ratingBar.rating}"
        var putFoodText = "${Food_ratingBar.rating}"
        var putShoppingText = "${Shopping_ratingBar.rating}"
        var putHistoryText = "${History_ratingBar.rating}"*/

        applyButton.setOnClickListener {
            val intent = Intent(this, RecommendActivity::class.java)

            val putActivity = Activity_ratingBar.rating
            val putFood = Food_ratingBar.rating
            val putShopping = Shopping_ratingBar.rating
            val putHistory = History_ratingBar.rating

            intent.putExtra("putActivity",putActivity.toString())
            intent.putExtra("putFood",putFood.toString())
            intent.putExtra("putShopping",putShopping.toString())
            intent.putExtra("putHistory",putHistory.toString())

            Toast.makeText(this, "Rating 점수를 기록합니다.", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Activity = $putActivity, Food =$putFood, Shopping =$putShopping, History =$putHistory", Toast.LENGTH_SHORT).show()

            startActivity(intent)
        }
    }
}
package org.techtown.trip_app.Recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.techtown.trip_app.R

class RecommendAdapter (val context: Context, val recommendList: ArrayList<Recommend>) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_listview, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val txtArea: TextView = view.findViewById(R.id.tv_rv_area)
        val txtActivity: TextView = view.findViewById(R.id.tv_rv_activity)
        val txtFood: TextView = view.findViewById(R.id.tv_rv_food)
        val txtShopping: TextView = view.findViewById(R.id.tv_rv_shopping)
        val txtHistory: TextView = view.findViewById(R.id.tv_rv_history)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val recommend = recommendList[position]
        txtArea.text = recommend.area
        txtActivity.text = recommend.activity.toString()
        txtFood.text = recommend.food.toString()
        txtShopping.text = recommend.shopping.toString()
        txtHistory.text = recommend.history.toString()

        return view
    }

    override fun getItem(position: Int): Any {
        return recommendList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return recommendList.size
    }
}
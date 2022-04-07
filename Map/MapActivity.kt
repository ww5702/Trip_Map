package org.techtown.trip_app.Map

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_map.*
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.techtown.trip_app.R
import org.techtown.trip_app.Recommend.RatingActivity
import org.techtown.trip_app.Recommend.RecommendActivity
import org.techtown.trip_app.SNS.LoginActivity
import org.w3c.dom.Text

class MapActivity : AppCompatActivity() {


    private lateinit var mapView: MapView              // 카카오 지도 뷰
    private val eventListener = MarkerEventListener(this)
    private var isFabOpen = false
    val db = FirebaseFirestore.getInstance()

    var pro: ProgressDialog? = null

    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun toggleFab() {
        //Toast.makeText(this, "메인 플로팅 버튼 클릭 : $isFabOpen", Toast.LENGTH_SHORT).show()

        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (isFabOpen) {
            ObjectAnimator.ofFloat(floatingJeju, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(floatingGangwon, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(floatingKR, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(floatingsetting, "translationY", 0f).apply { start() }
            floatingMain.setImageResource(R.drawable.airplane_black)

            // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션 세팅
        } else {
            ObjectAnimator.ofFloat(floatingJeju, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(floatingGangwon, "translationY", -400f).apply { start() }
            ObjectAnimator.ofFloat(floatingKR, "translationY", -600f).apply { start() }
            ObjectAnimator.ofFloat(floatingsetting, "translationY", -800f).apply { start() }
            floatingMain.setImageResource(R.drawable.ic_baseline_clear)
        }

        isFabOpen = !isFabOpen

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val putlatitude = intent.getStringExtra("latitude")
        val putlongitude = intent.getStringExtra("longitude")
        val photoTitle = intent.getStringExtra("photoTitle")

        when(item?.itemId) {
            R.id.mic -> {
                /*Log.d("check1", putlatitude.toString())
                Log.d("check1", putlongitude.toString())*/

                val intent = Intent(this, RecordActivity::class.java)
                startActivity(intent)
                intent.putExtra("photoTitle", photoTitle.toString())

                Toast.makeText(this, "원하는 순간의 음성 녹음", Toast.LENGTH_SHORT).show()
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
            R.id.checkmic -> {
                val intent = Intent(this, RecordCheckActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "지금까지 저장된 녹음을 확인해봅니다.", Toast.LENGTH_SHORT).show()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapViewContainer = map_view
        val mapView = MapView(this)
        val lm: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location =
                lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
        val uLatitude = userNowLocation.latitude
        val uLongitude = userNowLocation.longitude
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)

        //사진 넣을때 필요했던 정보들 가져오는 부분
        //val latitude = "37.291904"
        //val longitude = "127.203683"
        val photoTitle = intent.getStringExtra("photoTitle")
        val photoContent = intent.getStringExtra("photoContent")
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")
        //Log.d("test0",photoTitle.toString())
        //Log.d("test0",photoContent.toString())

        //평점 매긴거 가져온 부분
        val putActivity = intent.getStringExtra("putActivity")
        val putFood = intent.getStringExtra("putFood")
        val putShopping = intent.getStringExtra("putShopping")
        val putHistory = intent.getStringExtra("putHistory")

        Log.d("test1",putActivity.toString())
        Log.d("test2",putFood.toString())
        Log.d("test3",putShopping.toString())
        Log.d("test4",putHistory.toString())


        val note = hashMapOf(
                "Title" to photoTitle.toString(),
                "Content" to photoContent.toString(),
                "Latitude" to latitude.toString(),
                "Longitude" to longitude.toString()
        )

        mapView.setMapCenterPointAndZoomLevel(uNowPosition,2, true)
        mapViewContainer.addView(mapView)

        mapView.setPOIItemEventListener(eventListener)
        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))

        //시작했을때 저장되어있는 db 출력하여 지도에 핀 꼽기
        db.collection("Notes")
                .get()
                .addOnSuccessListener { result ->
                    for(document in result) {
                        val setTitle = document["Title"]
                        val setLatitude = document["Latitude"].toString()
                        val setLongitude = document["Longitude"].toString()


                        /*
                        Log.d("test0",setTitle.toString())
                        Log.d("test",setLatitude.toString())
                        Log.d("test",setLongitude.toString())*/

                        val marker = MapPOIItem()
                        marker.apply {
                            marker.itemName = setTitle.toString()
                            mapPoint = MapPoint.mapPointWithGeoCoord(setLatitude!!.toDouble(), setLongitude!!.toDouble())
                            marker.tag = 0
                            marker.mapPoint = mapPoint
                            // 기본으로 제공하는 BluePin 마커 모양.
                            marker.markerType = MapPOIItem.MarkerType.BluePin
                            // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                            mapView.addPOIItem(marker)
                        }
                    }

                }


        inputphoto.setOnClickListener {
            val newNowPosition = MapPoint.mapPointWithGeoCoord(latitude!!.toDouble(), longitude!!.toDouble())

            //val latitude = "37.291904"
            //val longitude = "127.203683"


            Log.d("testt",latitude.toString())
            Log.d("testt",longitude.toString())

            if (latitude != null && longitude != null) {
                val marker = MapPOIItem()
                marker.apply {
                    marker.itemName = photoTitle.toString()
                    mapPoint = MapPoint.mapPointWithGeoCoord(latitude!!.toDouble(), longitude!!.toDouble())
                    marker.tag = 0
                    marker.mapPoint = mapPoint
                    // 기본으로 제공하는 BluePin 마커 모양.
                    marker.markerType = MapPOIItem.MarkerType.BluePin
                    // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                    marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    mapView.addPOIItem(marker)
                }

                db.collection("Notes").document(latitude+","+longitude)
                        .set(note)
                        .addOnSuccessListener {
                            Toast.makeText(this,"노트가 추가되었습니다.",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            Toast.makeText(this,"실패.",Toast.LENGTH_SHORT).show()
                        }

                Toast.makeText(this, "위치 지정 성공", Toast.LENGTH_SHORT).show()
                mapView.setMapCenterPoint(newNowPosition, true)



            } else {
                Toast.makeText(this, "먼저 사진을 결정해주십시오.", Toast.LENGTH_SHORT).show()
            }
        }


        map_page_location_btn.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                try {
                    val now_marker = MapPOIItem()
                    now_marker.itemName = "현위치"
                    now_marker.tag = 0


                    now_marker.mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
                    now_marker.markerType = MapPOIItem.MarkerType.YellowPin
                    mapView.addPOIItem(now_marker)

                    mapView.setMapCenterPointAndZoomLevel(uNowPosition, 2,true)
                } catch (e: NullPointerException) {
                    Log.e("LOCATION_ERROR", e.toString())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.finishAffinity(this)
                    } else {
                        ActivityCompat.finishAffinity(this)
                    }

                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                    System.exit(0)
                }
            } else {
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                        this,
                        REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE
                )
            }
        }


        floatingMain.setOnClickListener{
            toggleFab()
        }
        floatingJeju.setOnClickListener{
            val newNowPosition = MapPoint.mapPointWithGeoCoord(33.382040, 126.53525235)
            mapView.setMapCenterPoint(newNowPosition, true)
            mapView.setZoomLevel(8,true)
            Toast.makeText(this, "제주도", Toast.LENGTH_SHORT).show()
        }
        floatingGangwon.setOnClickListener{
            val newNowPosition = MapPoint.mapPointWithGeoCoord(37.717599, 128.843654)
            mapView.setMapCenterPoint(newNowPosition, true)
            mapView.setZoomLevel(8,true)
            Toast.makeText(this, "강원도", Toast.LENGTH_SHORT).show()
        }
        floatingKR.setOnClickListener{
            val newNowPosition = MapPoint.mapPointWithGeoCoord(36.709613, 127.506837)
            mapView.setMapCenterPoint(newNowPosition, true)
            mapView.setZoomLevel(12,true)
            Toast.makeText(this, "KOREA", Toast.LENGTH_SHORT).show()
        }
        floatingsetting.setOnClickListener{
            Toast.makeText(this, "개발준비중", Toast.LENGTH_SHORT).show()
        }


        photo.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java)
            startActivity(intent)
        }


        zoomin.setOnClickListener {
            mapView.zoomIn(true)
        }
        zoomout.setOnClickListener {
            mapView.zoomOut(true)
        }
        recomend.setOnClickListener {
            val intent = Intent(this, RecommendActivity::class.java)

            intent.putExtra("putActivity",putActivity.toString())
            intent.putExtra("putFood",putFood.toString())
            intent.putExtra("putShopping",putShopping.toString())
            intent.putExtra("putHistory",putHistory.toString())

            startActivity(intent)
        }
        combination.setOnClickListener{
            val intent = Intent(this, CombinationActivity::class.java)
            startActivity(intent)
        }
        video.setOnClickListener {
            val intent = Intent(this, RecordVideoActivity::class.java)
            startActivity(intent)
        }

    }


    class CustomBalloonAdapter(inflater: LayoutInflater) : CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.balloon_layout, null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            name.text = poiItem?.itemName   // 해당 마커의 정보 이용 가능
            val db = FirebaseFirestore.getInstance()
            db.collection("Notes")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val setTitle = document["Title"]
                            val setContent = document["Content"]
                            if(setTitle == name.text) {
                                address.text = setContent.toString()
                            }
                        }
                    }
            return mCalloutBalloon
        }

        @SuppressLint("SetTextI18n")
        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 클릭 시
            val db = FirebaseFirestore.getInstance()
            db.collection("Notes")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val setTitle = document["Title"]
                            val setLatitude = document["Latitude"]
                            val setLongitude = document["Longitude"]
                            if(setTitle == name.text) {
                                address.text = "위도 :" + setLatitude.toString() + ", 경도 :" + setLongitude.toString()

                            }
                        }
                    }

            return mCalloutBalloon
        }
    }

    // 마커 클릭 이벤트 리스너
    class MarkerEventListener(val context: Context) : MapView.POIItemEventListener {

        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            // 마커 클릭 시
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
            // 말풍선 클릭 시 (Deprecated)
            // 이 함수도 작동하지만 그냥 아래 있는 함수에 작성하자
        }

        override fun onCalloutBalloonOfPOIItemTouched(
                mapView: MapView?,
                poiItem: MapPOIItem?,
                buttonType: MapPOIItem.CalloutBalloonButtonType?
        ) {
            // 말풍선 클릭 시
            val builder = AlertDialog.Builder(context)
            var pro: ProgressDialog? = null
            var getContent = "null"
            var getLatitude = "null"
            var getLongitude = "null"

            builder.setTitle("${poiItem?.itemName}")
            builder.setMessage("확인하고 싶은 정보를 클릭해주세요.")
            builder.setIcon(R.drawable.ic_baseline_airplanemode_active_24)

            val db = FirebaseFirestore.getInstance()
            db.collection("Notes")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val setTitle = document["Title"]
                            if(setTitle == "${poiItem?.itemName}") {
                                getContent = document["Content"] as String
                                getLatitude = document["Latitude"] as String
                                getLongitude = document["Longitude"] as String
                            }
                        }
                    }
            /*
            Log.d("test22",getContent)
            Log.d("test22",getLatitude)
            Log.d("test22",getLongitude)*/


            var listener = object : DialogInterface.OnClickListener {
                /*var getContent = "null"
                var getLatitude = "null"
                var getLongitude = "null"*/

                override fun onClick(p0: DialogInterface?, p1: Int) {

                    when (p1) {
                        DialogInterface.BUTTON_NEUTRAL -> {
                            SampleToast.createToast(context,"메모의 내용 : "+ getContent)?.show()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            SampleToast.createToast(context,"기록의 위도 : "+ getLatitude)?.show()
                        }
                        DialogInterface.BUTTON_POSITIVE -> {
                            SampleToast.createToast(context,"기록의 경도 : "+ getLongitude)?.show()
                        }
                    }
                }
            }
            builder.setPositiveButton("LONGITUDE", listener)
            builder.setNegativeButton("LATITUDE", listener)
            builder.setNeutralButton("MEMO", listener)


            builder.show()
        }

        override fun onDraggablePOIItemMoved(
                mapView: MapView?,
                poiItem: MapPOIItem?,
                mapPoint: MapPoint?
        ) {
            // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
        }
    }

}
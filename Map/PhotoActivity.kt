package org.techtown.trip_app.Map


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
//import androidx.exifinterface.media.ExifInterface
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_photo.*
import org.techtown.trip_app.R
import java.io.IOException
import java.util.jar.Manifest


class PhotoActivity : AppCompatActivity() {
    private var mView: TextView? = null
    private val OPEN_GALLERY = 1
    private var realPath = null

    var isphotoreal = "X"


    fun checkPermission(){
        val permission = android.Manifest.permission.ACCESS_MEDIA_LOCATION
        val permissionResult = ContextCompat.checkSelfPermission(this, permission)
        when(permissionResult){
            PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
// Go Main Function
            }
            PackageManager.PERMISSION_DENIED -> {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, arrayOf(permission), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            100 -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
// Go Main Function
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
// Finish() or Show Guidance on the need for permission
                }
            }
        }
    }


    private fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                var currentImageUrl: Uri? = data?.data

                Log.d("uripath", currentImageUrl.toString())

                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    if (currentImageUrl != null) {
                        getRealPathFromURI(currentImageUrl)
                    }
                    picture.setImageBitmap(bitmap)


                    try {
                        val exif = currentImageUrl?.let { getRealPathFromURI(it)?.let { ExifInterface(it) } }
                        if (exif != null) {
                            showExif(exif)
                            var attrLATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                            var attrLONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)



                            Log.d("check01", exif.getAttribute(ExifInterface.TAG_DATETIME).toString())
                            Log.d("check02", exif.getAttribute(ExifInterface.TAG_FLASH).toString())
                            Log.d("check03", exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE).toString())
                            Log.d("check04", exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).toString())
                            Log.d("check05", exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE).toString())
                            Log.d("check06", exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF).toString())
                            Log.d("check07", exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH).toString())
                            Log.d("check08", exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH).toString())
                            Log.d("check09", exif.getAttribute(ExifInterface.TAG_MAKE).toString())
                            Log.d("check010", exif.getAttribute(ExifInterface.TAG_MODEL).toString())
                            Log.d("check011", exif.getAttribute(ExifInterface.TAG_ORIENTATION).toString())
                            Log.d("check012", exif.getAttribute(ExifInterface.TAG_WHITE_BALANCE).toString())
                            Log.d("check013", attrLATITUDE.toString())
                            Log.d("check014", attrLONGITUDE.toString())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                    }


                    add_picture.setOnClickListener {
                        val exif = currentImageUrl?.let { getRealPathFromURI(it)?.let { ExifInterface(it) } }

                        if (isphotoreal == "O") {
                            if (exif != null) {
                                showExif(exif)
                                var attrLATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                                var attrLATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                                var attrLONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                                var attrLONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
                                val photoTitle = findViewById<EditText>(R.id.editTitle)
                                val photoContent = findViewById<EditText>(R.id.editContent)

                                //gps 체크
                                Log.d("check3", attrLATITUDE.toString())
                                Log.d("check4", attrLONGITUDE.toString())
                                Log.d("check5", attrLATITUDE_REF.toString())
                                Log.d("check6", attrLONGITUDE_REF.toString())

                                val intent = Intent(this, MapActivity::class.java)


                                if (attrLATITUDE_REF.equals("N")) {
                                    val latitude = convertToDegree(attrLATITUDE.toString())
                                    Log.d("check5", latitude.toString())
                                    intent.putExtra("latitude", latitude.toString())
                                } else {
                                    val latitude = 0 - convertToDegree(attrLATITUDE.toString())
                                    Log.d("check5", latitude.toString())
                                    intent.putExtra("latitude", latitude.toString())
                                }

                                if (attrLONGITUDE_REF.equals("E")) {
                                    var longtitude = convertToDegree(attrLONGITUDE.toString())
                                    Log.d("check6", longtitude.toString())
                                    intent.putExtra("longitude", longtitude.toString())
                                } else {
                                    var longtitude = 0 - convertToDegree(attrLONGITUDE.toString())
                                    Log.d("check6", longtitude.toString())
                                    intent.putExtra("longitude", longtitude.toString())
                                }

                                intent.putExtra("photoTitle", photoTitle.text.toString())
                                intent.putExtra("photoContent", photoContent.text.toString())

                                Toast.makeText(this, "사진 넣기를 눌러주세요.", Toast.LENGTH_SHORT).show()
                                startActivity(intent)

                            }
                        } else {
                            Toast.makeText(this, "먼저 사진을 골라주세요.", Toast.LENGTH_SHORT).show()
                        }

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Log.d("ActivityResult", "something wrong")
        }

    }


    fun getRealPathFromURI(contentUri: Uri): String? {
        if (contentUri.path!!.startsWith("/storage")) {
            return contentUri.path
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":".toRegex()).toTypedArray()[1]
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = MediaStore.Files.FileColumns._ID + " = " + id
        val cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), columns, selection, null, null)
        try {
            val columnIndex = cursor!!.getColumnIndex(columns[0])
            if (cursor!!.moveToFirst()) {
                Log.d("realpath", cursor!!.getString(columnIndex))
                return cursor!!.getString(columnIndex)
            }
        } finally {
            cursor!!.close()
        }
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)


        mView = findViewById<View>(R.id.infomation) as TextView

        checkPermission()

        take_picture.setOnClickListener { openGallery() }


        back.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showExif(exif: ExifInterface) {
        var myAttribute = "[사진 정보] \n\n"
        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif)
        //myAttribute += "GPSLatitude : 37.291904\n"
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif)
        //myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif)
        //myAttribute += "GPSLongitude : 127.203683\n"
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif)
        //myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif)
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif)
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif)

        /*
        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif)
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif)
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif)
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif)
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif)*/
        mView!!.text = myAttribute
        isphotoreal = "O"

    }

    private fun getTagString(tag: String, exif: ExifInterface): String {
        return (tag + " : " + exif.getAttribute(tag) + "\n")
    }

    private fun convertToDegree(stringDMS: String): Float {
        var result: Float? = null
        val DMS = stringDMS.split(",".toRegex(), 3)

        val stringD = DMS[0].split("/".toRegex(), 2)
        val D0: Double = stringD[0].toDouble()
        val D1: Double = stringD[1].toDouble()
        val FloatD = D0 / D1

        val stringM = DMS[1].split("/".toRegex(), 2)
        val M0: Double = stringM[0].toDouble()
        val M1: Double = stringM[1].toDouble()
        val FloatM = M0 / M1

        val stringS = DMS[2].split("/".toRegex(), 2)
        val S0: Double = stringS[0].toDouble()
        val S1: Double = stringS[1].toDouble()
        val FloatS = S0 / S1

        result = (FloatD + FloatM / 60 + FloatS / 3600).toFloat()
        return result
    }


}
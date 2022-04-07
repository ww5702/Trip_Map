package org.techtown.trip_app.Map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_recordvideo.*
import org.techtown.trip_app.R
import org.techtown.trip_app.databinding.ActivityRecordvideoBinding
import java.io.File
import java.text.SimpleDateFormat

class RecordVideoActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecordvideoBinding

    private val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    private val CAMERA_PERMISSION_FLAG = 100
    private val STORAGE_PERMISSION = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val STORAGE_PERMISSION_FLAG = 200

    private val REQUEST_VIDEO_CAPTURE_CODE = 1
    private var videoUri : Uri? = null // video 저장될 Uri

    private var isVideoPlaying = false // VideoView에 영상이 재생되고 있는지 상태를 확인


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordvideoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (checkPermission(CAMERA_PERMISSION, CAMERA_PERMISSION_FLAG)) {
            checkPermission(STORAGE_PERMISSION, STORAGE_PERMISSION_FLAG)
        }



        binding.btnPlay.setOnClickListener {
            when(isVideoPlaying){
                true -> {
                    isVideoPlaying = false
                    binding.videoView.stopPlayback()
                    binding.btnPlay.text = "Play"
                    binding.videoView.setVideoURI(videoUri)
                }
                false -> {
                    isVideoPlaying = true
                    binding.btnPlay.text = "Stop"
                    binding.videoView.start()
                }
            }
        }
        binding.videoView.setOnCompletionListener {
            binding.btnPlay.text = "Play"
            isVideoPlaying = false
        }

        binding.btnRecord.setOnClickListener {
            val recordVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val videoFile = File(
                File("${filesDir}/video").apply {
                    if(!this.exists()){
                        this.mkdirs()
                    }
                },
                newVideoFileName()
            )
            videoUri = FileProvider.getUriForFile(
                this,
                "org.techtown.trip_app",
                videoFile
            )
            recordVideoIntent.resolveActivity(packageManager)?.also{
                recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
                startActivityForResult(recordVideoIntent, REQUEST_VIDEO_CAPTURE_CODE)
            }
        }



        backhome.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when(requestCode){
                REQUEST_VIDEO_CAPTURE_CODE -> {
                    binding.videoView.setVideoURI(videoUri)
                    binding.videoView.requestFocus()
                    binding.btnPlay.isEnabled = true
                }
            }
        }
    }

    private fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_FLAG -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해야지만 앱을 사용 할 수 있습니다.", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        checkPermission(STORAGE_PERMISSION, STORAGE_PERMISSION_FLAG)
                    }
                }
            }
            STORAGE_PERMISSION_FLAG -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인해야지만 앱을 사용 할 수 있습니다.", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }
                }
            }
        }
    }


    private fun newVideoFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.mp4"
    }

}
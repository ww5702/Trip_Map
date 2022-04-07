package org.techtown.trip_app.Map

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activitiy_record.*
import kotlinx.android.synthetic.main.activitiy_record.back
import kotlinx.android.synthetic.main.activity_checkmemo.*
import org.techtown.trip_app.R
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RecordActivity : AppCompatActivity() {

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitiy_record)


        //타이틀 이름 가져오기 실패
        //record.setImageResource(R.drawable.record_image)

        //Glide.with(this).load(profile.text.toString()).circleCrop().into(record_image)


        var state = false
        var playstate = false
        var recordingStopped = false
        var current = LocalDateTime.now()
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")
        var dateAndtime = current.format(formatter)

        //val fileName: String = Date().getTime().toString() + ".mp3"
        val fileName: String = dateAndtime.toString() + ".mp3"
        Log.e("file", fileName)
        val output = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + fileName
        //val output = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}" + "/test.mp3"

        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, 0)

        button_start_recording.setOnClickListener {
            mediaRecorder = MediaRecorder()

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(output)

            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()

                state = true

                Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        button_stop_recording.setOnClickListener {
            if (state) {
                mediaRecorder?.stop()
                mediaRecorder?.release()

                state = false

                Toast.makeText(this, "녹음 중지", Toast.LENGTH_SHORT).show()
            }
        }

        button_pause_recording.setOnClickListener {
            if (state) {
                if (!recordingStopped) {
                    Toast.makeText(this, "녹음 정지", Toast.LENGTH_SHORT).show()
                    mediaRecorder?.pause()
                    recordingStopped = true
                    button_pause_recording.text = "다시 시작"
                } else {
                    Toast.makeText(this, "다시 시작", Toast.LENGTH_SHORT).show()
                    mediaRecorder?.resume()
                    recordingStopped = false
                    button_pause_recording.text = "정지"
                }
            }
        }

        back.setOnClickListener {
            finish()
        }

    }
}
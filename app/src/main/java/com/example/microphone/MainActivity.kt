package com.example.microphone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import android.widget.Toast
import android.media.MediaPlayer
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private var recorder: MediaRecorder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }

    private fun initButtons(){
        val file = createFileInDownloads()
        start.setOnClickListener {
            startRecording(file)
        }
        stop.setOnClickListener {
            stopRecording()
        }
        Play.setOnClickListener {
            playAudio(file)
        }
    }

    private fun startRecording(fileName: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val uri = Uri.fromParts("package", packageName, null)
            startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri), 1)
        }
        else {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                try {
                    prepare()
                } catch (e: IOException) {
                    Log.e("a", "prepare() failed")
                }

                start()
            }
        }
    }

    private fun createFileInDownloads(): String {
        return "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/audiorecord.3gp"
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    private fun playAudio(file: String){
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(file)
            mediaPlayer.prepare()
            mediaPlayer.start()
            Toast.makeText(applicationContext, "Playing Audio", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // make something
        }
    }
}

package com.example.microphone

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
//            requestPermissions()
            Log.v("a", file)
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
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
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

    private fun createFileInDownloads(): String {
        return "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
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

package com.example.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    val btnPlay: Button? = null
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mp = MediaPlayer.create(this, R.raw.music437322)
        //mp?.setOnCompletionListener {mp -> btnPlay?.setText("再生")}
    }

    fun btnPlay_onClick(view: View?) {
        if (mp?.isPlaying() == false) {
            mp?.start()
            btnPlay?.setText("停止")
        }
        else {
            mp?.stop()
            mp?.prepare()
            btnPlay?.setText("再生")
        }
        //btnPlay?.setText("再生")
    }
}

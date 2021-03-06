package com.example.myapplication

// import android.R
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import java.util.Random

class MainActivity : AppCompatActivity() {

    private var playBtn: Button? = null
    private var prevBtn: Button? = null
    private var skipBtn: Button? = null
    private var positionBar: SeekBar? = null
    private var volumeBar: SeekBar? = null
    private var elapsedTimeLabel: TextView? = null
    private var remainingTimeLabel: TextView? = null
    private var mp: MediaPlayer? = null
    private var totalTime: Int = 0
    private var playNumber: Int = 0

    //（オプション）Warning解消
    private val handler = Handler(Handler.Callback { msg ->
        val currentPosition = msg.what
        // 再生位置を更新
        positionBar!!.progress = currentPosition

        // 経過時間ラベル更新
        val elapsedTime = createTimeLabel(currentPosition)
        elapsedTimeLabel!!.text = elapsedTime

        // 残り時間ラベル更新
        val remainingTime = "- " + createTimeLabel(totalTime - currentPosition)
        remainingTimeLabel!!.text = remainingTime

        true
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playBtn = findViewById(R.id.playBtn)
        prevBtn = findViewById(R.id.prevBtn)
        skipBtn = findViewById(R.id.skipBtn)
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel)
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel)

        // Media Playerの初期化
        val musicList = mutableListOf(R.raw.music437322, R.raw.music447468, R.raw.music329319, R.raw.music481521)
        val musicListShuffuled : List<Int> = musicList.shuffled(java.util.Random(0))
        for(i in playNumber..musicList.count()) {
            var musicFile: Int = musicListShuffuled[0]
            mp = MediaPlayer.create(this, musicFile)
        }
        mp!!.isLooping = false
        mp!!.seekTo(0)
        mp!!.setVolume(0.5f, 0.5f)
        totalTime = mp!!.duration

        // 再生位置
        positionBar = findViewById(R.id.positionBar)
        positionBar!!.max = totalTime
        positionBar!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp!!.seekTo(progress)
                        positionBar!!.progress = progress
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            }
        )

        // 音量調節
        volumeBar = findViewById(R.id.volumeBar)
        volumeBar!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val volumeNum = progress / 100f
                    mp!!.setVolume(volumeNum, volumeNum)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            }
        )

        // Thread (positionBar・経過時間ラベル・残り時間ラベルを更新する)
        Thread(Runnable {
            // onClick(mButtonSkip)
            // while (!mButtonPlayPause.isEnabled()) {
            while (mp != null) {
                try {
                    val msg = Message()
                    msg.what = mp!!.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }

            }
        }).start()

    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    fun playBtnClick(view: View) {
        if (!mp!!.isPlaying) {
            // 停止中
            mp!!.start()
            playBtn!!.setBackgroundResource(R.drawable.stop)

        } else {
            // 再生中
            mp!!.pause()
            playBtn!!.setBackgroundResource(R.drawable.play)
        }
    }
}
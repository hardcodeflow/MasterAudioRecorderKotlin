package com.hardcodeflow.masteraudiorecorder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.audiovisualization.DbmHandler
import com.cleveroad.audiovisualization.GLAudioVisualizationView
import com.hardcodeflow.masteraudiorecorder.audio.AndroidAudioRecorderSettings
import com.hardcodeflow.masteraudiorecorder.audio.VisualizerHandler
import com.hardcodeflow.masteraudiorecorder.common.Util
import com.hardcodeflow.whitefox.extensions.CustomIntent
import com.hardcodeflow.whitefox.extensions.IntentAnimation
import com.hardcodeflow.whitefox.extensions.toast
import kotlinx.android.synthetic.main.activity_master_audio_recorder.*
import omrecorder.AudioChunk
import omrecorder.OmRecorder
import omrecorder.PullTransport
import omrecorder.Recorder
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MasterAudioRecorderActivity : AppCompatActivity(), PullTransport.OnAudioChunkPulledListener,
    OnCompletionListener {
    private var visualizerView: GLAudioVisualizationView? = null
    private var recorderSettings: AndroidAudioRecorderSettings =AndroidAudioRecorderSettings()
    private var visualizerHandler: VisualizerHandler = VisualizerHandler()
    private var isRecording = false
    private var mediaPlayer:MediaPlayer= MediaPlayer()
    //private val saveMenuItem: MenuItem = MenuItem()
    private var recorder: Recorder?=null
    private var timer: Timer = Timer()
    private var playerSecondsElapsed = 0
    private var recorderSecondsElapsed = 0
    private val REQUEST_RECORD_AUDIO = 0
    var audioFileTempPath: String =""
    var audioFileRootPath: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(intent.getSerializableExtra(AndroidAudioRecorderSettings.EXTRA_ANDROID_AUDIO_RECORDER_SETTINGS)!=null){

            recorderSettings=intent.getSerializableExtra(AndroidAudioRecorderSettings.EXTRA_ANDROID_AUDIO_RECORDER_SETTINGS) as AndroidAudioRecorderSettings

        }
        setContentView(R.layout.activity_master_audio_recorder)
        audioFileRootPath= this.getExternalFilesDir(null)!!.getAbsolutePath()
        audioFileTempPath= audioFileRootPath+ "/temp.wav"
        audioFileTempPath.toast(this)

        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO)
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        visualizerView = GLAudioVisualizationView.Builder(this)
            .setLayersCount(1)
            .setWavesCount(6)
            .setWavesHeight(R.dimen.aar_wave_height)
            .setWavesFooterHeight(R.dimen.aar_footer_height)
            .setBubblesPerLayer(20)
            .setBubblesSize(R.dimen.aar_bubble_size)
            .setBubblesRandomizeSize(true)
            .setBackgroundColor(Util.getDarkerColor(recorderSettings.color))
            .setLayerColors(intArrayOf(recorderSettings.color))
            .build()
        recordImageButton.setOnClickListener {
            toggleRecording()
        }
        deleteImageButton.setOnClickListener {
            stopRecording()
        }
        menuImageButton.setOnClickListener {
            startActivity(Intent(this, AudiosListActivity::class.java))
            CustomIntent.customType(this, IntentAnimation.FadeIn)


        }

        contentRelativeLayout.setBackgroundColor(Util.getDarkerColor(recorderSettings.color));
        contentRelativeLayout.addView(visualizerView, 0);
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==REQUEST_RECORD_AUDIO) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun togglePlaying() {
        pauseRecording()
        Util.wait(100, Runnable {
            if (isPlaying()) {
                stopPlaying()
            } else {
                startPlaying()
            }
        })
    }

    private fun stopRecording() {
        visualizerHandler.stop()
        visualizerHandler.release()

        visualizerView!!.release()
        visualizerView!!.stopRendering()

        isRecording=false
        recordImageButton.setImageResource(R.drawable.aar_ic_rec)
        timerTextView.setText("00:00:00")




        recorderSecondsElapsed = 0
        if (recorder != null) {
            recorder!!.stopRecording()
            recorder = null
        }
        stopTimer()
    }
    private fun startPlaying() {
        try {
            stopRecording()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(audioFileTempPath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            visualizerView!!.linkTo<ByteArray>(
                DbmHandler.Factory.newVisualizerHandler(
                    this,
                    mediaPlayer
                )
            )
            visualizerView!!.post { mediaPlayer.setOnCompletionListener(this) }
            timerTextView.setText("00:00:00")
            statusTextView.setText(R.string.aar_playing)
            statusTextView.setVisibility(View.VISIBLE)
        //    playImageButton.setImageResource(R.drawable.aar_ic_stop)
            playerSecondsElapsed = 0
            startTimer()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    override fun onAudioChunkPulled(audioChunk: AudioChunk?) {
        val amplitude =
            if (isRecording) audioChunk!!.maxAmplitude().toFloat() else 0f
        visualizerHandler.onDataReceived(amplitude)
    }


    override fun onCompletion(mp: MediaPlayer?) {
        stopPlaying()
    }
    private fun stopTimer() {
        if (timer != null) {
            timer.cancel()
            timer.purge()
            //timer = null
        }
    }
    private fun startTimer() {
        stopTimer()
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateTimer()
            }
        }, 0, 1000)
    }

    private fun updateTimer() {
        runOnUiThread {
            if (isRecording) {
                recorderSecondsElapsed++
                timerTextView.setText(Util.formatSeconds(recorderSecondsElapsed))
            } else if (isPlaying()) {
                playerSecondsElapsed++
                timerTextView.setText(Util.formatSeconds(playerSecondsElapsed))
            }
        }
    }
    private fun isPlaying(): Boolean {
        return try {
            mediaPlayer != null && mediaPlayer.isPlaying() && !isRecording
        } catch (e: java.lang.Exception) {
            false
        }
    }
    private fun pauseRecording() {
        isRecording = false
       /* if (!isFinishing) {
            saveMenuItem.setVisible(true)
        }*/

        statusTextView.setText(R.string.aar_paused)
        statusTextView.setVisibility(View.VISIBLE)
       // restartImageButton.setVisibility(View.VISIBLE)
       // playImageButton.setVisibility(View.VISIBLE)
        recordImageButton.setImageResource(R.drawable.aar_ic_rec)
       // restartImageButton.setImageResource(R.drawable.aar_ic_rec)
     //   playImageButton.setImageResource(R.drawable.aar_ic_play)
        visualizerView!!.release()
        visualizerHandler?.stop()
        if (recorder != null) {
            recorder!!.pauseRecording()
        }
        stopTimer()
    }
    fun toggleRecording() {
        stopPlaying()
        Util.wait(100, Runnable {
            if (isRecording) {
                stopRecording()
            } else {
                recordNewAudio()
            }
        })
    }


    private fun stopPlaying() {
        statusTextView.setText("")
        statusTextView.setVisibility(View.INVISIBLE)
       // playImageButton.setImageResource(R.drawable.aar_ic_play)
        visualizerView!!.release()

        visualizerHandler.stop()

        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop()
                mediaPlayer.reset()
            } catch (e: Exception) {
            }
        }
        stopTimer()
    }

    private fun recordNewAudio() {
        val sdf = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
        val currentDate = sdf.format(Date())
        audioFileTempPath= audioFileRootPath+ "/Audio "+currentDate+".wav"

        isRecording = true
        //saveMenuItem.setVisible(false)
        statusTextView.setText(R.string.aar_recording)
        statusTextView.setVisibility(View.VISIBLE)
        // restartImageButton.setVisibility(View.INVISIBLE)
        //  playImageButton.setVisibility(View.INVISIBLE)
        recordImageButton.setImageResource(R.drawable.aar_ic_stop)
        //  playImageButton.setImageResource(R.drawable.aar_ic_play)
        visualizerHandler = VisualizerHandler()
        // visualizerView!!.linkTo<Any>(visualizerHandler)
        visualizerView!!.linkTo(visualizerHandler)

        if (recorder == null) {
            timerTextView.setText("00:00:00")
            recorder = OmRecorder.wav(
                PullTransport.Default(
                    Util.getMic( recorderSettings.source, recorderSettings.channel, recorderSettings.sampleRate),
                    this
                ),
                File(audioFileTempPath)
            )
        }
        recorder!!.resumeRecording()
        startTimer()
    }
    private fun resumeRecording() {
        isRecording = true
        //saveMenuItem.setVisible(false)
        statusTextView.setText(R.string.aar_recording)
        statusTextView.setVisibility(View.VISIBLE)
       // restartImageButton.setVisibility(View.INVISIBLE)
      //  playImageButton.setVisibility(View.INVISIBLE)
        recordImageButton.setImageResource(R.drawable.aar_ic_stop)
      //  playImageButton.setImageResource(R.drawable.aar_ic_play)
        visualizerHandler = VisualizerHandler()
       // visualizerView!!.linkTo<Any>(visualizerHandler)
        visualizerView!!.linkTo(visualizerHandler)

        if (recorder == null) {
            timerTextView.setText("00:00:00")
            recorder = OmRecorder.wav(
                PullTransport.Default(
                    Util.getMic( recorderSettings.source, recorderSettings.channel, recorderSettings.sampleRate),
                    this
                ),
                File(audioFileTempPath)
            )
        }
        recorder!!.resumeRecording()
        startTimer()
    }
}

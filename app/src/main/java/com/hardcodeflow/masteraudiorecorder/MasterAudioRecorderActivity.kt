package com.hardcodeflow.masteraudiorecorder

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.audiovisualization.GLAudioVisualizationView
import com.hardcodeflow.masteraudiorecorder.audio.AndroidAudioRecorderSettings
import com.hardcodeflow.masteraudiorecorder.audio.VisualizerHandler
import com.hardcodeflow.masteraudiorecorder.common.Util
import kotlinx.android.synthetic.main.activity_master_audio_recorder.*
import omrecorder.AudioChunk
import omrecorder.OmRecorder
import omrecorder.PullTransport
import omrecorder.Recorder
import java.io.File
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(intent.getSerializableExtra(AndroidAudioRecorderSettings.EXTRA_ANDROID_AUDIO_RECORDER_SETTINGS)!=null){

            recorderSettings=intent.getSerializableExtra(AndroidAudioRecorderSettings.EXTRA_ANDROID_AUDIO_RECORDER_SETTINGS) as AndroidAudioRecorderSettings

        }
        setContentView(R.layout.activity_master_audio_recorder)

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
    }

    override fun onAudioChunkPulled(audioChunk: AudioChunk?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompletion(mp: MediaPlayer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        restartImageButton.setVisibility(View.VISIBLE)
        playImageButton.setVisibility(View.VISIBLE)
        restartImageButton.setImageResource(R.drawable.aar_ic_rec)
        playImageButton.setImageResource(R.drawable.aar_ic_play)
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
                pauseRecording()
            } else {
                resumeRecording()
            }
        })
    }


    private fun stopPlaying() {
        statusTextView.setText("")
        statusTextView.setVisibility(View.INVISIBLE)
        playImageButton.setImageResource(R.drawable.aar_ic_play)
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


    private fun resumeRecording() {
        isRecording = true
        //saveMenuItem.setVisible(false)
        statusTextView.setText(R.string.aar_recording)
        statusTextView.setVisibility(View.VISIBLE)
        restartImageButton.setVisibility(View.INVISIBLE)
        playImageButton.setVisibility(View.INVISIBLE)
        recordImageButton.setImageResource(R.drawable.aar_ic_pause)
        playImageButton.setImageResource(R.drawable.aar_ic_play)
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
                File(recorderSettings.filePath)
            )
        }
        recorder!!.resumeRecording()
        startTimer()
    }
}

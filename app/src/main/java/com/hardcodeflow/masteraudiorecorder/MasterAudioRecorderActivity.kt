package com.hardcodeflow.masteraudiorecorder

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.audiovisualization.GLAudioVisualizationView
import com.hardcodeflow.masteraudiorecorder.audio.AndroidAudioRecorderSettings
import com.hardcodeflow.masteraudiorecorder.common.Util
import omrecorder.AudioChunk
import omrecorder.PullTransport


class MasterAudioRecorderActivity : AppCompatActivity(), PullTransport.OnAudioChunkPulledListener,
    OnCompletionListener {
    private var visualizerView: GLAudioVisualizationView? = null
    private var recorderSettings: AndroidAudioRecorderSettings =AndroidAudioRecorderSettings()
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
    }

    override fun onAudioChunkPulled(audioChunk: AudioChunk?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompletion(mp: MediaPlayer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

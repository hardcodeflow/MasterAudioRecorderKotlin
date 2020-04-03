package com.hardcodeflow.masteraudiorecorder.audio

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import androidx.fragment.app.Fragment
import com.hardcodeflow.masteraudiorecorder.MasterAudioRecorderActivity

class AndroidAudioRecorderSettings {

    companion object {
        val EXTRA_ANDROID_AUDIO_RECORDER_SETTINGS = "AndroidAudioRecorderSettings"
    }
     var activity: Activity? = null
     var fragment: Fragment? = null

     var filePath =
        Environment.getExternalStorageDirectory().toString() + "/recorded_audio.wav"
     var source = AudioSource.MIC
     var channel = AudioChannel.STEREO
     var sampleRate = AudioSampleRate.HZ_44100
     var color = Color.parseColor("#546E7A")
     var requestCode = 0
     var autoStart = false
     var keepDisplayOn = false

    private fun AndroidAudioRecorder(activity: Activity) {
        this.activity = activity
    }

    private fun AndroidAudioRecorder(fragment: Fragment) {
        this.fragment = fragment
    }

    fun with(activity: Activity?): AndroidAudioRecorderSettings{
        return this
    }

    fun with(fragment: Fragment?): AndroidAudioRecorderSettings {
        return this
    }

    fun setFilePath(filePath: String):AndroidAudioRecorderSettings {
        this.filePath = filePath
        return this
    }

    fun setColor(color: Int): AndroidAudioRecorderSettings {
        this.color = color
        return this
    }

    fun setRequestCode(requestCode: Int): AndroidAudioRecorderSettings{
        this.requestCode = requestCode
        return this
    }

    fun setSource(source: AudioSource): AndroidAudioRecorderSettings {
        this.source = source
        return this
    }

    fun setChannel(channel: AudioChannel): AndroidAudioRecorderSettings {
        this.channel = channel
        return this
    }

    fun setSampleRate(sampleRate: AudioSampleRate): AndroidAudioRecorderSettings {
        this.sampleRate = sampleRate
        return this
    }

    fun setAutoStart(autoStart: Boolean): AndroidAudioRecorderSettings{
        this.autoStart = autoStart
        return this
    }

    fun setKeepDisplayOn(keepDisplayOn: Boolean): AndroidAudioRecorderSettings {
        this.keepDisplayOn = keepDisplayOn
        return this
    }
    /*
    fun record() {
        val intent = Intent(activity, MasterAudioRecorderActivity::class.java)
        intent.putExtra(EXTRA_FILE_PATH, filePath)
        intent.putExtra(EXTRA_COLOR, color)
        intent.putExtra(EXTRA_SOURCE, source)
        intent.putExtra(EXTRA_CHANNEL, channel)
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate)
        intent.putExtra(EXTRA_AUTO_START, autoStart)
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn)
        activity!!.startActivityForResult(intent, requestCode)
    }

    fun recordFromFragment() {
        val intent = Intent(fragment!!.activity, MasterAudioRecorderActivity::class.java)
        intent.putExtra(EXTRA_FILE_PATH, filePath)
        intent.putExtra(EXTRA_COLOR, color)
        intent.putExtra(EXTRA_SOURCE, source)
        intent.putExtra(EXTRA_CHANNEL, channel)
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate)
        intent.putExtra(EXTRA_AUTO_START, autoStart)
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn)
        fragment!!.startActivityForResult(intent, requestCode)
    }*/

}
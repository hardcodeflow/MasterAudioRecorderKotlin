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

   /*  var filePath =
        Environment.getExternalStorageDirectory().toString() + "/temp.wav"*/
     var source = AudioSource.MIC
     var channel = AudioChannel.STEREO
     var sampleRate = AudioSampleRate.HZ_44100
     var color = Color.parseColor("#546E7A")
     var requestCode = 0
     var autoStart = false
     var keepDisplayOn = false

}
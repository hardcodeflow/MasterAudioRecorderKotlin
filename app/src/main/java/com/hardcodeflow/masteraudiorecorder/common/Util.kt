package com.hardcodeflow.masteraudiorecorder.common

import android.R
import android.graphics.Color
import android.media.AudioFormat
import android.os.Handler
import com.hardcodeflow.masteraudiorecorder.audio.AudioChannel
import com.hardcodeflow.masteraudiorecorder.audio.AudioSampleRate
import com.hardcodeflow.masteraudiorecorder.audio.AudioSource


class Util {

    private val HANDLER = Handler()

    private fun Util() {}
    fun wait(millis: Int, callback: Runnable?) {
        HANDLER.postDelayed(callback, millis.toLong())
    }
    companion object {

    fun getMic(
        source: AudioSource,
        channel: AudioChannel,
        sampleRate: AudioSampleRate
    ): omrecorder.AudioSource.Smart {
        return omrecorder.AudioSource.Smart(
            source.source,
            AudioFormat.ENCODING_PCM_16BIT,
            channel.channel,
            sampleRate.sampleRate
        )
    }

    fun isBrightColor(color: Int): Boolean {
        if (R.color.transparent == color) {
            return true
        }
        val rgb = intArrayOf(
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
        val brightness = Math.sqrt(
            rgb[0] * rgb[0] * 0.241 + rgb[1] * rgb[1] * 0.691 + rgb[2] * rgb[2] * 0.068
        ).toInt()
        return brightness >= 200
    }

    fun getDarkerColor(color: Int): Int {
        val factor = 0.8f
        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return Color.argb(
            a,
            Math.max((r * factor).toInt(), 0),
            Math.max((g * factor).toInt(), 0),
            Math.max((b * factor).toInt(), 0)
        )
    }

    fun formatSeconds(seconds: Int): String? {
        return (getTwoDecimalsValue(seconds / 3600) + ":"
                + getTwoDecimalsValue(seconds / 60) + ":"
                + getTwoDecimalsValue(seconds % 60))
    }

    private fun getTwoDecimalsValue(value: Int): String {
        return if (value >= 0 && value <= 9) {
            "0$value"
        } else {
            value.toString() + ""
        }
    }
}
}
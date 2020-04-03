package com.hardcodeflow.masteraudiorecorder.audio

enum class AudioSampleRate {
    HZ_48000, HZ_44100, HZ_32000, HZ_22050, HZ_16000, HZ_11025, HZ_8000;

    val sampleRate: Int
        get() = name.replace("HZ_", "").toInt()
}
package com.hardcodeflow.masteraudiorecorder.audio

import com.cleveroad.audiovisualization.DbmHandler


class VisualizerHandler : DbmHandler<Float>() {
    override fun onDataReceivedImpl(
        amplitude: Float,
        layersCount: Int,
        dBmArray: FloatArray,
        ampsArray: FloatArray
    ) {
        var amplitude = amplitude
        amplitude = amplitude / 100
        if (amplitude <= 0.5) {
            amplitude = 0.0f
        } else if (amplitude > 0.5 && amplitude <= 0.6) {
            amplitude = 0.2f
        } else if (amplitude > 0.6 && amplitude <= 0.7) {
            amplitude = 0.6f
        } else if (amplitude > 0.7) {
            amplitude = 1f
        }
        try {
            dBmArray[0] = amplitude
            ampsArray[0] = amplitude
        } catch (e: Exception) {
        }
    }

    fun stop() {
        try {
            calmDownAndStopRendering()
        } catch (e: Exception) {
        }
    }
}
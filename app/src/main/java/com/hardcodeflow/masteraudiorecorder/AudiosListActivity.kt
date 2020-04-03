package com.hardcodeflow.masteraudiorecorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardcodeflow.masteraudiorecorder.adapters.AudioRecordListAdapter
import com.hardcodeflow.masteraudiorecorder.entities.AudioRecordData
import kotlinx.android.synthetic.main.activity_audios_list.*

class AudiosListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audios_list)
        var audios= mutableListOf<AudioRecordData>()

        for (i in 0..10){
            audios.add(AudioRecordData())
            audios.add(AudioRecordData())
            audios.add(AudioRecordData())
        }
        var lessonsListAdapter=AudioRecordListAdapter(audios)

        audiosRecyclerView.layoutManager= LinearLayoutManager(this)
        audiosRecyclerView.adapter=lessonsListAdapter



    }
}

package com.hardcodeflow.masteraudiorecorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardcodeflow.masteraudiorecorder.adapters.AudioRecordListAdapter
import com.hardcodeflow.masteraudiorecorder.entities.AudioRecordData
import kotlinx.android.synthetic.main.activity_audios_list.*
import java.io.File

class AudiosListActivity : AppCompatActivity() {
    var audioFileRootPath: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioFileRootPath= this.getExternalFilesDir(null)!!.getAbsolutePath()+"/"

        setContentView(R.layout.activity_audios_list)
        var audios= mutableListOf<AudioRecordData>()
        File(audioFileRootPath).walk().forEach {
            //println(it)
            if(  File(it.toString()).exists())
            audios.add(AudioRecordData(it.toString()))

        }

        var lessonsListAdapter=AudioRecordListAdapter(audios)

        audiosRecyclerView.layoutManager= LinearLayoutManager(this)
        audiosRecyclerView.adapter=lessonsListAdapter



    }
}

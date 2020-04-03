package com.hardcodeflow.masteraudiorecorder.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hardcodeflow.masteraudiorecorder.R
import com.hardcodeflow.masteraudiorecorder.entities.AudioRecordData
import com.hardcodeflow.masteraudiorecorder.viewholders.AudioRecordViewHolder
import com.hardcodeflow.whitefox.extensions.inflate

class AudioRecordListAdapter(val audioRecordList: List<AudioRecordData>) : RecyclerView.Adapter<AudioRecordViewHolder>() {

   // var onListAlbumPhotoItemClickListener: OnLessonItemClick? = null

    /*constructor(photoItemList: List<LessonData>, onListAlbumPhotoItemClickListener: OnLessonItemClick):this(photoItemList) {
        this.onListAlbumPhotoItemClickListener = onListAlbumPhotoItemClickListener
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioRecordViewHolder{
        val inflatedView = parent.inflate(R.layout.item_reocord_audio, false)
        return AudioRecordViewHolder(inflatedView)
    }

    override fun onBindViewHolder(mediaListAlbumPhotoViewHolder: AudioRecordViewHolder, position: Int) {
        mediaListAlbumPhotoViewHolder.bind(audioRecordList[position])
    }

    override fun getItemCount() = audioRecordList.size
}
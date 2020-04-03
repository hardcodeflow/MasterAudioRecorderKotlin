package com.hardcodeflow.masteraudiorecorder.viewholders

import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.hardcodeflow.masteraudiorecorder.R
import com.hardcodeflow.masteraudiorecorder.entities.AudioRecordData
import kotlinx.android.synthetic.main.item_reocord_audio.view.*


class AudioRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

    private var view: View = itemView

    private lateinit var audioRecordData:AudioRecordData
   // private var onListAlbumPhotoItemClick: OnLessonItemClick? = null
    private val columnWidth=2
    private var audioIsPlaying:Boolean=false
    init {

        itemView.playImageButton.setOnClickListener {
            if(!audioIsPlaying){
                playAudio()
            }else{
                stopAudio()

            }
            audioIsPlaying=!audioIsPlaying

        }

        /*itemView.imagePlay.setOnClickListener {
            item.url.toString().toast(view?.context!!)
           // onListAlbumPhotoItemClick?.onLessonItemClick(itemView, item)

        }*/

        /*itemView.imageViewCard.setOnClickListener {
            item.url.toString().toast(view?.context!!)
            val mainActivity = Intent(view?.context!!, BookDetailsActivity::class.java)
            view?.context!!.startActivity(mainActivity)
            //  onListAlbumPhotoItemClick?.onLessonItemClick(itemView, item)

        }*/

    }

    fun playAudio(){
        itemView.playImageButton.setImageResource(R.drawable.aar_ic_stop)

    }
    fun stopAudio(){

        itemView.playImageButton.setImageResource(R.drawable.aar_ic_play)
    }

    fun bind(audioRecordData:AudioRecordData) {
        this.audioRecordData = audioRecordData
      //  this.onListAlbumPhotoItemClick = onListAlbumPhotoItemClick
        // view.imageViewCard.load(item.url)
        //view.imageViewCard.layoutParams= RelativeLayout.LayoutParams(SharedPreferences.data.screenWith, SharedPreferences.data.screenWith / 3 + 80)
        //view.frontImage.layoutParams= RelativeLayout.LayoutParams(SharedPreferences.data.screenWith, SharedPreferences.data.screenWith / 3 + 80)
        view.audioNameTextView.text=audioRecordData.audioRecordName

    }

    override fun onClick(v: View?) {
      //  onListAlbumPhotoItemClick?.onLessonItemClick(v, item)
    }

}

package com.example.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.utils.OnItemRecyclerViewClickListener
import kotlinx.android.synthetic.main.layout_song_item.view.*

class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private val songs = mutableListOf<Song>()
    private var onItemClickListener: OnItemRecyclerViewClickListener? = null

    fun updateData(songs: MutableList<Song>) {
        songs?.let {
            this.songs.clear()
            this.songs.addAll(songs)
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemRecyclerViewClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(
        itemView: View,
        val onItemClickListener: OnItemRecyclerViewClickListener?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView
        val artist: TextView
        val image: ImageView


        init {
            title = itemView.textTitleSong
            artist = itemView.textArtistSong
            image = itemView.imageSong
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClickListener(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_song_item, parent, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        songs[position].apply {
            holder.title.text = title
            holder.artist.text = artist
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}

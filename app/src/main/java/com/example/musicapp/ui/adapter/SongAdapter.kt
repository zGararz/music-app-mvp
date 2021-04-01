package com.example.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.data.model.Song

import kotlinx.android.synthetic.main.layout_song_item.view.*

class SongAdapter(
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private val songs = mutableListOf<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_song_item, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        songs[position].also {
            holder.itemView.textTitleSong.text = it.title
            holder.itemView.textArtistSong.text = it.artist
        }
    }

    override fun getItemCount() = songs.size

    fun updateData(songs: MutableList<Song>) {
        songs.run {
            clear()
            addAll(songs)
            notifyDataSetChanged()
        }
    }

    class ViewHolder(
        itemView: View,
        private val itemCLick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                itemCLick(adapterPosition)
            }
        }
    }
}

package com.example.musicapp.ui.screenplayer

import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.utils.OnDataLoadCallBack

class PlayerPresenter(
    private val view: PlayerContract.View,
    private val repository: SongRepository
) : PlayerContract.Presenter {

    override fun getLocalSong() {
        repository.getSong(object : OnDataLoadCallBack<List<Song>> {
            override fun onSuccess(data: List<Song>?) {
                data?.let {
                    if (!it.isEmpty()) {
                        view.loadSuccess(it)
                    }
                }
            }

            override fun onFail(e: String) {
                view.showError(e)
            }

        })
    }


}

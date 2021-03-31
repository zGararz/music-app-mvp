package com.example.musicapp.ui.screenplayer

import android.os.Handler
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.service.MusicService
import com.example.musicapp.utils.OnDataLoadCallBack

class PlayerPresenter(
    private val view: PlayerContract.View,
    private val repository: SongRepository,
    private val service: MusicService
) : PlayerContract.Presenter {
    private val songs = mutableListOf<Song>()
    private var p = 0
    private var handlerProcess: Handler? = null


    override fun getLocalSong() {
        repository.getSong(object : OnDataLoadCallBack<List<Song>> {
            override fun onSuccess(data: List<Song>?) {
                data?.let {
                    songs.clear()
                    songs.addAll(it)
                    view.loadSuccess(it)
                    view.setSongInfo(it[0])
                }
            }

            override fun onFail(e: String) {
                view.showError(e)
            }

        })
    }

    override fun handleCreateSong() {
        service.create(songs[p])
        view.apply {
            setSongInfo(songs[p])
            setMaxSeekBar(service.getDuration())
            setSeekBar(0)
            setTime(0)
        }

    }


    override fun handlePlaySong(b: Boolean?) {
        if (b != null) setPlay(b) else setPlay(!service.isPlaying())
    }

    override fun handleNextSong() {
        if (p < (songs.size - 1)) p++ else p = 0
        handleCreateSong()
        service.play()
        setPlay(true)
    }

    override fun handlePrevSong() {
        if (p > 0) p-- else p = songs.size - 1
        handleCreateSong()
        setPlay(true)
    }

    override fun handlerSeek(p: Int?) {
        p?.let { service.seekto(it) }
    }

    fun setSongPosition(pos: Int) {
        p = pos
    }

    private fun setPlay(b: Boolean) {
        if (b) {
            service.play()
            view.setPlayButton(true)
            process()

        } else {
            service.pause()
            view.setPlayButton(false)
        }
    }

    private fun process() {
        handlerProcess = Handler()
        handlerProcess?.postDelayed(object : Runnable {
            override fun run() {
                view.setSeekBar(service.getCurrentPosition())
                view.setTime(service.getCurrentPosition())
                if (service.isPlaying()) {
                    handlerProcess?.postDelayed(this, 100)
                }
            }
        }, 100)
    }

}

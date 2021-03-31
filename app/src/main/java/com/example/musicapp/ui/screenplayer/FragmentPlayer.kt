package com.example.musicapp.ui.screenplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.data.source.local.SongLocalDataSource
import com.example.musicapp.notifications.*
import com.example.musicapp.service.MusicService
import com.example.musicapp.ui.adapter.SongAdapter
import com.example.musicapp.utils.OnItemRecyclerViewClickListener
import com.example.musicapp.utils.SimpleTimeFormat

import kotlinx.android.synthetic.main.fragment_player.*

const val EXCESS_TIME = 500

class FragmentPlayer(val service: MusicService) : Fragment(), PlayerContract.View,
    View.OnClickListener, OnItemRecyclerViewClickListener, SeekBar.OnSeekBarChangeListener {
    lateinit var presenter: PlayerPresenter
    lateinit var notification: MusicNotification
    lateinit var song: Song
    val songAdapter: SongAdapter = SongAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
        initNotify()
        initListener()

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(broadcastReceiver)
        notification.cancelNotification()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonPlay -> {
                presenter.handlePlaySong(null)
            }
            R.id.buttonNext -> presenter.handleNextSong()
            R.id.buttonPrev -> presenter.handlePrevSong()
        }
    }


    override fun loadSuccess(list: List<Song>) {
        songAdapter.updateData(list as MutableList<Song>)
        presenter.handleCreateSong()
    }

    override fun showError(e: String) {
        Toast.makeText(activity, e, Toast.LENGTH_SHORT).show()
    }


    override fun setSongInfo(song: Song) {
        this.song = song
        textTitle.text = this.song.title
        textArtist.text = this.song.artist
    }

    override fun setPlayButton(b: Boolean) {
        if (!b) {
            buttonPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_play_circle_outline))
            notification.createNotification(song, R.drawable.ic_baseline_play_arrow_24)

        } else {
            buttonPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_circle_outline))
            notification.createNotification(song, R.drawable.ic_baseline_pause_24)
        }
    }

    override fun setSeekBar(process: Int) {

        seekbarSong.apply {
            progress = process
            if (progress >= (seekbarSong.max - EXCESS_TIME)) {
                presenter.handleNextSong()
            }
        }

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        presenter.handlerSeek(seekBar?.progress)
        presenter.handlePlaySong(true)
    }

    override fun setMaxSeekBar(max: Int) {
        seekbarSong.max = max
    }

    override fun setTime(time: Int) {
        textEndTime.text = SimpleTimeFormat.format(seekbarSong.max - time)
        textStartTime.text = SimpleTimeFormat.format(time)

    }

    override fun onItemClickListener(pos: Int) {
        presenter.setSongPosition(pos)
        presenter.handleCreateSong()
        presenter.handlePlaySong(true)
    }

    fun initListener() {
        buttonPlay.setOnClickListener(this)
        buttonNext.setOnClickListener(this)
        buttonPrev.setOnClickListener(this)
        seekbarSong.setOnSeekBarChangeListener(this)

    }

    fun initRecyclerView() {
        songAdapter.setOnItemClickListener(this)
        recyclerView.adapter = songAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    fun initData() {
        presenter = PlayerPresenter(
            this,
            SongRepository.getInstance(SongLocalDataSource.getInstance(activity!!)),
            service
        )
        presenter.getLocalSong()
    }

    fun initNotify() {
        notification = MusicNotification(activity!!.applicationContext)
        activity?.registerReceiver(broadcastReceiver, IntentFilter(TRACK))
        activity?.startService(Intent(activity, MusicService::class.java))
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.getStringExtra(ACTION_NAME)
            Log.d("AAA", action.toString())
            when (action) {
                ACTION_NEXT -> presenter.handleNextSong()
                ACTION_PLAY -> presenter.handlePlaySong(null)
                ACTION_PREV -> presenter.handlePrevSong()
            }
        }

    }
}

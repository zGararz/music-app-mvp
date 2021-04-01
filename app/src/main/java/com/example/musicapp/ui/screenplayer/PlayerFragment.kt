package com.example.musicapp.ui.screenplayer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.data.source.local.SongLocalDataSource
import com.example.musicapp.data.source.local.SongLocalHandler
import com.example.musicapp.notifications.*
import com.example.musicapp.service.MusicService
import com.example.musicapp.ui.adapter.SongAdapter
import com.example.musicapp.utils.SimpleTimeFormat

import kotlinx.android.synthetic.main.fragment_player.*

private const val EXCESS_TIME = 500

@Suppress("DEPRECATION")
class FragmentPlayer(
    val service: MusicService
) : Fragment(),
    PlayerContract.View,
    View.OnClickListener,
    SeekBar.OnSeekBarChangeListener {

    private var presenter: PlayerPresenter? = null
    private var notification: MusicNotification? = null
    private val songs = mutableListOf<Song>()
    private val songAdapter = SongAdapter(this::onItemClickListener)
    private var currentSong = 0
    private var handlerProcess: Handler? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_player, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
        initNotify()
        initListener()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonPlay -> buttonPlayListener(null)
            R.id.buttonNext -> buttonNextListener()
            R.id.buttonPrev -> buttonPrevListener()
        }
    }

    override fun loadSuccess(songs: List<Song>) {
        this.songs.addAll(songs)
        songAdapter.updateData(songs.toMutableList())
        createSong()
        songs[0].showSongInfo()
    }

    override fun showError(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.progress?.let { service.seekTo(it) }
        buttonPlayListener(true)
    }

    private fun initRecyclerView() {
        recyclerViewSongs.adapter = songAdapter
    }

    private fun initData() {
        presenter = PlayerPresenter(
                this,
                SongRepository.getInstance(SongLocalDataSource.getInstance(SongLocalHandler(activity!!)))
        )
        presenter?.getLocalSong()
    }

    private fun initNotify() {
        notification = MusicNotification(activity!!.applicationContext)
        activity?.registerReceiver(broadcastReceiver, IntentFilter(TRACK))
        activity?.startService(Intent(activity, MusicService::class.java))
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.getStringExtra(ACTION_NAME)) {
                    ACTION_NEXT -> buttonNextListener()
                    ACTION_PLAY -> buttonPlayListener(null)
                    ACTION_PREV -> buttonPrevListener()
                }
            }

        }
    }

    private fun initListener() {
        buttonPlay.setOnClickListener(this)
        buttonNext.setOnClickListener(this)
        buttonPrev.setOnClickListener(this)
        seekBarSong.setOnSeekBarChangeListener(this)
    }

    private fun Song.showSongInfo() {
        textTitle.text = title
        textArtist.text = artist
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setButtonPlay(b: Boolean) {
        if (!b) {
            buttonPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_play_circle_outline))
            songs[currentSong].let {
                notification?.createNotification(
                    it,
                    R.drawable.ic_baseline_play_arrow_24
                )
            }
        } else {
            buttonPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_circle_outline))
            songs[currentSong].let {
                notification?.createNotification(
                    it,
                    R.drawable.ic_baseline_pause_24
                )
            }
        }
    }

    private fun setSeekBar(process: Int) {
        seekBarSong.apply {
            progress = process
            if (progress >= (seekBarSong.max - EXCESS_TIME)) {
                buttonNextListener()
            }
        }

    }

    private fun setMaxSeekBar(max: Int) {
        seekBarSong.max = max
    }

    private fun setTime(time: Int) {
        textEndTime.text = SimpleTimeFormat.format(seekBarSong.max - time)
        textStartTime.text = SimpleTimeFormat.format(time)
    }

    private fun buttonPlayListener(b: Boolean?) {
        if (b != null) setPlay(b) else setPlay(!service.isPlaying())
    }

    private fun buttonNextListener() {
        if (currentSong < (songs.size - 1)) currentSong++ else currentSong = 0
        createSong()
        service.play()
        setPlay(true)
    }

    private fun buttonPrevListener() {
        if (currentSong > 0) currentSong-- else currentSong = songs.size - 1
        createSong()
        setPlay(true)
    }

    private fun process() {
        handlerProcess = Handler()
        handlerProcess?.postDelayed(object : Runnable {
            override fun run() {
                setSeekBar(service.getCurrentPosition())
                setTime(service.getCurrentPosition())
                if (service.isPlaying()) {
                    handlerProcess?.postDelayed(this, 100)
                }
            }
        }, 100)
    }

    private fun setPlay(b: Boolean) {
        if (b) {
            service.play()
            setButtonPlay(true)
            process()

        } else {
            service.pause()
            setButtonPlay(false)
        }
    }

    private fun createSong() {
        service.create(songs[currentSong])
        view.apply {
            songs[currentSong].showSongInfo()
            setMaxSeekBar(service.getDuration())
            setSeekBar(0)
            setTime(0)
        }
    }

    private fun onItemClickListener(pos: Int) {
        presenter?.apply {
            currentSong = pos
            createSong()
            buttonPlayListener(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(broadcastReceiver)
        notification?.cancelNotification()
    }
}

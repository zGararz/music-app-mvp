package com.example.musicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.service.MusicService
import com.example.musicapp.ui.screenplayer.FragmentPlayer

class MainActivity : AppCompatActivity() {
    private var service: MusicService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initService()
    }

    fun initService() {
        bindService(MusicService.getIntent(this), serviceConnection, BIND_AUTO_CREATE)
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.SongBinder
            this@MainActivity.service = binder.getService()
            this@MainActivity.service?.let {
                supportFragmentManager.beginTransaction()
                        .add(R.id.frameRoot, FragmentPlayer(it))
                        .commit()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }
     companion object {
         fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
     }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}

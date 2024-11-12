package com.vlunov.todo.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class MainService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        Toast.makeText(this, "Server is runnining", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}
package com.vlunov.todo.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vlunov.todo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TodoListFragment())
                .commit()
        }

        //startService(Intent(this, MainService::class.java))*/
    }
}
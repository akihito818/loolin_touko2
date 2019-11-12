package com.example.lookin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class Genre : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.genre)


        val rButton = findViewById<View>(R.id.imageButton) as ImageButton
        rButton.setOnClickListener{
            val intent = Intent(this@Genre, touko::class.java)
            intent.putExtra("id", "")
            startActivity(intent)
        }
        val Button = findViewById<View>(R.id.imageButton4) as ImageButton
        Button.setOnClickListener{
            val intent = Intent(this@Genre, ImageActivity::class.java)
            startActivity(intent)
        }

    }
}
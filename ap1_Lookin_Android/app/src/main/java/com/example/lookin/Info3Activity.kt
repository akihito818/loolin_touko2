package com.example.lookin



import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.info3.*
import android.widget.Toast


class Info3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info3)

        val photo = getDrawable(intent.getIntExtra(PHOTO, 0))
        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(photo).into(imageView)


        imageButton.setOnClickListener {
            finish()
        }

        favButton.setOnClickListener {
            Toast.makeText(applicationContext, "お気に入り", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PHOTO: String = "photo"

        fun createIntent2(activity: Activity, @DrawableRes photo: Int) =
            Intent(activity, Info3Activity::class.java).apply {
                putExtra(PHOTO, photo)
            }
    }

}
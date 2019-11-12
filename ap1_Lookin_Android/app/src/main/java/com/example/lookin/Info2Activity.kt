package com.example.lookin


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout.VERTICAL
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.info2.*
import kotlinx.android.synthetic.main.info3.*
import kotlinx.android.synthetic.main.info3.imageButton


class Info2Activity : AppCompatActivity() {

    private val onImageClick = { resId: Int ->
        val intent = Info3Activity.createIntent2(this, resId)
        startActivity(intent)
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info2)

        val photo = intent.getIntArrayExtra(PHOTO)
        val category = intent.getStringExtra(CATEGORY)

        imageButton.setOnClickListener {
            finish()
        }

        val atmosRecyclerView = findViewById<View>(R.id.atmosRecyclerView) as RecyclerView

        categoryText.text = category

        val lManager = GridLayoutManager(this, 3, VERTICAL, false)
        atmosRecyclerView.apply {
            layoutManager = lManager
            //
            adapter = InfoAdapter(photo, this@Info2Activity, onImageClick)
        }


    }

    companion object {
        private const val PHOTO: String = "photo"
        private const val CATEGORY: String = "CATE"

        fun createIntent(activity: Activity, @DrawableRes photo: IntArray, category: String) =
            Intent(activity, Info2Activity::class.java).apply {
                putExtra(PHOTO, photo)
                putExtra(CATEGORY, category)
            }
    }
}

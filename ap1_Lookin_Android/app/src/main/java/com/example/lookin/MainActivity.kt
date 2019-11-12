package com.example.lookin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.second.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second)

        val dataList = mutableListOf<TimeLineData>()

        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 2, true))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 0, false))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 10, true))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 0, false))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 0, false))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 6, true))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 0, false))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 7, true))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 0, false))
        dataList.add(TimeLineData("ユーザー名","テキストてきすとテキストてきすとテキストてきすとテキストてきすと" ,"1 hours ago", 0, false))

        //RecyclerViewにAdapterとLayoutManagerを設定
        findViewById<RecyclerView>(R.id.TimeLineRecyclerView).also { recyclerView: RecyclerView ->
            recyclerView.adapter = TimeLineAdapter(this, dataList)
            recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        }

        closeButton.setOnClickListener {
            val intentback = Intent(application, ImageActivity::class.java)
            startActivity(intentback)
        }

    }
}

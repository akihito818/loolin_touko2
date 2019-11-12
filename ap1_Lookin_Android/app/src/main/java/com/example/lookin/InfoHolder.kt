package com.example.lookin


import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


//画像の保持
class InfoHolder (itemView: View, private val mContext: Context) : RecyclerView.ViewHolder(itemView){

    private val iView : ImageView

    init {
        iView = itemView.findViewById<View>(R.id.iview) as ImageView
    }

    fun index(item : Int){
        Glide.with(mContext).load(item).into(iView)
    }
}
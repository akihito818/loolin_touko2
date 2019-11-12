package com.example.lookin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//配列内の画像の位置
class InfoAdapter(
    private val place: IntArray,
    private val mContext: Context,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<InfoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.info_image, parent, false)
        return InfoHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.apply {
            index(place[position])
            itemView.setOnClickListener {
                onImageClick(place[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return place.size
    }
}
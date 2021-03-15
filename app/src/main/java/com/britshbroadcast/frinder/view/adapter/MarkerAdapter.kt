package com.britshbroadcast.frinder.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.britshbroadcast.frinder.databinding.MarkerItemLayoutBinding
import com.britshbroadcast.frinder.model.data.Result
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MarkerAdapter(private var resultList: List<Result>, private val markerDelegate: MarkerDelegate): RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {
    inner class MarkerViewHolder(val binding: MarkerItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    interface MarkerDelegate{
        fun selectMarker(result: Result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val binding = MarkerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MarkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {

        with(holder){

            with(resultList[position]){
                binding.nameTextView.text = this.name
                binding.addressTextView.text = this.vicinity
                Glide.with(binding.photoImageView)
                    .load(this.icon)
                    .into(binding.photoImageView)
                holder.itemView.setOnClickListener {
                    markerDelegate.selectMarker(this)
                }

            }
        }
    }

    override fun getItemCount(): Int = resultList.size

    fun updateData(data: List<Result>){
        resultList = data
        notifyDataSetChanged()
    }

}
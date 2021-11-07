package com.example.shopdrop.presentation.product_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.shopdrop.R
import com.example.shopdrop.common.GlideApp
import com.example.shopdrop.presentation.product_details.model.SliderItem
import com.google.firebase.storage.StorageReference
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(
    private val context: Context,
    private val slider: SliderItem,
    private val reference: StorageReference
) :
    SliderViewAdapter<SliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_slider_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GlideApp.with(context).load(reference.child("${slider.imagePath}/image${position + 1}.jpg"))
            .into(holder.image)
    }

    override fun getCount(): Int {
        return slider.size
    }


    inner class ViewHolder(item: View) : SliderViewAdapter.ViewHolder(item) {

        val image: ImageView = item.findViewById(R.id.product_image)
    }

}
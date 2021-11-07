package com.example.shopdrop.presentation.product_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants.PROFILE_PICTURE
import com.example.shopdrop.common.GlideApp
import com.example.shopdrop.domain.model.Review
import com.google.firebase.storage.StorageReference

class ReviewAdapter(
    private val context: Context,
    private val reviewList: List<Review>,
    private val reference: StorageReference
) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]
        GlideApp.with(context)
            .load(reference.child("${review.userProfileImage}/${PROFILE_PICTURE}"))
            .circleCrop().into(holder.profileImage)
        holder.reviewDate.text = review.reviewDate
        holder.userComment.text = review.userComment
        holder.userName.text = review.userName
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profileImage: ImageView = itemView.findViewById(R.id.review_image)
        val userName: TextView = itemView.findViewById(R.id.review_userName)
        val userComment: TextView = itemView.findViewById(R.id.review_comment)
        val reviewDate: TextView = itemView.findViewById(R.id.review_date)
    }
}
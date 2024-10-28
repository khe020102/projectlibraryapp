package com.example.libraryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.book.Book
import com.example.libraryapp.retrofit.review.ReviewResponse

class MyReviewAdapter(private var reviewList: List<ReviewResponse>,private var bookInfo: List<Book>) :
    RecyclerView.Adapter<MyReviewAdapter.ViewHolder>() {
    
    private lateinit var itemClickListener: OnItemClickListener
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_myreview, parent, false)
        return ViewHolder(view)
    }
    
    override fun getItemCount(): Int = reviewList.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]
        val bookInfo=bookInfo.getOrNull(position)
        holder.bind(review,bookInfo)
        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bookTitle: TextView = itemView.findViewById(R.id.book_title)
        private val reviewTitle: TextView = itemView.findViewById(R.id.review_title)
        private val reviewContents: TextView = itemView.findViewById(R.id.review_contents)
        private val bookImage: ImageView = itemView.findViewById(R.id.book_image)
        
        fun bind(review: ReviewResponse,book: Book?) {
            if (book != null) {
                bookTitle.text = book.title
                Glide.with(itemView.context).load(book.imageSrc).into(bookImage)
            } else {
                bookTitle.text = "로딩중"
                bookImage.setImageResource(R.drawable.placeholder_image) // Placeholder image resource
            }
            reviewTitle.text = review.title
            reviewContents.text = review.content
        }
    }
    
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    
    fun updateReview(newReview: List<ReviewResponse>,newBookInfo:List<Book>) {
        reviewList = newReview
        bookInfo=newBookInfo
        notifyDataSetChanged()
    }
}
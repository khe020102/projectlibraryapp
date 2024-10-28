package com.example.libraryapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.book.Aladin

data class AladinAdapter(private var aladins: List<Aladin>, private val context: Context) :
    RecyclerView.Adapter<AladinAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageButton = itemView.findViewById(R.id.book_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = aladins[position]
        Glide.with(holder.itemView.context).load(currentBook.cover).into(holder.bookImage)

        // book_image 버튼에 대한 클릭 이벤트 처리
        holder.bookImage.setOnClickListener {
            val url = currentBook.link
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return aladins.size
    }

    fun updateBooks(newBooks: List<Aladin>) {
        aladins = newBooks
        notifyDataSetChanged()
    }
}


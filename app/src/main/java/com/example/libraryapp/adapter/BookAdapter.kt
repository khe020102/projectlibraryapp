package com.example.libraryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.fragment.book.BookDetailFragment
import com.example.libraryapp.retrofit.book.Book

class BookAdapter(private var books: List<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageButton = itemView.findViewById(R.id.book_image)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = books[position]
        Glide.with(holder.itemView.context).load(currentBook.imageSrc).into(holder.bookImage)
        
        // book_image 버튼에 대한 클릭 이벤트 처리
        holder.bookImage.setOnClickListener {
            val context = holder.itemView.context
            val isbnNo = currentBook.isbnNo
            val fragment = BookDetailFragment.newInstance(isbnNo) // BookInformationFragment로 전환
            val transaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    
    override fun getItemCount(): Int {
        return books.size
    }
    
    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}


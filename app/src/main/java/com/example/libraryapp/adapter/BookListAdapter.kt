package com.example.libraryapp.adapter
// 리사이클러 뷰 어댑터 클래스
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.book.Book

class BookListAdapter(private var bookList: List<Book>) :
    RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    
    private var itemClickListener: OnItemClickListener? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }
    
    override fun getItemCount(): Int = bookList.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book)
        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(it, position)
        }
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tv_list_title)
        private val name: TextView = itemView.findViewById(R.id.tv_list_name)
        private val publish: TextView = itemView.findViewById(R.id.tv_list_publish)
        private val bookImage: ImageView = itemView.findViewById(R.id.img01)
        
        fun bind(book: Book) {
            title.text = book.title
            name.text = book.author
            publish.text = book.publish
            Glide.with(itemView.context).load(book.imageSrc).into(bookImage)
        }
    }
    
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    
    fun getBookAt(position: Int): Book = bookList[position]
    
    fun updateBooks(newBooks: List<Book>) {
        bookList = newBooks
        notifyDataSetChanged()
    }
}
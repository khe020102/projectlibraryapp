package com.example.libraryapp.fragment.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.book.Book
import com.example.libraryapp.retrofit.book.Favorite
import com.example.libraryapp.retrofit.book.FavoriteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookInformationFragment : Fragment() {
    private lateinit var isbnTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var publishTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var likedIcon: ImageButton
    
    private val bookApi = RetrofitClientInstance.bookApi
    private lateinit var bookInfo:Book
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookinformation, container, false)
        
        // UI 요소 초기화
        isbnTextView = view.findViewById(R.id.tv_list_ISBN)
        titleTextView = view.findViewById(R.id.tv_list_title)
        nameTextView = view.findViewById(R.id.tv_list_name)
        publishTextView = view.findViewById(R.id.tv_list_publish)
        yearTextView = view.findViewById(R.id.tv_list_year)
        likedIcon = view.findViewById(R.id.tv_list_liked)
        
        return view
    }
    
    fun setBook(book: Book) {
        bookInfo=book
        isbnTextView.text = bookInfo.isbnNo
        titleTextView.text = bookInfo.title
        nameTextView.text = bookInfo.author
        publishTextView.text = bookInfo.publish
        yearTextView.text = bookInfo.pubyear
        isLiked(bookInfo.isbnNo)
    }
    
    private fun isLiked(isbnNo: String) {
        bookApi.isBookFavorite(isbnNo).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val like = response.body()!!
                    if (like) {
                        likedIcon.setImageResource(R.drawable.favorite)
                        likedIcon.setOnClickListener { deleteLike(isbnNo) }
                    } else {
                        likedIcon.setImageResource(R.drawable.non_favorite)
                        likedIcon.setOnClickListener { addLike(isbnNo) }
                    }
                } else {
                    onFailure(call, Throwable("Failed to load book details"))
                }
            }
            
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "좋아요 정보를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    
    fun addLike(isbnNo: String) {
        val favorite = Favorite(isbnNo)
        bookApi.postFavorite(favorite).enqueue(object : Callback<FavoriteResponse> {
            override fun onResponse(
                call: Call<FavoriteResponse>,
                response: Response<FavoriteResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "좋아요 등록 완료",
                        Toast.LENGTH_SHORT
                    ).show()
                    refreshData()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "좋아요 등록 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            
            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "좋아요 정보를 등록하는 중 오류가 발생했습니다. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    
    fun deleteLike(isbnNo: String) {
        bookApi.deleteFavorite(isbnNo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "좋아요 삭제 완료",
                        Toast.LENGTH_SHORT
                    ).show()
                    refreshData()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "좋아요 삭제 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "좋아요 정보를 등록하는 중 오류가 발생했습니다. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    
    private fun refreshData() {
        // 데이터 다시 설정
        setBook(bookInfo)
    }
}

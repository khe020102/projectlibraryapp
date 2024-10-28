package com.example.libraryapp.fragment.mypage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.adapter.MyReviewAdapter
import com.example.libraryapp.databinding.FragmentMyreviewsBinding
import com.example.libraryapp.fragment.book.BookDetailFragment
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.book.Book
import com.example.libraryapp.retrofit.review.ReviewResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReviewFragment : Fragment() {
    private var myReviews: List<ReviewResponse> = emptyList()
    private var bookInfo: MutableList<Book> = mutableListOf()
    private val reviewApi=RetrofitClientInstance.reviewApi
    private val bookApi = RetrofitClientInstance.bookApi
    private lateinit var reviewListAdapter : MyReviewAdapter
    
    private var _binding: FragmentMyreviewsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyreviewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewListAdapter = MyReviewAdapter(myReviews, bookInfo)
        binding.favoriteList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteList.adapter = reviewListAdapter
        
        getMyReview()
        
        reviewListAdapter.setItemClickListener(object : MyReviewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val review = myReviews[position]
                val fragment = BookDetailFragment.newInstance(review.isbnNo)
                //책 상세정보 페이지로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }
    
    private fun getMyReview() {
        reviewApi.getMyReviews().enqueue(object : Callback<List<ReviewResponse>> {
            override fun onResponse(call: Call<List<ReviewResponse>>, response: Response<List<ReviewResponse>>) {
                if (response.isSuccessful) {
                    myReviews = response.body() ?: emptyList()
                    lifecycleScope.launch {
                        bookInfo = fetchBooksInfo(myReviews)
                        reviewListAdapter.updateReview(myReviews, bookInfo)
                    }
                }
            }
            override fun onFailure(call: Call<List<ReviewResponse>>, t: Throwable) {
                // 실패 처리 로직 추가
            }
        })
    }
    
    private suspend fun fetchBooksInfo(reviews: List<ReviewResponse>): MutableList<Book> {
        val books = mutableListOf<Book>()
        withContext(Dispatchers.IO) {
            for (review in reviews) {
                val response = bookApi.getBook(review.isbnNo).execute()
                if (response.isSuccessful) {
                    response.body()?.let { books.add(it) }
                }
            }
        }
        return books
    }
    
    private fun updateAdapter() {
        reviewListAdapter.updateReview(myReviews, bookInfo)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
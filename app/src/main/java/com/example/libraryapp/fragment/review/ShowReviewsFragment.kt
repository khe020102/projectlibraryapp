package com.example.libraryapp.fragment.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryapp.R
import com.example.libraryapp.adapter.ReviewAdapter
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.review.ReviewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowReviewsFragment : Fragment() {
    private val reviewApi = RetrofitClientInstance.reviewApi
    
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewTextViewResult: TextView
    
    private var isbnNo: String = ""
    private var reviewList: List<ReviewResponse> = listOf()
    
    companion object {
        const val ISBN_NO = "isbn_no"
        fun newInstance(isbnNo: String): ShowReviewsFragment {
            val fragment = ShowReviewsFragment()
            val args = Bundle()
            args.putString(ISBN_NO, isbnNo)
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isbnNo = it.getString(ISBN_NO, "")
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reviews, container, false)
        
        // 리사이클러뷰 초기화
        reviewRecyclerView = view.findViewById(R.id.listView_main_list)
        reviewTextViewResult = view.findViewById(R.id.reviewTextViewResult)
        
        reviewRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        reviewTextViewResult.movementMethod = ScrollingMovementMethod()
        
        // 리뷰 어댑터 설정
        reviewAdapter = ReviewAdapter(requireContext(), reviewList)
        reviewRecyclerView.adapter = reviewAdapter
        
        // 리뷰 데이터 가져오기
        fetchReview()
        // 리뷰 작성 버튼 클릭 리스너 설정
        val btnWriteReview: Button = view.findViewById(R.id.reviewwirteButton)
        btnWriteReview.setOnClickListener {
            // WriteReviewFragment로 이동하여 리뷰 작성
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, WriteReviewFragment.newInstance(isbnNo))
                .addToBackStack(null)
                .commit()
        }
        
        
        return view
    }
    
    private fun fetchReview() {
        reviewApi.getBookReviews(isbnNo).enqueue(object : Callback<List<ReviewResponse>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<ReviewResponse>>, response: Response<List<ReviewResponse>>) {
                if (response.isSuccessful) {
                    reviewList = response.body() ?: emptyList()
                    if (reviewList.isEmpty()){
                        reviewTextViewResult.text = "작성된 리뷰가 없습니다."
                    }
                    reviewAdapter.updateReviews(reviewList)
                } else {
                    // 서버 응답 오류 처리
                    reviewTextViewResult.text = "오류가 발생했습니다. 다시 시도해 주세요"
                }
            }
            
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<List<ReviewResponse>>, t: Throwable) {
                // 네트워크 오류 처리
                reviewTextViewResult.text = "네트워크 오류: ${t.message}"
            }
        })
    }
    
}

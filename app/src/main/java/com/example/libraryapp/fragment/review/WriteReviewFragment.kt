package com.example.libraryapp.fragment.review//package com.example.libraryapp
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.libraryapp.R
import com.example.libraryapp.fragment.book.BookDetailFragment
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.review.Review
import com.example.libraryapp.retrofit.review.ReviewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

////리뷰작성프래그먼트
class WriteReviewFragment : Fragment() {
    private val reviewApi = RetrofitClientInstance.reviewApi
    private var isbnNo: String = ""
    private var rate: Float = 3.0f
    
    private lateinit var editTextreview: EditText
    private lateinit var editTexttitle: EditText
    private lateinit var textViewResult: TextView
    private lateinit var dialog: AlertDialog
    
    companion object {
        const val ISBN_NO = "isbn_no"
        fun newInstance(isbnNo: String): WriteReviewFragment {
            val fragment = WriteReviewFragment()
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)
        
        textViewResult = view.findViewById(R.id.textView_result)
        textViewResult.movementMethod = ScrollingMovementMethod()
        
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        val okButton: Button = view.findViewById(R.id.okButton)
        val reviewRating: RatingBar = view.findViewById(R.id.reviewRating)
        
        editTexttitle = view.findViewById(R.id.titleEdit)
        editTextreview = view.findViewById(R.id.reviewEdit)
        
        
        cancelButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, BookDetailFragment.newInstance(isbnNo))
            transaction.addToBackStack(null)
            transaction.commit()
        }
        
        reviewRating.setOnRatingBarChangeListener { _, rating, _ ->
            rate = rating
        }
        
        okButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            if (editTexttitle.text.toString() == "" || editTextreview.text.toString() == "") {
                Toast.makeText(
                    requireContext(),
                    "모든 항복을 입력해 주세요",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val call = reviewApi.postReview(
                    Review(
                        title = editTexttitle.text.toString(),
                        isbnNo = isbnNo,
                        content = editTextreview.text.toString(),
                        score = rate.toString()
                    )
                )
                call.enqueue(object : Callback<ReviewResponse> {
                    override fun onResponse(
                        call: Call<ReviewResponse>,
                        response: Response<ReviewResponse>
                    ) {
                        if (response.isSuccessful) {
                            dialog =
                                builder.setMessage("리뷰작성이 완료되었습니다.")
                                    .setNegativeButton("확인") { _, _ ->
                                        // Review2Fragment로 이동
                                        val transaction =
                                            requireActivity().supportFragmentManager.beginTransaction()
                                        transaction.replace(
                                            R.id.main_container,
                                            BookDetailFragment.newInstance(isbnNo)
                                        )
                                        transaction.addToBackStack(null)
                                        transaction.commit()
                                    }.create()
                            dialog.show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "등록에 실패했습니다. 다시 시도해 주세요",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    
                    override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "네트워크 오류가 발생했습니다. 다시 시도해 주세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
        return view
    }
}

package com.example.libraryapp.fragment.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.fragment.review.ShowReviewsFragment
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.book.Book
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetailFragment : Fragment() {
    private var isbnNo: String = ""
    private val bookApi = RetrofitClientInstance.bookApi
    private lateinit var adapter: PagerAdapter
    
    companion object {
        const val ISBN_NO = "isbn_no"
        fun newInstance(isbnNo: String): BookDetailFragment {
            val fragment = BookDetailFragment()
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.page_layout, container, false)
        
        loadBookDetails()
        
        adapter = PagerAdapter(this)
        adapter.addFragment(BookInformationFragment(), "책 정보")
        adapter.addFragment(LocationFragment(), "위치")
        adapter.addFragment(ShowReviewsFragment.newInstance(isbnNo), "리뷰")
        
        val viewPager = view.findViewById<ViewPager2>(R.id.book_viewPager)
        viewPager.adapter = adapter
        
        val tabLayout = view.findViewById<TabLayout>(R.id.myPagetabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
        
        return view
    }
    
    private fun loadBookDetails() {
        bookApi.getBook(isbnNo).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    val book = response.body()!!
                    updateFragments(book)
                    updateUI(book)
                } else {
                    onFailure(call, Throwable("Failed to load book details"))
                }
            }
            
            override fun onFailure(call: Call<Book>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "책 정보를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    
    private fun updateFragments(book: Book) {
        (adapter.fragmentList[0] as? BookInformationFragment)?.setBook(book)
        (adapter.fragmentList[1] as? LocationFragment)?.setBook(book)
    }
    
    
    private fun updateUI(book: Book) {
        view?.let {
            val imageView = it.findViewById<ImageView>(R.id.img)
            Glide.with(this).load(book.imageSrc).into(imageView)
        }
    }
    
    class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()
        
        override fun getItemCount(): Int = fragmentList.size
        
        override fun createFragment(position: Int): Fragment = fragmentList[position]
        
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
        
        fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]
    }
}


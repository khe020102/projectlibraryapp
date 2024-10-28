package com.example.libraryapp.fragment.mypage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.adapter.BookListAdapter
import com.example.libraryapp.databinding.FragmentMyfavoriteBinding
import com.example.libraryapp.fragment.book.BookDetailFragment
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.book.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFavoriteFragment : Fragment() {
    private var favoriteBooks: List<Book> = emptyList()
    private val bookApi = RetrofitClientInstance.bookApi
    private val bookListAdapter = BookListAdapter(favoriteBooks)
    
    private var _binding: FragmentMyfavoriteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyfavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteList.adapter = bookListAdapter
        
        getFavoriteBook()
        
        bookListAdapter.setItemClickListener(object : BookListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val book = favoriteBooks[position]
                val fragment = BookDetailFragment.newInstance(book.isbnNo)
                //책 상세정보 페이지로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }
    
    private fun getFavoriteBook() {
        bookApi.getMyFavoriteBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    favoriteBooks = response.body() ?: emptyList()
                    bookListAdapter.updateBooks(favoriteBooks)
                }
            }
            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                // 실패 처리 로직 추가
            }
        })
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
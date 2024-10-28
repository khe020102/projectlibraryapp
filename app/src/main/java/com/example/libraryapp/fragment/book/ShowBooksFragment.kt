package com.example.libraryapp.fragment.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.adapter.AladinAdapter
import com.example.libraryapp.adapter.BookAdapter
import com.example.libraryapp.databinding.FragmentBooksBinding
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.book.Aladin
import com.example.libraryapp.retrofit.book.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowBooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private lateinit var recommendAdapter: BookAdapter
    private lateinit var popularAdapter: BookAdapter
    private lateinit var aladinAdapter: AladinAdapter

    private var recommendedBooks: List<Book> = listOf()
    private var popularBooks: List<Book> = listOf()
    private var newBooks: List<Aladin> = listOf()

    private val bookApi = RetrofitClientInstance.bookApi
    private val aladinRepository = RetrofitClientInstance.aladinApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupSpinner(binding.spinnerFilter)
        fetchBooks()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recommendAdapter = BookAdapter(recommendedBooks)
        popularAdapter= BookAdapter(popularBooks)
        aladinAdapter = AladinAdapter(newBooks, requireContext())
        binding.recyclerView.adapter = recommendAdapter
    }

    private fun setupSpinner(spinner: Spinner) {
        val options = listOf("AI 추천도서", "인기도서", "신간도서")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.recyclerView.adapter = recommendAdapter
                        recommendAdapter.updateBooks(recommendedBooks)
                    } // AI 추천도서
                    1 -> {
                        binding.recyclerView.adapter = popularAdapter
                        popularAdapter.updateBooks(popularBooks)
                    } // 인기도서
                    2 -> {
                        binding.recyclerView.adapter = aladinAdapter
                        aladinAdapter.updateBooks(newBooks)
                    }// 신간도서
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun fetchBooks() {
        // 추천 도서 가져오기
        bookApi.getRecommendBook().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    recommendedBooks = response.body() ?: listOf()
                    recommendAdapter.updateBooks(recommendedBooks)
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                // 오류 처리
            }
        })

        // 인기 도서 가져오기
        bookApi.getPopularBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    popularBooks = response.body() ?: listOf()
                    popularAdapter.updateBooks(recommendedBooks)
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                // 오류 처리
            }
        })

        // 신간 도서 가져오기
        aladinRepository.newbook().enqueue(object : Callback<List<Aladin>> {
            override fun onResponse(call: Call<List<Aladin>>, response: Response<List<Aladin>>) {
                if (response.isSuccessful) {
                    newBooks = response.body() ?: listOf()
                    aladinAdapter.updateBooks(newBooks)
                }
            }

            override fun onFailure(call: Call<List<Aladin>>, t: Throwable) {
                // 오류 처리
            }
        })
    }
}

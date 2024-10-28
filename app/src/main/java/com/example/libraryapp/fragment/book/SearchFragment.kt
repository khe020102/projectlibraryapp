package com.example.libraryapp.fragment.book

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libraryapp.R
import com.example.libraryapp.adapter.BookListAdapter
import com.example.libraryapp.databinding.FragmentSearchBinding
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.book.Book
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var searchResult: List<Book> = emptyList()   // 리사이클러 뷰 아이템
    private var keyword = ""        // 검색 키워드
    private var pageNumber = 1      // 검색 페이지 번호

    private var _binding: FragmentSearchBinding? = null
    private var bookListAdapter: BookListAdapter? = null    // 리사이클러 뷰 어댑터
    private val bookApi = RetrofitClientInstance.bookApi
    private lateinit var pageButtons: MutableList<Button>
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        pageButtons = mutableListOf()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화된 어댑터 설정
        bookListAdapter = BookListAdapter(searchResult)

        // 리사이클러 뷰
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = bookListAdapter

        // 리스트 아이템 클릭 시 해당 위치로 이동
        bookListAdapter?.setItemClickListener(object : BookListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val book = bookListAdapter?.getBookAt(position)
                val fragment = BookDetailFragment.newInstance(book!!.isbnNo)

                // 책 상세정보 페이지로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment) // add 대신 replace 사용
                    .addToBackStack("SearchFragment")
                    .commit()
            }
        })

        // 검색 버튼
        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        // 키워드 입력창에서 엔터키 입력 시 검색 수행
        binding.etSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performSearch() {
        keyword = binding.etSearchField.text.toString().trim()
        if (keyword.isNotEmpty()) {
            searchKeyword(keyword)
        } else {
            Snackbar.make(binding.root, "검색어를 입력하세요.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updatePageButtons() {
        val totalPages = (searchResult.size + 9) / 10  // 총 페이지 수 계산

        // 현재 페이지 그룹 계산
        val currentGroup = (pageNumber - 1) / 10
        val startPage = currentGroup * 10 + 1
        val endPage = minOf(startPage + 9, totalPages)

        // 기존 페이지 버튼들을 모두 제거
        binding.layoutPageButtons.removeAllViews()
        pageButtons.clear()

        // 처음 페이지 버튼 추가
        if (totalPages > 1) {
            val firstPageButton = createPageButton("<<<", 1)
            binding.layoutPageButtons.addView(firstPageButton)
        }

        // 이전 그룹 버튼 추가
        if (startPage > 1) {
            val prevGroupButton = createPageButton("<<", startPage - 1)
            binding.layoutPageButtons.addView(prevGroupButton)
        }

        // 이전 페이지 버튼 추가
        if (pageNumber > 1) {
            val prevPageButton = createPageButton("◀", pageNumber - 1)
            binding.layoutPageButtons.addView(prevPageButton)
        }

        // 새로운 페이지 버튼들을 추가
        for (i in startPage..endPage) {
            val button = createPageButton(i.toString(), i)
            if (i == pageNumber) {
                button.setTypeface(null, Typeface.BOLD)
            }
            binding.layoutPageButtons.addView(button)
            pageButtons.add(button)
        }

        // 다음 페이지 버튼 추가
        if (pageNumber < totalPages) {
            val nextPageButton = createPageButton("▶", pageNumber + 1)
            binding.layoutPageButtons.addView(nextPageButton)
        }

        // 다음 그룹 버튼 추가
        if (endPage < totalPages) {
            val nextGroupButton = createPageButton(">>", endPage + 1)
            binding.layoutPageButtons.addView(nextGroupButton)
        }

        // 마지막 페이지 버튼 추가
        if (totalPages > 1) {
            val lastPageButton = createPageButton(">>>", totalPages)
            binding.layoutPageButtons.addView(lastPageButton)
        }
    }

    private fun createPageButton(text: String, pageNumber: Int): Button {
        return Button(requireContext(), null, 0, R.style.SmallPageButton).apply {
            this.text = text
            setOnClickListener { navigateToPage(pageNumber) }
        }
    }

    private fun navigateToPage(pageNumber: Int) {
        this.pageNumber = pageNumber
        viewSearch()
    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String) {
        bookApi.searchBook(keyword).enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    searchResult = response.body() ?: emptyList()
                    viewSearch()
                } else {
                    Snackbar.make(binding.root, "검색에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Snackbar.make(binding.root, "네트워크 오류가 발생했습니다.", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun viewSearch() {
        val start = (pageNumber - 1) * 10
        val end = (pageNumber * 10).coerceAtMost(searchResult.size)
        val sliced = if (start in searchResult.indices) {
            searchResult.slice(start until end)
        } else {
            emptyList()
        }
        bookListAdapter!!.updateBooks(sliced)

        // 페이지 버튼 업데이트
        updatePageButtons()

        // 검색 결과가 없을 때 처리
        if (searchResult.isEmpty()) {
            Snackbar.make(binding.root, "검색 결과가 없습니다.", Snackbar.LENGTH_SHORT).show()
        }
    }
}

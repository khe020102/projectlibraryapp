package com.example.libraryapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.libraryapp.R
import com.example.libraryapp.databinding.ActivityHomeBinding
import com.example.libraryapp.fragment.book.SearchFragment
import com.example.libraryapp.fragment.book.ShowBooksFragment
import com.example.libraryapp.fragment.mypage.MypageFragment

class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_books
        }
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_books -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, ShowBooksFragment()).commit()
                    true
                }
                R.id.fragment_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, SearchFragment()).commit()
                    true
                }
                R.id.fragment_mypage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, MypageFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
    
}

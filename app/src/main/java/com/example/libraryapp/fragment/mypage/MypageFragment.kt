package com.example.libraryapp.fragment.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.libraryapp.MainActivity
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentMypageBinding
import com.example.libraryapp.retrofit.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {
    
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    // UserApi 인스턴스 생성
    private val userApi = RetrofitClientInstance.userApi
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnMyReview.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MyReviewFragment())
                .addToBackStack(null)
                .commit()
        }
        
        binding.btnMyFavorite.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MyFavoriteFragment())
                .addToBackStack(null)
                .commit()
        }
        
        binding.btnLogout.setOnClickListener {
            logoutAndNavigateToMainActivity()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun logoutAndNavigateToMainActivity() {
        userApi.logout().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // 로그아웃 요청이 실패한 경우 처리
                    Toast.makeText(requireContext(), "Logout failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 네트워크 오류 등 처리
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



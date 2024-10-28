package com.example.libraryapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libraryapp.retrofit.user.LoginData
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.RetrofitClientInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: ImageButton
    private lateinit var editTextId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnRegister: Button
    private var userApi = RetrofitClientInstance.userApi
    private var bookApi = RetrofitClientInstance.bookApi
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // RetrofitClientInstance에서 UserApi 불러오는 부분 추가
        
        btnLogin = findViewById(R.id.btnLogin)
        editTextId = findViewById(R.id.editTextId)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnRegister = findViewById(R.id.btnRegister)
        
        // 로그인 버튼 클릭
        btnLogin.setOnClickListener {
            val user = editTextId.text.toString()
            val pass = editTextPassword.text.toString()
            
            // 빈칸 제출시 Toast
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this@LoginActivity, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val loginData =
                            LoginData(username = user, password = pass) // LoginData 객체 생성
                        userApi.login(loginData) // 수정된 부분
                        // 로그인 성공시
                        Toast.makeText(this@LoginActivity, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        try {
                            bookApi.getRecommendBook()
                            intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        } catch (e: Exception) {
                            intent = Intent(this@LoginActivity, Register2Activity::class.java)
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@LoginActivity,
                            "아이디와 비밀번호를 확인해 주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }
                }
            }
        }
        // 회원가입 버튼 클릭시
        btnRegister.setOnClickListener {
            val loginIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(loginIntent)
        }
    }
}
package com.example.libraryapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.libraryapp.MainActivity
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var editTextId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRePassword: EditText
    private lateinit var editTextNick: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var btnRegister: ImageButton
    private lateinit var btnCheckId: Button
    private lateinit var btnCheckNick: Button
    
    private var checkId: Boolean = false
    private var checkNick: Boolean = false
    
    private val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,15}$"
    private val phonePattern = "^(\\+[0-9]+)?[0-9]{10,15}$"
    
    private val api = RetrofitClientInstance.userApi
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        editTextId = findViewById(R.id.editTextId_Reg)
        editTextPassword = findViewById(R.id.editTextPass_Reg)
        editTextRePassword = findViewById(R.id.editTextRePass_Reg)
        editTextNick = findViewById(R.id.editTextNick_Reg)
        editTextPhone = findViewById(R.id.editTextPhone_Reg)
        btnRegister = findViewById(R.id.btnRegister_Reg)
        btnCheckId = findViewById(R.id.btnCheckId_Reg)
        btnCheckNick = findViewById(R.id.btnCheckNick_Reg)
        
        // 학번 중복확인 버튼 클릭 이벤트 처리
        btnCheckId.setOnClickListener {
            val studentNumber = editTextId.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = api.checkStudentNumber(studentNumber)
                    if (!response) {
                        checkId = true
                        tostMessage("사용 가능한 학번입니다")
                    } else {
                        tostMessage("이미 존재하는 학번입니다:")
                    }
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Error occurred: ${e.message}")
                    tostMessage("학번 중복확인 중 오류가 발생했습니다.")
                }
            }
        }
        
        // 닉네임 중복확인 버튼 클릭 이벤트 처리
        btnCheckNick.setOnClickListener {
            val nickname = editTextNick.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = api.checkNickname(nickname)
                    if (!response) {
                        checkNick = true
                        tostMessage("사용 가능한 닉네임입니다.")
                    } else {
                        tostMessage("이미 존재하는 닉네임입니다.")
                    }
                } catch (e: Exception) {
                    tostMessage("닉네임 중복확인 중 오류가 발생했습니다.")
                }
            }
        }
        
        // 완료 버튼 클릭 이벤트 처리
        btnRegister.setOnClickListener {
            val studentID = editTextId.text.toString()
            val password = editTextPassword.text.toString()
            val repassword = editTextRePassword.text.toString()
            val nickname = editTextNick.text.toString()
            val phoneNumber = editTextPhone.text.toString()
            
            // 회원가입 로직 수행
            if (studentID.isEmpty() || password.isEmpty() || repassword.isEmpty() || nickname.isEmpty() || phoneNumber.isEmpty()) {
                tostMessage("회원정보를 모두 입력해주세요.")
            } else if (!checkId) {
                tostMessage("아이디 중복확인을 해주세요.")
            } else if (!checkNick) {
                tostMessage("닉네임 중복확인을 해주세요.")
            } else if (!Pattern.matches(pwPattern, password)) {
                tostMessage("비밀번호 형식이 옳지 않습니다.")
            } else if (password != repassword) {
                tostMessage("비밀번호가 일치하지 않습니다.")
            } else if (!Pattern.matches(phonePattern, phoneNumber)) {
                tostMessage("전화번호 형식이 옳지 않습니다.")
            } else {
                // 회원 가입 정보 서버로 전송
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        // authorities 매개변수에 빈 리스트를 전달
                        api.signup(
                            User(
                                studentID = studentID,
                                password = password,
                                nickname = nickname,
                                phoneNumber = phoneNumber
                            )
                        )
                        tostMessage("가입되었습니다.")
                        val intent = Intent(
                            applicationContext,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                        
                    } catch (e: Exception) {
                        Log.e(
                            "RegisterActivity",
                            "Error occurred during signup: ${e.message}"
                        )
                        tostMessage("회원가입에 실패했습니다.")
                    }
                }
            }
        }
    }
    
    private fun tostMessage(m: String) {
        Toast.makeText(this@RegisterActivity, m, Toast.LENGTH_SHORT).show()
    }
}

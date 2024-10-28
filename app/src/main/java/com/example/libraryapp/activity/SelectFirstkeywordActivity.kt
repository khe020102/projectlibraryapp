package com.example.libraryapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libraryapp.R

class Register2Activity : AppCompatActivity() {

    private lateinit var keywordListView: ListView
    private lateinit var completeButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        // 키워드 목록 생성
        val keywords = arrayOf("문학", "역사", "과학", "예술", "철학", "사회", "기술", "종교", "자연", "건강")

        // 키워드 리스트뷰 초기화 및 어댑터 설정
        keywordListView = findViewById(R.id.keywordListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, keywords)
        keywordListView.adapter = adapter

        // 완료 버튼 클릭 리스너 설정
        completeButton = findViewById(R.id.completeButton)
        completeButton.setOnClickListener {
            val selectedKeywords = getSelectedKeywords()
            if (selectedKeywords.size == 1) {
                // 선택된 키워드 처리
                handleSelectedKeywords(selectedKeywords)
                // Register3Activity로 이동
                val intent = Intent(this, Register3Activity::class.java)
                intent.putExtra("selectedKeyword", selectedKeywords[0]) // 선택된 키워드를 전달
                startActivity(intent)
                finish() // 현재 액티비티 종료
            } else {
                Toast.makeText(this, "키워드를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 선택된 키워드 가져오기
    private fun getSelectedKeywords(): List<String> {
        val selectedKeywords = mutableListOf<String>()
        val positions = keywordListView.checkedItemPositions
        for (i in 0 until positions.size()) {
            val position = positions.keyAt(i)
            if (positions.valueAt(i)) {
                selectedKeywords.add(keywordListView.adapter.getItem(position) as String)
            }
        }
        return selectedKeywords
    }

    // 선택된 키워드 처리
    private fun handleSelectedKeywords(selectedKeywords: List<String>) {
        // 선택된 키워드에 대한 처리를 여기에 구현하세요
        // 예를 들어, 선택된 키워드를 로그에 출력하거나 다음 화면으로 전환할 수 있습니다.
        val message = "선택된 키워드: ${selectedKeywords.joinToString(", ")}"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

package com.example.libraryapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.RetrofitClientInstance
import com.example.libraryapp.retrofit.book.Keyword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Register3Activity : AppCompatActivity() {

    private lateinit var keywordListView: ListView
    private lateinit var completeButton: ImageButton
    private val bookApi = RetrofitClientInstance.bookApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register3)

        // keywordListView 초기화
        keywordListView = findViewById(R.id.keywordListView)

        val intent = intent
        val selectedKeyword = intent.getStringExtra("selectedKeyword")

        // 키워드 목록 생성
        val keywordList = when (selectedKeyword) {
            "문학" -> listOf("고전문학", "현대문학", "시", "소설", "에세이", "희곡", "전기문학", "단편소설", "수필", "산문")
            "역사" -> listOf("고대사", "중세사", "근대사", "현대사", "세계사", "한국사", "유럽사", "아시아사", "미국사", "전쟁사")
            "과학" -> listOf("물리학", "화학", "생물학", "지구과학", "천문학", "환경과학", "공학", "기술", "과학사", "과학철학")
            "예술" -> listOf("미술", "음악", "영화", "연극", "사진", "조각", "디자인", "무용", "문학예술", "건축")
            "철학" -> listOf("고대철학", "중세철학", "근대철학", "현대철학", "윤리학", "논리학", "형이상학", "인식론", "정치철학", "미학")
            "사회" -> listOf("사회학", "정치학", "경제학", "인류학", "심리학", "교육학", "법학", "행정학", "국제관계학", "사회문제")
            "기술" -> listOf("컴퓨터", "인터넷", "소프트웨어", "하드웨어", "프로그래밍", "네트워크", "데이터베이스", "보안", "AI", "로봇")
            "종교" -> listOf("기독교", "불교", "이슬람교", "힌두교", "유교", "유대교", "신흥종교", "종교철학", "종교사", "종교사회학")
            "자연" -> listOf("자연사", "생태학", "동물학", "식물학", "지질학", "해양학", "기후학", "지리학", "자연재해", "자연보호")
            "건강" -> listOf("의학", "한의학", "치의학", "간호학", "영양학", "운동학", "정신건강", "질병", "건강관리", "약학")
            // 다른 키워드에 따른 목록 추가
            else -> listOf("기본 목록1", "기본 목록2", "기본 목록3")
        }

        // 어댑터 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, keywordList)
        keywordListView.adapter = adapter

        // 완료 버튼 클릭 리스너 설정
        completeButton = findViewById(R.id.completeButton)
        completeButton.setOnClickListener {
            val selectedKeywords = getSelectedKeywords()
            if (selectedKeywords.size == 5) {
                // 선택된 키워드 처리
                handleSelectedKeywords(selectedKeywords)
                // 키워드 서버에 전달
                postKeywordsToServer(selectedKeywords)
            } else {
                Toast.makeText(this, "5가지 키워드를 선택해주세요.", Toast.LENGTH_SHORT).show()
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

    // 키워드 서버에 전달
    private fun postKeywordsToServer(selectedKeywords: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val keyword= Keyword("s", selectedKeywords.joinToString(",") )
                bookApi.postKeywords(keyword)
                val intent = Intent(this@Register3Activity, HomeActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@Register3Activity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

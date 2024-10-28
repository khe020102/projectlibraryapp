package com.example.libraryapp.fragment.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.libraryapp.R
import com.example.libraryapp.retrofit.book.Book

class LocationFragment : Fragment() {
    private lateinit var tableLayout: TableLayout
    
    private var book: Book? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)
        tableLayout = view.findViewById(R.id.table1)
        book?.let { setBook(it) }
        return view
    }
    
    fun setBook(book: Book) {
        this.book = book
        if (this::tableLayout.isInitialized) {
            val numList = book.num.split("|")
            val locationList = book.location.split("|")
            for (i in numList.indices) {
                addTableRow(numList[i], locationList[i])
            }
        }
    }
    
    private fun addTableRow(num:String,location:String) {
        val tableRow = TableRow(context)
        
        val numTextView = TextView(context).apply {
            text = num
            layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            gravity = View.TEXT_ALIGNMENT_CENTER
        }
        
        val locationTextView = TextView(context).apply {
            text = location
            layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            gravity = View.TEXT_ALIGNMENT_CENTER
        }
        
        tableRow.addView(numTextView)
        tableRow.addView(locationTextView)
        
        tableLayout.addView(tableRow)
    }
}
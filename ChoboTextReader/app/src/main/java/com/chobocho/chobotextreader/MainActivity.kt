package com.chobocho.chobotextreader

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import com.chobocho.dbmanager.BookManager
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemClickListener
import android.widget.SearchView

class MainActivity : AppCompatActivity() {
    val TAG = "[eBook] " + MainActivity::class.simpleName
    lateinit var bookManger : BookManager
    var booklist : ArrayList<String> = ArrayList()
    lateinit var searchView : SearchView
    lateinit var ebookListView : ListView
    lateinit var bookListAdpater: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBookManager()

        setContentView(R.layout.activity_main)
        initUI()
    }

    fun initBookManager() {
        bookManger = BookManager(this)
        updateBookList("")
    }

    fun updateBookList(query : String) {
        booklist.clear()
        bookManger.getEbookList(query)!!.stream().forEach { e -> booklist.add(e) }
        booklist.sort()
    }

    fun initUI() {
        ebookListView = findViewById(R.id.ebookListView)
        bookListAdpater = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,booklist)
        ebookListView.setAdapter(bookListAdpater)
        ebookListView.setOnItemClickListener(OnItemClickListener { adapterView, _, position, _ ->
            val title = adapterView.getItemAtPosition(position) as String
            displaySelectedText(title)
        })

        searchView = findViewById(R.id.ebooSearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updateBookList(query!!)
                bookListAdpater.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateBookList(newText!!)
                bookListAdpater.notifyDataSetChanged()
                return false
            }
        })
    }

    fun displaySelectedText(title:String) {
        var bookText = bookManger.getBookText(title)

        val message = bookText.toString()
        val intent = Intent(this, DisplayTextActivity::class.java).apply {
            putExtra(SHOW_TEXT_MESSAGE, message)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        if (booklist.isEmpty()) {
            updateBookList("")
            bookListAdpater.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        booklist.clear()
        bookListAdpater.notifyDataSetChanged()
        super.onPause()
    }

    companion object {
        const val SHOW_TEXT_MESSAGE = "com.chobocho.ebook.SHOW_EBOOK"
    }
}
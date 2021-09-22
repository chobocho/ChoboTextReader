package com.chobocho.chobotextreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView

class DisplayTextActivity : AppCompatActivity() {
    val TAG = "[eBook] " + DisplayTextActivity::class.simpleName
    lateinit var textView : TextView
    var textData : String = ""
    var textPos : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_text)

        val message = intent.getStringExtra(SHOW_TEXT_MESSAGE)
        val msgSize = message.toString().length
        textData = message.toString()

        Log.i(TAG, "Size: " + msgSize)

        textView = findViewById(R.id.textView)
        textView.setMovementMethod(ScrollingMovementMethod())

        var seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb: SeekBar?, p1: Int, p2: Boolean) {
                if (msgSize < 1000) {
                    return
                }

                textPos = Integer.parseInt(sb?.progress.toString())
                textPos = if (textPos < 100) textPos else 99
                var target : Int = msgSize * textPos / 100
                target = if (target > 200) target else 200

                textView.text = message!!.subSequence(target, msgSize)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (textPos <= 0 || textData.length < 1000) {
            textView.text = textData
        } else {
            val msgSize = textData.length
            var target: Int = msgSize * textPos / 100
            target = if (target > 200) target else 200
            textView.text = textData.subSequence(target, msgSize)
        }
    }

    override fun onPause() {
        textView.text = ""
        super.onPause()
    }

    companion object {
        const val SHOW_TEXT_MESSAGE = "com.chobocho.ebook.SHOW_EBOOK"
    }
}
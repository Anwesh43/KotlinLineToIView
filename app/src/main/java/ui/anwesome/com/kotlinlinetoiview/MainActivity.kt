package ui.anwesome.com.kotlinlinetoiview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.linetoiview.LineToIView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LineToIView.create(this)
    }
}

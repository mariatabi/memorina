package com.example.makelayout

import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT)

        params.weight = 1.toFloat() // единичный вес

        // TODO: 3) реализовать переворот карт с "рубашки" на лицевую сторону и обратно

        val points = arrayOf(R.drawable.ic_heart,R.drawable.ic_light,R.drawable.ic_plane,R.drawable.ic_smiley,
            R.drawable.squarecat, R.drawable.bee, R.drawable.butterfly
        )

        var firstCard: View = View(this)
        var openCards = 0 // // число открытых карт
        var RightCount = 0 //matched cards

        val catViews = ArrayList<ImageView>()
        points.shuffle()

        val colorListener = View.OnClickListener() {
            when (openCards) {
                0 -> { // перевернуть карту
                    firstCard = it; openCards++; //it.isClickable = false
                    GlobalScope.launch (Dispatchers.Main) {
                        flip(firstCard as ImageView, points[it.tag.toString().toInt()])
                    }
                }
                1 -> {
                    // проверить, совпадает ли перевёрнутая сейчас карта
                    if (it.tag == firstCard.tag) {
                        GlobalScope.launch (Dispatchers.Main) {
                            openCards++;

                            flip(it as ImageView,points[it.tag.toString().toInt()])
                            delay(1000)
                            hide(it as ImageView)
                            hide(firstCard as ImageView)
                            openCards = 0
                            RightCount += 1
                            if (RightCount == points.size){
                                val text = "WIN"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(applicationContext, text, duration)
                                toast.show()
                            }
                        }
                    } else {
                        // карту перевернуть
                        // подождать, вернуть обратно обе карты
                        // не забыть включить карты it.isClickable = true
                        GlobalScope.launch(Dispatchers.Main) {
                            openCards++;
                            flip(it as ImageView, points[it.tag.toString().toInt()])
                            delay(1000)
                            flip(firstCard as ImageView, R.drawable.ic_code)
                            flip(it as ImageView, R.drawable.ic_code)
                            openCards = 0
                        }
                    }
                }
                // и перевёрнутая ранее
                else -> Log.d("mytag", "two cards are open already") // ничего не делаем
            }
            // запуск функции во внешнем потоке
//            GlobalScope.launch (Dispatchers.Main)
//            { setBackgroundWithDelay(it) }
            //it.setBackgroundColor(Color.YELLOW)
        }

        val catViews = ArrayList<ImageView>()

        // TODO: 2) случайным образом разместить 8 пар картинок

        for (i in 1..16) {
            catViews.add( // вызываем конструктор для создания нового ImageView
                ImageView(applicationContext).apply {
                    setImageResource(R.drawable.ic_code)
                    layoutParams = params
                    tag = (i-1)/2 // TODO: указать тег в зависимости от картинки
                    setOnClickListener(colorListener)
                })
        }

        catViews.shuffle()

        val rows = Array(4, { LinearLayout(applicationContext)})
        var count = 0

        for (view in catViews) {
            val row: Int = count / 4
            rows[row].addView(view)
            count ++
        }

        for (row in rows) {
            row.apply{
                orientation = LinearLayout.HORIZONTAL
                layoutParams = params
            }
            layout.addView(row)
        }
            // TODO: 1) заполнить 4 строки элементами из массива catViews по 4 штуки в ряду
        //  /*
               val cat2 = ImageView(applicationContext)
               cat2.setImageResource(R.drawable.squarecat); cat2.layoutParams = params
               val cat3 = ImageView(applicationContext)
               cat3.setImageResource(R.drawable.squarecat)
               val cat4 = ImageView(applicationContext)
               //cat.layoutParams = ViewGroup.LayoutParams(applicationContext, )
               cat4.setImageResource(R.drawable.squarecat)
               val row1 = LinearLayout(applicationContext)
               row1.orientation = LinearLayout.HORIZONTAL
               row1.setBackgroundColor(Color.GRAY)
               row1.addView(cat2); row1.addView(cat);
               val row2 = LinearLayout(applicationContext)
               row2.orientation = LinearLayout.HORIZONTAL
               row2.setBackgroundColor(Color.GRAY)
               row2.addView(cat3); row2.addView(cat4);
               layout.addView(row1); layout.addView(row2)
         */

        setContentView(layout)
    }

    suspend fun setBackgroundWithDelay(v: View) {
        delay(1000)
        v.setBackgroundColor(Color.YELLOW)
        delay(1000)
        v.visibility = View.INVISIBLE
        v.isClickable = false
    }


    suspend fun flip(v: ImageView,r: Int) {
        v.setImageResource(r)
        v.isClickable = !v.isClickable
    }

    suspend fun hide(v: ImageView) {
        v.visibility = View.INVISIBLE
        v.isClickable = false
    }
}
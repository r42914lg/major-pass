package com.r42914lg.major

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.r42914lg.major.model.Car
import com.r42914lg.major.model.Visitor
import com.r42914lg.major.ui.Visitors
import com.r42914lg.major.ui.theme.MajorTheme
import kotlin.collections.listOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MajorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Visitors(
                        modifier = Modifier.padding(innerPadding),
                        visitors = listOf(
                                Visitor(
                                    "Кац",
                                    listOf(
                                        Car("Мерседес", "х156уя197"),
                                        Car("Ауди", "е996ен150"),
                                    )
                                ),
                            Visitor(
                                "Мойша",
                                listOf(
                                    Car("Жигули", "м0248мм"),
                                )
                            ),
                            Visitor(
                                "Бегемот",
                                listOf(
                                    Car("Форд", "к7208мо"),
                                )
                            ),
                        )
                    )
                }
            }
        }
    }
}
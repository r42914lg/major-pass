package com.r42914lg.major

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.r42914lg.major.model.Visitor
import com.r42914lg.major.ui.Visitors
import com.r42914lg.major.ui.theme.MajorTheme

class MainActivity : ComponentActivity() {
    private val mainStateHolder: MainStateHolder by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MajorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            mainStateHolder.onScreenEvent(
                                ScreenEvent.AddVisitor(
                                    Visitor(name = "Новый игрок")
                                )
                            )
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "Новый игрок")
                        }
                    }
                ) { innerPadding ->
                    Visitors(
                        mainStateHolder = mainStateHolder,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
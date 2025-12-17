package com.r42914lg.major

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.r42914lg.major.model.Visitor
import com.r42914lg.major.ui.Details
import com.r42914lg.major.ui.Navigator
import com.r42914lg.major.ui.RouteDetails
import com.r42914lg.major.ui.RouteListing
import com.r42914lg.major.ui.Visitors
import com.r42914lg.major.ui.rememberNavigationState
import com.r42914lg.major.ui.theme.MajorTheme
import com.r42914lg.major.ui.toEntries

class MainActivity : ComponentActivity() {
    private val mainStateHolder: MainStateHolder by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationState = rememberNavigationState(
                startRoute = RouteListing,
                topLevelRoutes = setOf(RouteListing)
            )
            val navigator = remember { Navigator(navigationState) }
            val entryProvider = entryProvider {
                entry<RouteListing>{ Visitors(mainStateHolder) {
                    navigator.navigate(RouteDetails(it))
                } }
                entry<RouteDetails>{ key -> Details(
                        data = key.data,
                        onEditComplete = {
                            mainStateHolder.onScreenEvent(ScreenEvent.AddVisitor(it))
                            navigator.goBack()
                        },
                        onNavigateBack = {
                            navigator.goBack()
                        }
                    )
                }
            }


            MajorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            mainStateHolder.onScreenEvent(ScreenEvent.AddVisitor(Visitor()))
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Visitor")
                        }
                    }
                ) { innerPadding ->
                    NavDisplay(
                        entries = navigationState.toEntries(entryProvider),
                        onBack = { navigator.goBack() },
                        sceneStrategy = remember { DialogSceneStrategy() }
                    )
                }
            }
        }
    }
}
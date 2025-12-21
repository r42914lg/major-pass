package com.r42914lg.major

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
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
                entry<RouteListing>{
                    Visitors(
                        mainStateHolder = mainStateHolder,
                        intentParam = intent.getStringExtra(INTENT_PARAM_KEY)
                    ) {
                        navigator.navigate(RouteDetails(it))
                    }
                }
                entry<RouteDetails>{ key -> Details(
                        data = key.data,
                        onEditComplete = {
                            mainStateHolder.onScreenEvent(ScreenEvent.EditVisitor(it))
                            navigator.goBack()
                        },
                        onNavigateBack = {
                            navigator.goBack()
                        }
                    )
                }
            }
            MajorTheme {
                NavDisplay(
                    entries = navigationState.toEntries(entryProvider),
                    onBack = { navigator.goBack() },
                    sceneStrategy = remember { DialogSceneStrategy() }
                )
            }
        }
    }

    companion object {
        const val INTENT_PARAM_KEY = "intent_param_key"
    }
}
package com.r42914lg.major.ui

import android.content.ClipData
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.r42914lg.major.MainStateHolder
import com.r42914lg.major.R
import com.r42914lg.major.ScreenEvent
import com.r42914lg.major.model.Car
import com.r42914lg.major.model.Visitor
import com.r42914lg.major.model.toClipboardText
import kotlinx.coroutines.launch

@Composable
fun Visitors(
    mainStateHolder: MainStateHolder,
    modifier: Modifier = Modifier,
    onVisitorClick: (Visitor) -> Unit = {},
) {
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()

    val state by mainStateHolder.screenState.collectAsStateWithLifecycle()
    val canShare by remember {
        derivedStateOf {
            state.count { it.isSelected } > 0
        }
    }

    val clipLabel = stringResource(R.string.clip_label)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
            ) {
                Text(
                    text = "Paid Version - Exclusive Features",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = canShare,
                    onClick = {
                        scope.launch {
                            clipboardManager.setClipEntry(
                                ClipEntry(
                                    ClipData.newPlainText(
                                        clipLabel,
                                        state.toClipboardText()
                                    )
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.copy_to_clipboard))
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                mainStateHolder.onScreenEvent(ScreenEvent.AddVisitor)
            }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_visitor_description))
            }
        }
    ) { paddingValues ->
        if (state.isNotEmpty()) {
            LazyColumn(Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, bottom = 100.dp)) {
                items(state.size, key = { state[it].id }) { index ->
                    VisitorCard(
                        modifier = Modifier.clickable {
                            onVisitorClick(state[index])
                        },
                        visitor = state[index],
                        onDelete = {
                            mainStateHolder.onScreenEvent(ScreenEvent.RemoveVisitor(it))
                        },
                        onVisitorSelected = {
                            mainStateHolder.onScreenEvent(ScreenEvent.SelectVisitor(it))
                        },
                        onCarSelected = { car, visitor ->
                            mainStateHolder.onScreenEvent(ScreenEvent.SelectCar(car, visitor))
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.add_tennis_player))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitorCard(
    modifier: Modifier = Modifier,
    visitor: Visitor = Visitor(),
    onDelete: (Visitor) -> Unit = {},
    onVisitorSelected: (Visitor) -> Unit = {},
    onCarSelected: (Car, Visitor) -> Unit = { _, _ -> },
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete(visitor)
                true
            } else {
                false
            }
        },
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier.padding(vertical = 4.dp),
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.Transparent
                },
                label = "background color"
            )
            val alignment = Alignment.CenterEnd
            val icon = Icons.Default.Delete
            val scale by animateFloatAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
                label = "icon scale"
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = stringResource(R.string.delete_descritpion),
                    modifier = Modifier.scale(scale)
                )
            }
        }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (visitor.name.isNotBlank()) {
                        Checkbox(
                            checked = visitor.isSelected,
                            onCheckedChange = { onVisitorSelected(visitor) }
                        )
                    }
                    Text(text = visitor.name.ifBlank { stringResource(R.string.edit_fio) })
                }
                if (visitor.cars.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        if (visitor.cars.isNotEmpty()) {
                            Text(stringResource(R.string.cars_title))
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            visitor.cars.forEach { car ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = car.isSelected,
                                        onCheckedChange = { onCarSelected(car, visitor) }
                                    )
                                    Text(
                                        text = car.make,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun visitorsCardPreview() {
    VisitorCard(
        visitor = Visitor(
            name = "Мойша",
            cars = listOf(
                Car("Жигули", "м0248мм"),
            )
        )
    )
}
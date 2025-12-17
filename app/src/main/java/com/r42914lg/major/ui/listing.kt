package com.r42914lg.major.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.r42914lg.major.MainStateHolder
import com.r42914lg.major.ScreenEvent
import com.r42914lg.major.model.Car
import com.r42914lg.major.model.Visitor

@Composable
fun Visitors(
    mainStateHolder: MainStateHolder,
    modifier: Modifier = Modifier,
    onVisitorClick: (Visitor) -> Unit = {},
) {
    val state by mainStateHolder.screenState.collectAsStateWithLifecycle()
    state?.let { visitors ->
        LazyColumn(modifier.padding(vertical = 100.dp, horizontal = 16.dp)) {
            items(visitors.size, key = { visitors[it].id }) { index ->
                VisitorCard(
                    modifier = Modifier.clickable {
                        onVisitorClick(visitors[index])
                    },
                    visitor = visitors[index],
                    onDelete = {
                        mainStateHolder.onScreenEvent(ScreenEvent.RemoveVisitor(it))
                    }
                )
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Добавь тенниста ниже!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitorCard(
    modifier: Modifier = Modifier,
    visitor: Visitor = Visitor(),
    onDelete: (Visitor) -> Unit = {}
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
                    contentDescription = "Delete",
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
                Text(text = visitor.name)
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    visitor.cars.forEach { car ->
                        var isChecked by remember { mutableStateOf(false) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it }
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
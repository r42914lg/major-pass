package com.r42914lg.major.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.r42914lg.major.model.Car
import com.r42914lg.major.model.Visitor
import com.r42914lg.major.ui.theme.MajorTheme

@Composable
fun Visitors(
    modifier: Modifier = Modifier,
    visitors: List<Visitor> = emptyList()
) {
    LazyColumn(modifier) {
        visitors.forEach {
            item {
                VisitorCard(visitor = it)
            }
        }
    }
}

@Composable
fun VisitorCard(
    modifier: Modifier = Modifier,
    visitor: Visitor = Visitor(),
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
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

@Preview(showBackground = true)
@Composable
fun visitorsPreview() {
    MajorTheme {
        Visitors(
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

@Preview(showBackground = true)
@Composable
fun visitorsCardPreview() {
    VisitorCard(
        visitor = Visitor(
            "Мойша",
            listOf(
                Car("Жигули", "м0248мм"),
            )
        )
    )
}
package com.r42914lg.major.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.r42914lg.major.model.Car
import com.r42914lg.major.model.Visitor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    data: Visitor,
    modifier: Modifier = Modifier,
    onEditComplete: (Visitor) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf(data.name) }
    val cars = remember { data.cars.toMutableStateList() }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Visitor") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                singleLine = true,
                onValueChange = { name = it },
                label = { Text("Visitor Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Cars", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(cars) { index, car ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = car.make,
                            onValueChange = { updatedMake ->
                                cars[index] = car.copy(make = updatedMake)
                            },
                            label = { Text("Make") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = car.licencePlate,
                            onValueChange = { updatedPlate ->
                                cars[index] = car.copy(licencePlate = updatedPlate)
                            },
                            label = { Text("Plate") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { cars.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove Car")
                        }
                    }
                }
            }

            Button(
                onClick = { cars.add(Car("", "")) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить машину")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val updatedVisitor = data.copy(name = name, cars = cars.toList())
                    onEditComplete(updatedVisitor)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun detailsPreview() {
    Details(
        data = Visitor(
            name = "John Doe",
            cars = listOf(
                Car("Toyota", "1234"),
                Car("Honda", "5678")
            )
        )
    )
}

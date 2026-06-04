package com.example.ahorradinv1.metas.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetaScreen(viewModel: MetaViewModel) {
    val state by viewModel.uiState.collectAsState()

    // Estados para los campos de texto fijos en pantalla
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título limpio sin el texto de la rama
        Text(
            text = "Mis Metas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- FORMULARIO DIRECTO EN PANTALLA PARA AGREGAR ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Nueva Meta de Ahorro", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nuevoTitulo,
                    onValueChange = { nuevoTitulo = it },
                    label = { Text("Título de la meta") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nuevaDescripcion,
                    onValueChange = { nuevaDescripcion = it },
                    label = { Text("Descripción o estrategia") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (nuevoTitulo.isNotBlank()) {
                            viewModel.agregarMeta(nuevoTitulo, nuevaDescripcion)
                            nuevoTitulo = ""
                            nuevaDescripcion = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Agregar Meta", color = Color.White)
                }
            }
        }

        // --- LISTA DE METAS CON BOTÓN BORRAR ---
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is MetaUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is MetaUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = currentState.mensaje)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.cargarMetas() }) { Text("Reintentar") }
                    }
                }
                is MetaUiState.Success -> {
                    if (currentState.lista.isEmpty()) {
                        Text("No tienes metas registradas.", color = Color.Gray)
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(currentState.lista) { meta ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = meta.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text(text = meta.descripcion, color = Color.Gray, fontSize = 14.sp)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = if (meta.completada) "✅ Completada" else "⏳ Pendiente",
                                                fontSize = 12.sp
                                            )
                                        }
                                        // Botón explícito para borrar metas
                                        IconButton(onClick = { viewModel.eliminarMeta(meta.id) }) {
                                            Text("🗑️", fontSize = 18.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
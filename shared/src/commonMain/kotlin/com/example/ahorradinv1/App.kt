package com.example.ahorradinv1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun App() {

    var loggedIn by remember {
        mutableStateOf(false)
    }

    MaterialTheme {

        if (loggedIn) {
            DashboardScreen()
        } else {
            LoginScreen(
                onLoginSuccess = {
                    loggedIn = true
                }
            )
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Ahorradín",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
            },
            label = {
                Text("Usuario")
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Contraseña")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (
                    usuario == "admin"
                    && password == "1234"
                ) {
                    onLoginSuccess()
                } else {
                    error = "Usuario o contraseña incorrectos"
                }
            }
        ) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = error,
            color = Color.Red
        )
    }
}

@Composable
fun DashboardScreen() {

    var saldo by remember { mutableStateOf(3250.0) }
    var ingresos by remember { mutableStateOf(5000.0) }
    var gastos by remember { mutableStateOf(1750.0) }

    var mostrarIngreso by remember { mutableStateOf(false) }
    var mostrarGasto by remember { mutableStateOf(false) }

    var cantidadIngreso by remember { mutableStateOf("") }
    var cantidadGasto by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
    ) {

        Text(
            text = "¡Hola, Mauricio Ramos Castro!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Saldo Actual:")

                Text(
                    text = "$${String.format("%.2f", saldo)} MXN",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "+$${String.format("%.0f", ingresos)}",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )

                        Text("Ingresos")
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "-$${String.format("%.0f", gastos)}",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )

                        Text("Gastos")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mis Metas",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Text(
                        "Laptop Nueva",
                        fontWeight = FontWeight.Bold
                    )

                    Text("$15,000 / $3,750")

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { 0.25f },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("25%")
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Text(
                        "Fondo Emergencia",
                        fontWeight = FontWeight.Bold
                    )

                    Text("$5,000 / $2,500")

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { 0.50f },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("50%")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Movimientos Recientes",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("+3300 Depósito")
        Text("-45 Cafetería")

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2E7D32)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    "Recomendación Inteligente",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Reduce gastos hormiga esta semana y acelera tu ahorro para la Laptop.",
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = {
                    mostrarIngreso = true
                }
            ) {
                Text("Añadir Ingreso")
            }

            Button(
                onClick = {
                    mostrarGasto = true
                }
            ) {
                Text("Añadir Gasto")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Text("Inicio")
            Text("Metas")
            Text("Calendario")
            Text("Perfil")
        }
    }

    if (mostrarIngreso) {

        AlertDialog(
            onDismissRequest = {
                mostrarIngreso = false
            },
            confirmButton = {

                Button(
                    onClick = {

                        val monto =
                            cantidadIngreso.toDoubleOrNull() ?: 0.0

                        saldo += monto
                        ingresos += monto

                        cantidadIngreso = ""
                        mostrarIngreso = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            title = {
                Text("Nuevo Ingreso")
            },
            text = {

                OutlinedTextField(
                    value = cantidadIngreso,
                    onValueChange = {
                        cantidadIngreso = it
                    },
                    label = {
                        Text("Cantidad")
                    }
                )
            }
        )
    }

    if (mostrarGasto) {

        AlertDialog(
            onDismissRequest = {
                mostrarGasto = false
            },
            confirmButton = {

                Button(
                    onClick = {

                        val monto =
                            cantidadGasto.toDoubleOrNull() ?: 0.0

                        saldo -= monto
                        gastos += monto

                        cantidadGasto = ""
                        mostrarGasto = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            title = {
                Text("Nuevo Gasto")
            },
            text = {

                OutlinedTextField(
                    value = cantidadGasto,
                    onValueChange = {
                        cantidadGasto = it
                    },
                    label = {
                        Text("Cantidad")
                    }
                )
            }
        )
    }
}

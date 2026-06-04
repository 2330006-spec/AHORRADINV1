package com.example.ahorradinv1

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.ahorradinv1.models.LoginResponse
import com.example.ahorradinv1.models.Meta
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    var appState by remember { mutableStateOf<AppState>(AppState.Login) }
    val sessionManager = remember { SessionManager() }

    // Verificar sesión inicial
    LaunchedEffect(Unit) {
        if (sessionManager.obtenerToken() != null) {
            appState = AppState.Dashboard
        }
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            // Capa 1: El contenido de la App
            AnimatedContent(
                targetState = appState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(800)) togetherWith fadeOut(animationSpec = tween(400))
                }
            ) { state ->
                when (state) {
                    is AppState.Login -> LoginScreen(onLoginSuccess = { token ->
                        sessionManager.guardarToken(token)
                        appState = AppState.Dashboard
                    })
                    is AppState.Dashboard -> DashboardScreen()
                }
            }

            // Capa 2: Efecto de Expansión Blanca (Cinemático)
            var showExpansion by remember { mutableStateOf(false) }
            
            // Detectar cuando pasamos a Dashboard para activar la expansión
            LaunchedEffect(appState) {
                if (appState is AppState.Dashboard) {
                    showExpansion = true
                }
            }

            if (showExpansion) {
                var startExpansion by remember { mutableStateOf(false) }
                
                val scale by animateFloatAsState(
                    targetValue = if (startExpansion) 50f else 0f,
                    animationSpec = tween(durationMillis = 1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1f))
                )

                val alpha by animateFloatAsState(
                    targetValue = if (scale > 30f) 0f else 1f,
                    animationSpec = tween(durationMillis = 500),
                    finishedListener = { showExpansion = false }
                )

                LaunchedEffect(Unit) {
                    delay(50)
                    startExpansion = true
                }

                if (alpha > 0f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 92.dp)
                            .size(64.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                            }
                            .background(Color.White, CircleShape)
                    )
                }
            }
        }
    }
}

sealed class AppState {
    object Login : AppState()
    object Dashboard : AppState()
}



enum class LoginState {
    WELCOME, LOGIN
}

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    val apiService = remember { ApiService() }
    val scope = rememberCoroutineScope()

    var currentState by remember { mutableStateOf(LoginState.WELCOME) }
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Animación de desenfoque (Efecto iPhone corregido)
    val blurRadius by animateDpAsState(
        targetValue = if (currentState == LoginState.WELCOME) 0.dp else 24.dp,
        animationSpec = tween(durationMillis = 600)
    )

    // Animación de "Video" (Zoom/Movimiento infinito)
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // FONDO: TextureView (Soporta desenfoque y pausa)
        VideoBackground(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.scaleX = if (currentState == LoginState.WELCOME) scale else 1.1f
                    this.scaleY = if (currentState == LoginState.WELCOME) scale else 1.1f
                }
                .blur(blurRadius),
            videoUrl = "",
            isPlaying = currentState == LoginState.WELCOME
        )

        // CAPA OSCURA (Se intensifica en el login)
        val overlayAlpha by animateFloatAsState(
            targetValue = if (currentState == LoginState.WELCOME) 0.3f else 0.7f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = overlayAlpha))
        )

        // CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // SECCIÓN SUPERIOR: Título
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Text(
                    text = "AHORRADÍN",
                    fontSize = 40.sp, // Ajustado para evitar salto de línea
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.White,
                    letterSpacing = 6.sp, // Reducido un poco para que quepa en una línea
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Domina tus finanzas.\nConstruye el futuro que mereces.",
                    fontSize = 18.sp, // Letra más grande
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Light
                )
            }

            // SECCIÓN INFERIOR: Botones o Formulario
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentState == LoginState.WELCOME) {
                    Button(
                        onClick = { currentState = LoginState.LOGIN },
                        modifier = Modifier.fillMaxWidth().height(64.dp), // Botón más alto
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(32.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            text = "LOGIN",
                            fontSize = 18.sp, // Letra más grande
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { /* Sign Up */ },
                        modifier = Modifier.fillMaxWidth().height(64.dp), // Botón más alto
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                        shape = RoundedCornerShape(32.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            text = "SIGN UP",
                            fontSize = 18.sp, // Letra más grande
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                } else {
                    // FORMULARIO CON FONDO DIFUMINADO
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = { usuario = it },
                        label = { Text("USUARIO", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                            focusedContainerColor = Color.White.copy(alpha = 0.1f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("CONTRASEÑA", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                            focusedContainerColor = Color.White.copy(alpha = 0.1f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Button(
                            onClick = {
                                isLoading = true
                                error = ""
                                scope.launch {
                                    val response = apiService.login(usuario, password)
                                    isLoading = false
                                    if (response.success) {
                                        onLoginSuccess(response.token ?: "")
                                    } else {
                                        error = response.mensaje
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(64.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                            shape = RoundedCornerShape(32.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            Text("LOGIN", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        TextButton(
                            onClick = { currentState = LoginState.WELCOME },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("CANCEL", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        }
                    }

                    if (error.isNotEmpty()) {
                        Text(
                            text = error,
                            color = Color(0xFFFF5252),
                            modifier = Modifier.padding(top = 10.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DashboardScreen() {
    val apiService = remember { ApiService() }
    var metas by remember { mutableStateOf<List<Meta>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        metas = apiService.obtenerMetas()
    }

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
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = { seccionActiva = "inicio" }) {
                        Text("Inicio", color = if (seccionActiva == "inicio") Color(0xFF2E7D32) else Color.Gray)
                    }
                    TextButton(onClick = { seccionActiva = "metas" }) {
                        Text("Metas", color = if (seccionActiva == "metas") Color(0xFF2E7D32) else Color.Gray)
                    }
                    Text("Calendario", modifier = Modifier.padding(top = 12.dp), color = Color.Gray)
                    Text("Perfil", modifier = Modifier.padding(top = 12.dp), color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mis Metas (Desde API)",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column {
            metas.forEach { meta ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(meta.nombre, fontWeight = FontWeight.Bold)
                        Text("$${meta.ahorrado} / $${meta.objetivo}")
                        LinearProgressIndicator(
                            progress = { (meta.ahorrado / meta.objetivo).toFloat() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { mostrarIngreso = true }) { Text("Añadir Ingreso") }
                        Button(onClick = { mostrarGasto = true }) { Text("Añadir Gasto") }
                    }
                }
            }
        }
    }

    // Cuadros de diálogo para Ingresos y Gastos permanecen intactos
    if (mostrarIngreso) {
        AlertDialog(
            onDismissRequest = { mostrarIngreso = false },
            confirmButton = {
                Button(onClick = {
                    val monto = cantidadIngreso.toDoubleOrNull() ?: 0.0
                    saldo += monto
                    ingresos += monto
                    cantidadIngreso = ""
                    mostrarIngreso = false
                }) { Text("Guardar") }
            },
            title = { Text("Nuevo Ingreso") },
            text = { OutlinedTextField(value = cantidadIngreso, onValueChange = { cantidadIngreso = it }, label = { Text("Cantidad") }) }
        )
    }

    if (mostrarGasto) {
        AlertDialog(
            onDismissRequest = { mostrarGasto = false },
            confirmButton = {
                Button(onClick = {
                    val monto = cantidadGasto.toDoubleOrNull() ?: 0.0
                    saldo -= monto
                    gastos += monto
                    cantidadGasto = ""
                    mostrarGasto = false
                }) { Text("Guardar") }
            },
            title = { Text("Nuevo Gasto") },
            text = { OutlinedTextField(value = cantidadGasto, onValueChange = { cantidadGasto = it }, label = { Text("Cantidad") }) }
        )
    }
}
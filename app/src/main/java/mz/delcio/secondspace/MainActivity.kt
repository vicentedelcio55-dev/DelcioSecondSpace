package mz.delcio.secondspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF101A2C)
private val Blue = Color(0xFF5478FF)
private val Panel = Color(0xFF1A2940)
private val White = Color(0xFFF5F7FB)

class MainActivity : ComponentActivity() {
    private lateinit var pinStore: PinStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pinStore = PinStore(this)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Blue, background = Navy, surface = Panel,
                onBackground = White, onSurface = White
            )) {
                Surface(modifier = Modifier.fillMaxSize(), color = Navy) {
                    SecondSpaceApp(pinStore)
                }
            }
        }
    }
}

@Composable
private fun SecondSpaceApp(pinStore: PinStore) {
    var hasPin by remember { mutableStateOf(pinStore.hasPin()) }
    var enteredPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var unlocked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("DÉLCIO", color = Blue, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text("Second Space", color = White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            if (unlocked) "O teu espaço privado está aberto"
            else if (hasPin) "Introduz o PIN para continuar"
            else "Cria um PIN para proteger o teu espaço",
            color = Color(0xFFB9C5D9)
        )
        Spacer(Modifier.height(28.dp))

        if (unlocked) {
            Card(colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Espaço privado", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))
                    Text("Versão inicial", color = Color(0xFFB9C5D9))
                    Spacer(Modifier.height(16.dp))
                    Text("Aqui será adicionada a área de ficheiros privados.", color = White)
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { unlocked = false; enteredPin = "" },
                        modifier = Modifier.fillMaxWidth()) { Text("Bloquear espaço") }
                }
            }
        } else {
            OutlinedTextField(
                value = enteredPin,
                onValueChange = { if (it.length <= 8 && it.all(Char::isDigit)) enteredPin = it },
                label = { Text("PIN numérico (4 a 8 dígitos)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth()
            )
            if (!hasPin) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = { if (it.length <= 8 && it.all(Char::isDigit)) confirmPin = it },
                    label = { Text("Confirmar PIN") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    message = when {
                        enteredPin.length !in 4..8 -> "Usa um PIN de 4 a 8 dígitos."
                        !hasPin && enteredPin != confirmPin -> "Os PINs não coincidem."
                        !hasPin -> {
                            pinStore.savePin(enteredPin)
                            hasPin = true
                            enteredPin = ""
                            confirmPin = ""
                            "PIN criado. Introduz o PIN para desbloquear."
                        }
                        pinStore.verifyPin(enteredPin) -> {
                            unlocked = true
                            enteredPin = ""
                            ""
                        }
                        else -> {
                            enteredPin = ""
                            "PIN incorreto. Tenta novamente."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) { Text(if (hasPin) "Desbloquear" else "Criar PIN") }

            if (message.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(message, color = if (message.startsWith("PIN incorreto")) Color(0xFFFF8A80) else Color(0xFFB9C5D9))
            }
        }

        Spacer(Modifier.height(30.dp))
        Text("Privacidade local • Sem conta obrigatória", color = Color(0xFF8090AA), fontSize = 12.sp)
    }
}

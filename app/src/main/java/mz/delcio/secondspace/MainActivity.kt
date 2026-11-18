package mz.delcio.secondspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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

private val Black = Color(0xFF08090C)
private val Panel = Color(0xFF17191F)
private val Accent = Color(0xFF8B9CFF)
private val White = Color(0xFFF5F5F7)
private val Muted = Color(0xFF9296A3)

class MainActivity : ComponentActivity() {
    private lateinit var pinStore: PinStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(8, 9, 12)
        window.navigationBarColor = android.graphics.Color.rgb(8, 9, 12)

        pinStore = PinStore(this)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Accent,
                    background = Black,
                    surface = Panel,
                    onBackground = White,
                    onSurface = White
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Black
                ) {
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
    var selectedSection by remember { mutableStateOf<String?>(null) }

    if (unlocked) {
        if (selectedSection == null) {
            Dashboard(
                onSelect = { selectedSection = it },
                onLock = {
                    unlocked = false
                    selectedSection = null
                    enteredPin = ""
                }
            )
        } else {
            SectionScreen(
                title = selectedSection!!,
                onBack = { selectedSection = null }
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .verticalScroll(rememberScrollState())
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(70.dp))

            Text(
                "DÉLCIO",
                color = Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 5.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "SECOND SPACE",
                color = White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                if (hasPin) "O teu espaço. A tua privacidade."
                else "Cria um PIN para proteger o teu espaço.",
                color = Muted,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(42.dp))

            OutlinedTextField(
                value = enteredPin,
                onValueChange = {
                    if (it.length <= 8 && it.all(Char::isDigit)) {
                        enteredPin = it
                    }
                },
                label = { Text("PIN numérico") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            if (!hasPin) {
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = {
                        if (it.length <= 8 && it.all(Char::isDigit)) {
                            confirmPin = it
                        }
                    },
                    label = { Text("Confirmar PIN") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    message = when {
                        enteredPin.length !in 4..8 ->
                            "O PIN deve ter entre 4 e 8 dígitos."

                        !hasPin && enteredPin != confirmPin ->
                            "Os PINs não coincidem."

                        !hasPin -> {
                            pinStore.savePin(enteredPin)
                            hasPin = true
                            enteredPin = ""
                            confirmPin = ""
                            "PIN criado. Agora podes entrar."
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    if (hasPin) "DESBLOQUEAR" else "CRIAR PIN",
                    fontWeight = FontWeight.Bold
                )
            }

            if (message.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                Text(message, color = Accent)
            }

            Spacer(Modifier.height(45.dp))

            Text(
                "PROTEÇÃO LOCAL  •  DÉLCIO SECOND SPACE",
                color = Muted,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun Dashboard(
    onSelect: (String) -> Unit,
    onLock: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .verticalScroll(rememberScrollState())
            .padding(22.dp)
    ) {
        Spacer(Modifier.height(22.dp))

        Text(
            "DÉLCIO",
            color = Accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "O teu espaço privado",
            color = White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Tudo o que é teu, num só lugar.",
            color = Muted,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Panel)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("ESPAÇO PROTEGIDO", color = Accent, fontSize = 11.sp)
                Spacer(Modifier.height(8.dp))
                Text("Bem-vindo de volta.", color = White, fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(5.dp))
                Text("A tua área privada está desbloqueada.",
                    color = Muted, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(25.dp))

        Text(
            "AS TUAS ÁREAS",
            color = Muted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(Modifier.height(13.dp))

        FeatureCard("📷", "Fotografias", "Imagens privadas", onSelect)
        FeatureCard("🎬", "Vídeos", "Os teus vídeos pessoais", onSelect)
        FeatureCard("🔒", "Aplicações", "Proteção de aplicações", onSelect)
        FeatureCard("🛡️", "Espaço privado", "A tua área protegida", onSelect)

        Spacer(Modifier.height(25.dp))

        OutlinedButton(
            onClick = onLock,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("BLOQUEAR ESPAÇO")
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "DÉLCIO SECOND SPACE  •  PRIVACIDADE EM PRIMEIRO LUGAR",
            color = Muted,
            fontSize = 9.sp
        )

        Spacer(Modifier.height(15.dp))
    }
}

@Composable
private fun FeatureCard(
    symbol: String,
    title: String,
    description: String,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 11.dp)
            .clickable { onSelect(title) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(symbol, fontSize = 27.sp)

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = White, fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(description, color = Muted, fontSize = 12.sp)
            }

            Text("›", color = Accent, fontSize = 26.sp)
        }
    }
}

@Composable
private fun SectionScreen(
    title: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("DÉLCIO SECOND SPACE", color = Accent,
            fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(28.dp))

        Text(title, color = White, fontSize = 27.sp,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(14.dp))

        Text(
            "Esta área está preparada para a próxima fase de desenvolvimento.",
            color = Muted,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(28.dp))

        Button(onClick = onBack) {
            Text("VOLTAR AO INÍCIO")
        }
    }
}

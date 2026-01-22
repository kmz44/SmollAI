/*
 * Copyright (C) 2024 smollai IA
 * Modern model setup screen for teens
 */

package io.smollai.smollaiandroid.ui.screens.model_setup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.data.ModelsDB
import io.smollai.smollaiandroid.data.SmollAIPreferences
import io.smollai.smollaiandroid.ui.components.AppProgressDialog
import io.smollai.smollaiandroid.ui.components.hideProgressDialog
import io.smollai.smollaiandroid.ui.components.setProgressDialogText
import io.smollai.smollaiandroid.ui.components.setProgressDialogTitle
import io.smollai.smollaiandroid.ui.components.showProgressDialog
import io.smollai.smollaiandroid.ui.screens.chat.ChatActivity
import io.smollai.smollaiandroid.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths

class ModernModelSetupActivity : ComponentActivity(), KoinComponent {
    private var openChatScreen: Boolean = true
    private val modelsDB: ModelsDB by inject()
    private val SmollAIPreferences: SmollAIPreferences by inject()
    
    // Selector de archivos GGUF
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            copyModelFile(uri) {
                if (openChatScreen) {
                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }    /**
     * Copia el archivo del modelo desde la URI seleccionada al directorio interno de la app
     * y lo agrega a la base de datos de modelos.
     */
    private fun copyModelFile(uri: Uri, onComplete: () -> Unit) {
        var fileName = ""
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        
        if (fileName.isNotEmpty()) {
            setProgressDialogTitle(getString(R.string.copying_model_file))
            setProgressDialogText(getString(R.string.model_copy_description, fileName))
            showProgressDialog()
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    contentResolver.openInputStream(uri).use { inputStream ->
                        FileOutputStream(File(filesDir, fileName)).use { outputStream ->
                            inputStream?.copyTo(outputStream)
                        }
                    }
                    
                    val modelId = modelsDB.addModel(
                        fileName,
                        "",
                        Paths.get(filesDir.absolutePath, fileName).toString(),
                    )
                    
                    // Guardar este modelo como el seleccionado
                    SmollAIPreferences.setSelectedModelId(modelId)
                    SmollAIPreferences.setFirstTimeSetupCompleted()
                    
                    withContext(Dispatchers.Main) {
                        hideProgressDialog()
                        onComplete()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        hideProgressDialog()
                        Toast.makeText(this@ModernModelSetupActivity, 
                            "Error copying file: ${e.message}", 
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.invalid_file), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        openChatScreen = intent.extras?.getBoolean("openChatScreen") ?: true
          setContent {
            SmollAITheme {
                Box {
                    ModernModelSetupScreen(
                        onSelectFile = { 
                            filePickerLauncher.launch("*/*") 
                        },
                        onBackPressed = { finish() }
                    )
                    AppProgressDialog()
                }
            }
        }
    }
}

@Composable
fun ModernModelSetupScreen(
    onSelectFile: () -> Unit,
    onBackPressed: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(0f) }
    
    // Animación de entrada
    LaunchedEffect(Unit) {
        delay(100)
        scale = 1f
    }
    
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Coincidir con la paleta de colores del chat
                if (androidx.compose.foundation.isSystemInDarkTheme()) {
                    Color(0xFF0F172A) // Dark background like chat
                } else {
                    Color(0xFFF1F5F9) // Light background
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .scale(animatedScale),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            
            // Logo y título
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(R.drawable.smollai_logo),
                    contentDescription = "smollai IA Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                
                // Título principal
                Text(
                    text = "📁 Seleccionar Modelo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFontFamily,
                    color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color(0xFF1E293B),
                    textAlign = TextAlign.Center
                )
                
                // Descripción
                Text(
                    text = "Selecciona tu archivo GGUF para comenzar",
                    fontSize = 16.sp,
                    fontFamily = InterFontFamily,
                    color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            // Botón principal - Seleccionar archivo
            Button(
                onClick = onSelectFile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF6366F1), // Purple
                                    Color(0xFFD946EF)  // Pink
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📂 Seleccionar Archivo GGUF",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = InterFontFamily,
                        color = Color.White
                    )
                }
            }
            
            // Botón volver
            OutlinedButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color(0xFF334155)
                ),
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White.copy(alpha = 0.2f) else Color(0xFF94A3B8)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "⬅️ Volver",
                    fontSize = 16.sp,
                    fontFamily = InterFontFamily
                )
            }        }
    }
}

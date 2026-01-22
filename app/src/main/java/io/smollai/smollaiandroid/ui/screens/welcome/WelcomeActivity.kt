/*
 * Copyright (C) 2024 smollai IA
 * Renovated for teens with modern UI/UX
 */

package io.smollai.smollaiandroid.ui.screens.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.ui.screens.model_setup.ModernModelSetupActivity
import io.smollai.smollaiandroid.ui.theme.*
import io.smollai.smollaiandroid.utils.NetworkUtils

class WelcomeActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContentView(ComposeView(this).apply {
            setContent {
                smollaiAndroidTheme {
                    WelcomeScreen(
                        onLocalModeClick = { openModelSetup() },
                        onCloudModeClick = { showCloudModeComingSoon() },
                        onMoreInfoClick = { openMoreInformation() }
                    )
                }
            }
        })
    }
    
    private fun openModelSetup() {
        val intent = Intent(this, ModernModelSetupActivity::class.java)
        intent.putExtra("openChatScreen", true)
        startActivity(intent)
        finish()
    }
      private fun showCloudModeComingSoon() {
        Toast.makeText(this, getString(R.string.cloud_mode_coming_soon), Toast.LENGTH_LONG).show()
    }
    
    private fun openMoreInformation() {
        if (NetworkUtils.isInternetAvailable(this)) {
            val intent = Intent(this, Class.forName("io.smollai.smollaiandroid.ui.screens.webview.WebViewActivity"))
            intent.putExtra("url", "https://masinformacion.usasavorwarts.com/")
            intent.putExtra("title", getString(R.string.more_info))
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun WelcomeScreen(
    onLocalModeClick: () -> Unit,
    onCloudModeClick: () -> Unit,
    onMoreInfoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SmollAISecondary,
                        SmollAIPrimary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo placeholder
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SmollAIPrimary)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💭",
                        fontSize = 48.sp
                    )
                }
            }
            
            // Welcome title
            Text(
                text = stringResource(R.string.welcome_to_smollai),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Subtitle
            Text(
                text = stringResource(R.string.welcome_subtitle),
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            // Mode selection title
            Text(
                text = stringResource(R.string.choose_your_mode),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Local Mode Card
            ModeCard(
                title = stringResource(R.string.local_mode),
                description = stringResource(R.string.local_mode_desc),
                emoji = "🏠",
                isAvailable = true,
                onClick = onLocalModeClick,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Cloud Mode Card
            ModeCard(
                title = stringResource(R.string.cloud_mode),
                description = stringResource(R.string.cloud_mode_desc),
                emoji = "☁️",
                isAvailable = false,
                onClick = onCloudModeClick,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // More Information Button
            OutlinedButton(
                onClick = onMoreInfoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.White, Color.White.copy(alpha = 0.7f))
                    )
                )
            ) {
                Text(
                    text = stringResource(R.string.more_info),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Footer
            Text(
                text = stringResource(R.string.welcome_footer),
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ModeCard(
    title: String,
    description: String,
    emoji: String,
    isAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isAvailable) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAvailable) Color.White else Color.White.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAvailable) SmollAIPrimary else SmollAIPrimary.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = description,
                fontSize = 14.sp,
                color = if (isAvailable) Color.Gray else Color.Gray.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)            )
            
            if (!isAvailable) {
                Text(
                    text = stringResource(R.string.coming_soon),
                    fontSize = 12.sp,
                    color = smollaiAccent,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

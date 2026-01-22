/*
 * Copyright (C) 2024 smollai IA
 * Renovated for teens with modern UI/UX
 */

package io.smollai.smollaiandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import io.smollai.smollaiandroid.ui.screens.chat.ChatActivity

class SmollAIMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ir directamente al chat - el ChatActivity maneja la selección de modelo si es necesario
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
        finish()
    }
}

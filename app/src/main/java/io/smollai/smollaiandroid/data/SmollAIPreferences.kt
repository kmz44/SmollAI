/*
 * Copyright (C) 2024 smollai IA
 * Preferences manager for smollai IA app
 */

package io.smollai.smollaiandroid.data

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.annotation.Single

@Single
class SmollAIPreferences(private val context: Context) {
    
    companion object {
        private const val PREFS_NAME = "smollai_preferences"
        private const val KEY_SELECTED_MODEL_ID = "selected_model_id"
        private const val KEY_FIRST_TIME_SETUP = "first_time_setup"
        private const val KEY_RESPONSE_MAX_LENGTH = "response_max_length"
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    /**
     * Guarda el ID del modelo seleccionado
     */
    fun setSelectedModelId(modelId: Long) {
        prefs.edit().putLong(KEY_SELECTED_MODEL_ID, modelId).apply()
    }
    
    /**
     * Obtiene el ID del modelo seleccionado, -1 si no hay ninguno
     */
    fun getSelectedModelId(): Long {
        return prefs.getLong(KEY_SELECTED_MODEL_ID, -1L)
    }
    
    /**
     * Verifica si hay un modelo seleccionado
     */
    fun hasSelectedModel(): Boolean {
        return getSelectedModelId() != -1L
    }
    
    /**
     * Elimina el modelo seleccionado (para cambiar de modelo)
     */
    fun clearSelectedModel() {
        prefs.edit().remove(KEY_SELECTED_MODEL_ID).apply()
    }
    
    /**
     * Marca que el usuario ya completó el setup inicial
     */
    fun setFirstTimeSetupCompleted() {
        prefs.edit().putBoolean(KEY_FIRST_TIME_SETUP, false).apply()
    }
    
    /**
     * Verifica si es la primera vez que el usuario abre la app
     */
    fun isFirstTimeSetup(): Boolean {
        return prefs.getBoolean(KEY_FIRST_TIME_SETUP, true)
    }
    
    /**
     * Establece la longitud máxima de respuesta (0 = sin límite)
     */
    fun setResponseMaxLength(maxLength: Int) {
        prefs.edit().putInt(KEY_RESPONSE_MAX_LENGTH, maxLength).apply()
    }
    
    /**
     * Obtiene la longitud máxima de respuesta (0 = sin límite)
     */
    fun getResponseMaxLength(): Int {
        return prefs.getInt(KEY_RESPONSE_MAX_LENGTH, 0) // 0 = sin límite por defecto
    }
}

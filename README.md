# 🧠 SmollAI - Tu IA Local en la Palma de tu Mano

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![LLM](https://img.shields.io/badge/AI-LLM-blueviolet?style=for-the-badge)](https://github.com/ggerganov/llama.cpp)

**SmollAI** es una aplicación de Android moderna y potente diseñada para ejecutar modelos de lenguaje (LLM) completamente fuera de línea. Basada en el motor de alto rendimiento `llama.cpp`, SmollAI te permite chatear con IA avanzada sin necesidad de internet, garantizando **privacidad absoluta** y **zero latencia de red**.

---

## ✨ Características Principales

- 🚀 **Inferencia 100% Local**: No se envían datos a la nube. Privacidad total.
- 📱 **Interfaz Premium**: Diseño moderno, oscuro y elegante optimizado para una experiencia fluida.
- 📂 **Soporte GGUF**: Importa cualquier modelo en formato GGUF directamente desde tu almacenamiento.
- ⚙️ **Control Total**: Ajusta parámetros como `Min-P`, `Temperatura` y `System Prompts` para cada chat.
- 🧠 **Cerebro en el Dispositivo**: Utiliza la potencia de tu procesador móvil para tareas de IA complejas.
- 🎨 **Markdown Rico**: Soporte completo para renderizado de código, tablas y fórmulas matemáticas.

---

## 🚀 Instalación Rápida

1. **Descarga**: Obtén el último APK desde la sección de [Releases](https://github.com/[USER]/SmollAI/releases).
2. **Instala**: Abre el APK en tu dispositivo Android (asegúrate de permitir instalaciones de fuentes desconocidas).
3. **Descarga un Modelo**: Ve a [Hugging Face](https://huggingface.co/models?library=gguf) y descarga un modelo ligero (ej. `Llama-3.2-1B-Instruct-Q4_K_M.gguf`).
4. **Importa**: Abre SmollAI, ve al menú de opciones e importa tu archivo `.gguf`.
5. **Chatea**: ¡Eso es todo! Empieza a hablar con tu IA local.

---

## 🛠️ Desarrollo y Compilación

Si eres desarrollador y quieres compilar SmollAI desde la fuente:

### Requisitos
- Android Studio Ladybug o superior.
- Android NDK 21+.
- CMake 3.18+.

### Pasos
```bash
git clone https://github.com/[USER]/SmollAI.git
cd SmollAI
./gradlew assembleRelease
```

---

## 🎨 Diseño Visual

La aplicación utiliza una paleta de colores **Midnight Indigo** y **Cyber Pink**, ofreciendo un look futurista y profesional:

- **Fondo**: `#0F172A` (Slate Dark)
- **Acento Primario**: `#6366F1` (Indigo)
- **Gradiente Secundario**: `#D946EF` (Fuchsia)

---

## 📄 Licencia

Este proyecto está bajo la licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.

## 🙏 Agradecimientos

- [llama.cpp](https://github.com/ggerganov/llama.cpp)
- [Markwon](https://github.com/noties/Markwon)
- [Koin](https://insert-koin.io/)

---

> [!TIP]
> **SmollAI** es ideal para dispositivos con 8GB+ de RAM, pero puede ejecutar modelos de 1B parámetros sorprendentemente bien en dispositivos de gama media con 4GB.


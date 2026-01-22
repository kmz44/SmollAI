/*
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

package io.smollai.smollaiandroid.ui.screens.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Spanned
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.data.Chat
import io.smollai.smollaiandroid.data.ModelsDB
import io.smollai.smollaiandroid.data.SmollAIPreferences
import io.smollai.smollaiandroid.ui.components.AppBarTitleText
import io.smollai.smollaiandroid.ui.components.MediumLabelText
import io.smollai.smollaiandroid.ui.screens.welcome.WelcomeActivity
import io.smollai.smollaiandroid.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

class ChatActivity : ComponentActivity() {
    private val viewModel: ChatScreenViewModel by inject()
    private val modelsDB: ModelsDB by inject()
    private val SmollAIPreferences: SmollAIPreferences by inject()
    
    // Selector de archivos para cambio de modelo
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { copyAndSetNewModel(it) }
    }
    
    private fun goBackToMainMenu() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun copyAndSetNewModel(uri: Uri) {
        var fileName = ""
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        
        if (fileName.isNotEmpty()) {
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
                    
                    // Cambiar al nuevo modelo
                    SmollAIPreferences.setSelectedModelId(modelId)
                    viewModel.updateChatLLM(modelId)
                    viewModel.loadModel()
                    
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChatActivity,
                            getString(R.string.model_changed_success),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChatActivity, 
                            "Error: ${e.message}", 
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "chat",
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
            ) {
                composable("edit-chat") {
                    EditChatSettingsScreen(
                        viewModel,
                        onBackClicked = { navController.navigateUp() },
                    )
                }
                composable("chat") {
                    ChatActivityScreenUI(
                        viewModel,
                        onEditChatParamsClick = { navController.navigate("edit-chat") },
                        onSelectNewModelFileClick = { filePickerLauncher.launch("*/*") },
                        onBackToMainMenuClick = { goBackToMainMenu() },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatActivityScreenUI(
    viewModel: ChatScreenViewModel,
    onEditChatParamsClick: () -> Unit,
    onSelectNewModelFileClick: () -> Unit,
    onBackToMainMenuClick: () -> Unit,
) {
    val context = LocalContext.current
    val currChat by remember { viewModel.currChatState }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(currChat) { viewModel.loadModel() }
    SmollAITheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerUI(
                    viewModel,
                    onItemClick = { chat ->
                        viewModel.switchChat(chat)
                        scope.launch { drawerState.close() }
                    },
                )
            },
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (androidx.compose.foundation.isSystemInDarkTheme()) SmollAIBackgroundDark else SmollAIBackgroundLight
                    ),
                topBar = {
                    TopAppBar(
                        title = {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                AppBarTitleText(
                                    currChat?.name ?: stringResource(R.string.untitled),
                                    modifier = Modifier,
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                     Box(modifier = Modifier
                                         .size(8.dp)
                                         .clip(CircleShape)
                                         .background(Color(0xFF10B981))) {
                                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                            val alpha by infiniteTransition.animateFloat(
                                                initialValue = 0.5f,
                                                targetValue = 1f,
                                                animationSpec = infiniteRepeatable(
                                                    animation = tween(1000),
                                                    repeatMode = RepeatMode.Reverse
                                                ),
                                                label = "alpha"
                                            )
                                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF10B981).copy(alpha = alpha)))
                                         }
                                     Spacer(modifier = Modifier.width(6.dp))
                                     Text(
                                         modifier = Modifier.padding(top = 2.dp),
                                         text = if (currChat != null && currChat?.llmModelId != -1L) {
                                            viewModel.modelsRepository
                                                .getModelFromId(currChat!!.llmModelId)
                                                ?.name ?: ""
                                        } else {
                                            "llama-3.2-1b-instruct-q4" // Default/Placeholder
                                        },
                                        fontFamily = AppFontFamily,
                                        fontSize = 11.sp,
                                        color = SmollAIPrimary,
                                        letterSpacing = 1.sp
                                     )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = stringResource(R.string.view_chats_desc),
                                    tint = Color.DarkGray,
                                )
                            }
                        },
                        actions = {
                            if (currChat != null) {
                                Box {
                                    IconButton(
                                        onClick = { viewModel.showMoreOptionsPopupState.value = true },
                                    ) {
                                        Icon(
                                            Icons.Default.MoreVert,                                                contentDescription = stringResource(R.string.options_desc),
                                        )
                                    }
                                    ChatMoreOptionsPopup(viewModel, onEditChatParamsClick, onSelectNewModelFileClick, onBackToMainMenuClick)
                                }
                            }
                        },
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .background(
                                if (androidx.compose.foundation.isSystemInDarkTheme()) SmollAIBackgroundDark else SmollAIBackgroundLight
                            ),
                ) {
                    if (currChat != null) {
                        ScreenUI(viewModel)
                    }
                }
            }
        }

        var showSelectModelsListDialog by remember { viewModel.showSelectModelListDialogState }
        if (showSelectModelsListDialog) {
            val modelsList by
                viewModel.modelsRepository.getAvailableModels().collectAsState(emptyList())
            SelectModelsList(
                onDismissRequest = { showSelectModelsListDialog = false },
                modelsList,
                onModelListItemClick = { model ->
                    viewModel.updateChatLLM(model.id)
                    viewModel.loadModel()
                    showSelectModelsListDialog = false
                },
                onModelDeleteClick = { model ->
                    viewModel.deleteModel(model.id)
                    Toast
                        .makeText(
                            viewModel.context,
                            context.getString(R.string.model_deleted, model.name),
                            Toast.LENGTH_LONG,
                        ).show()
                },
            )
        }
    }
}

@Composable
private fun ColumnScope.ScreenUI(viewModel: ChatScreenViewModel) {
    MessagesList(viewModel)
    MessageInput(viewModel)
}

@Composable
private fun ColumnScope.MessagesList(viewModel: ChatScreenViewModel) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    viewModel.getChatMessages()?.let { chatMessagesFlow ->
        val messages by chatMessagesFlow.collectAsState(emptyList())
        val isGeneratingResponse by remember { viewModel.isGeneratingResponse }
        val partialResponse by remember { viewModel.partialResponse }
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size)
            }
        }
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize().weight(1f)) {
            items(messages) { chatMessage ->
                MessageListItem(
                    viewModel.markwon.render(viewModel.markwon.parse(chatMessage.message)),
                    chatMessage.isUserMessage,
                    onCopyClicked = {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(context.getString(R.string.copy), chatMessage.message)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, context.getString(R.string.message_copied), Toast.LENGTH_SHORT).show()
                    },
                    onShareClicked = {
                        context.startActivity(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, chatMessage.message)
                            },
                        )
                    },
                )
            }
            if (isGeneratingResponse) {
                item {
                    if (partialResponse.isNotEmpty()) {
                        MessageListItem(
                            viewModel.markwon.render(viewModel.markwon.parse(partialResponse)),
                            false,
                            {},
                            {},
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .animateItem(),
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp), // Reducido de 24dp a 20dp
                                painter = painterResource(R.drawable.smollai_logo),
                                contentDescription = "smollai IA",
                                tint = Color.Unspecified,
                            )
                            Text(
                                text = "🧠 smollai está pensando...",
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                fontFamily = AppFontFamily,
                                fontSize = 12.sp,
                                color = SmollAIPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.MessageListItem(
    messageStr: Spanned,
    isUserMessage: Boolean,
    onCopyClicked: () -> Unit,
    onShareClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isUserMessage) {
        Row(modifier = modifier.fillMaxWidth().animateItem()) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(20.dp), // Reducido para ser más proporcional
                    painter = painterResource(id = R.drawable.smollai_logo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
            Column(modifier = Modifier.padding(start = 4.dp)) {
                ChatMessageText(
                    modifier =
                        Modifier
                             .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp))
                             .background(
                                 Color(0xFF1E293B).copy(alpha = 0.7f)
                             )
                             .border(
                                 width = 1.dp,
                                 color = SmollAIPrimary.copy(alpha = 0.2f), 
                                 shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
                             )
                            .padding(start = 4.dp) // Space for the left border strip effect
                            .drawBehind {
                                drawRect(
                                    color = SmollAIPrimary,
                                    topLeft = Offset.Zero,
                                    size = Size(4.dp.toPx(), size.height)
                                )
                            }
                            .padding(12.dp)
                            .fillMaxSize(),
                    textColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFFE2E8F0).toArgb() else Color(0xFF1E293B).toArgb(),
                    textSize = 15f,
                    message = messageStr,
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .clickable { onCopyClicked() }
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                             imageVector = Icons.Default.ContentCopy,
                             contentDescription = null,
                             modifier = Modifier.size(14.dp),
                             tint = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.copy).uppercase(),
                            fontSize = 10.sp,
                            fontFamily = AppFontFamily,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                            color = Color(0xFF94A3B8),
                            letterSpacing = 1.sp
                        )
                    }

                    Row(
                        modifier = Modifier.clickable { onShareClicked() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                         Icon(
                             imageVector = Icons.Default.Share,
                             contentDescription = null,
                             modifier = Modifier.size(14.dp),
                             tint = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.share).uppercase(),
                            fontSize = 10.sp,
                            fontFamily = AppFontFamily,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                            color = Color(0xFF94A3B8),
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth().animateItem(),
            horizontalArrangement = Arrangement.End,
        ) {
            ChatMessageText(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFFD946EF)
                                )
                            ), 
                            RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
                        )
                        .padding(12.dp)
                        .widthIn(max = 280.dp),
                textColor = android.graphics.Color.WHITE,
                textSize = 15f,
                message = messageStr,
            )
        }
    }
}

@Composable
private fun MessageInput(viewModel: ChatScreenViewModel) {
    val currChat by remember { viewModel.currChatState }
    if ((currChat?.llmModelId ?: -1L) == -1L) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.select_a_model),
            fontFamily = AppFontFamily,
        )
    } else {
        var questionText by remember { mutableStateOf("") }
        val isGeneratingResponse by remember { viewModel.isGeneratingResponse }
        val isInitializingModel by remember { viewModel.isInitializingModel }
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .border(0.dp, Color.Transparent),
                value = questionText,
                onValueChange = { questionText = it },
                shape = RoundedCornerShape(24.dp),
                colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else smollaiTextPrimary,
                        unfocusedTextColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else smollaiTextPrimary,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = if (androidx.compose.foundation.isSystemInDarkTheme())  Color(0xFF1E293B) else Color(0xFFF1F5F9),
                        unfocusedContainerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                placeholder = {
                    Text(
                        text =
                            if (isGeneratingResponse || isInitializingModel) {
                                stringResource(R.string.loading_model)
                            } else {
                                stringResource(R.string.ask_a_question)
                            },
                        color = Color(0xFF94A3B8)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { /* TODO: Implement Mic */ }) {
                          Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8)
                          )
                    }
                },
                keyboardOptions =
                    KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
            )
            Spacer(modifier = Modifier.width(12.dp))
            if (isGeneratingResponse || isInitializingModel) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SmollAIPrimary)
                    if (isGeneratingResponse) {
                        IconButton(onClick = { viewModel.stopGeneration() }) {
                            Icon(Icons.Default.Stop, contentDescription = stringResource(R.string.stop_desc), tint = smollaiError)
                        }
                    }
                }
            } else {
                IconButton(
                    enabled = questionText.isNotEmpty(),
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6366F1),
                                Color(0xFFD946EF)
                            )
                        ), 
                        CircleShape
                    ),
                    onClick = {
                        keyboardController?.hide()
                        viewModel.sendUserQuery(questionText)
                        questionText = ""
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.send_text_desc),
                        tint = Color.White,
                    )
                }
            }
        }
    }
}


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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.ui.components.AppBarTitleText
import io.smollai.smollaiandroid.ui.components.SmallLabelText
import io.smollai.smollaiandroid.ui.theme.AppFontFamily
import io.smollai.smollaiandroid.ui.theme.smollaiAndroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditChatSettingsScreen(
    viewModel: ChatScreenViewModel,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current
    viewModel.currChatState.value?.let { chat ->
        var chatName by remember { mutableStateOf(chat.name) }
        var systemPrompt by remember { mutableStateOf(chat.systemPrompt) }
        var minP by remember { mutableFloatStateOf(chat.minP) }
        var temperature by remember { mutableFloatStateOf(chat.temperature) }

        smollaiAndroidTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { AppBarTitleText(stringResource(R.string.edit_chat_settings)) },
                        navigationIcon = {
                            IconButton(onClick = { onBackClicked() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.navigate_back_desc),
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    viewModel.updateChat(
                                        chat.copy(
                                            name = chatName,
                                            systemPrompt = systemPrompt,
                                            minP = minP,
                                            temperature = temperature,
                                        ),
                                    )
                                    onBackClicked()
                                },
                            ) {
                                Icon(
                                    Icons.Default.Done,
                                    contentDescription = stringResource(R.string.save_settings_desc),
                                )
                            }
                        },
                    )
                },
            ) { paddingValues ->
                Column(
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState()),
                ) {
                    TextField(
                        colors =
                            TextFieldDefaults.colors(
                                focusedContainerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF1E293B) else Color.White,
                                unfocusedContainerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF1E293B) else Color.White,
                                focusedTextColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color.Black,
                                unfocusedTextColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color.Black,
                                focusedLabelColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B),
                                unfocusedLabelColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B)
                            ),
                        modifier = Modifier.fillMaxWidth(),
                        value = chatName,
                        onValueChange = { chatName = it },
                        label = { Text(stringResource(R.string.chat_name), fontFamily = AppFontFamily) },
                        textStyle = TextStyle(fontFamily = AppFontFamily),
                        keyboardOptions =
                            KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Words,
                            ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        colors =
                            TextFieldDefaults.colors(
                                focusedContainerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF1E293B) else Color.White,
                                unfocusedContainerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF1E293B) else Color.White,
                                focusedTextColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color.Black,
                                unfocusedTextColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color.Black,
                                focusedLabelColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B),
                                unfocusedLabelColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B)
                            ),
                        modifier = Modifier.fillMaxWidth(),
                        value = systemPrompt,
                        onValueChange = { systemPrompt = it },
                        label = { Text(stringResource(R.string.system_prompt), fontFamily = AppFontFamily) },
                        textStyle = TextStyle(fontFamily = AppFontFamily),
                        keyboardOptions =
                            KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences,
                            ),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (chat.isTask) {
                        SmallLabelText(
                            context.getString(R.string.updates_task_notice),
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        stringResource(R.string.min_p),
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = AppFontFamily,
                    )
                    Text(
                        stringResource(R.string.min_p_description),
                        fontFamily = AppFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Slider(
                        value = minP,
                        onValueChange = { minP = it },
                        valueRange = 0.0f..1.0f,
                        steps = 100,
                    )
                    Text(
                        text = "%.2f".format(minP),
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = AppFontFamily,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        stringResource(R.string.temperature),
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = AppFontFamily,
                    )
                    Text(
                        stringResource(R.string.temperature_description),
                        fontFamily = AppFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Slider(
                        value = temperature,
                        onValueChange = { temperature = it },
                        valueRange = 0.0f..5.0f,
                        steps = 50,
                    )
                    Text(
                        text = "%.1f".format(temperature),
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = AppFontFamily,
                    )
                }
            }
        }
    }
}

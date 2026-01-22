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

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.ui.components.createAlertDialog
import io.smollai.smollaiandroid.ui.theme.AppFontFamily

@Composable
fun ChatMoreOptionsPopup(
    viewModel: ChatScreenViewModel,
    onEditChatSettingsClick: () -> Unit,
    onSelectNewModelFileClick: () -> Unit = {},
    onBackToMainMenuClick: () -> Unit = {},
) {
    val context = LocalContext.current
    var expanded by remember { viewModel.showMoreOptionsPopupState }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.edit_chat_name_desc)) },
            text = { Text(stringResource(R.string.edit_chat_settings_menu), fontFamily = AppFontFamily) },
            onClick = {
                onEditChatSettingsClick()
                expanded = false
            },
        )
        // Opción "Inicio" eliminada por solicitud del usuario
        /*
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.back_to_main_menu_desc)) },
            text = { Text(stringResource(R.string.back_to_main_menu), fontFamily = AppFontFamily) },
            onClick = {
                onBackToMainMenuClick()
                expanded = false
            },
        )
        */
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Assistant, contentDescription = stringResource(R.string.change_model_desc)) },
            text = { Text(stringResource(R.string.change_model_menu), fontFamily = AppFontFamily) },
            onClick = {
                viewModel.showSelectModelListDialogState.value = true
                expanded = false
            },
        )
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.FolderOpen, contentDescription = stringResource(R.string.select_model_file_desc)) },
            text = { Text(stringResource(R.string.select_model_file_menu), fontFamily = AppFontFamily) },
            onClick = {
                onSelectNewModelFileClick()
                expanded = false
            },
        )
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_chat_desc)) },
            text = { Text(stringResource(R.string.delete_chat_menu), fontFamily = AppFontFamily) },
            onClick = {
                viewModel.currChatState.value?.let { chat ->
                    createAlertDialog(
                        dialogTitle = context.getString(R.string.delete_chat_menu),
                        dialogText = context.getString(R.string.delete_chat_confirmation, chat.name),
                        dialogPositiveButtonText = context.getString(R.string.delete),
                        dialogNegativeButtonText = context.getString(R.string.cancel),
                        onPositiveButtonClick = {
                            viewModel.deleteChat(chat)
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.chat_deleted, chat.name),
                                    Toast.LENGTH_LONG,
                                ).show()
                        },
                        onNegativeButtonClick = {},
                    )
                }
                expanded = false
            },
        )
    }
}

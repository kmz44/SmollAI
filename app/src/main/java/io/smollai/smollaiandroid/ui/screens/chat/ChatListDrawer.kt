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

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.data.Chat
import io.smollai.smollaiandroid.ui.components.AppAlertDialog
import io.smollai.smollaiandroid.ui.theme.AppAccentColor
import io.smollai.smollaiandroid.ui.theme.AppFontFamily

@Composable
fun DrawerUI(
    viewModel: ChatScreenViewModel,
    onItemClick: (Chat) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
                .windowInsetsPadding(WindowInsets.safeContent)
                .padding(8.dp)
                .requiredWidth(300.dp)
                .fillMaxHeight(),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = AppAccentColor),
                onClick = {
                    val chatCount = viewModel.chatsDB.getChatsCount()
                    val newChatId =
                        viewModel.chatsDB.addChat(chatName = context.getString(R.string.untitled) + " ${chatCount + 1}")
                    onItemClick(Chat(id = newChatId, name = context.getString(R.string.untitled) + " ${chatCount + 1}", systemPrompt = context.getString(R.string.you_are_helpful_assistant)))
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.new_chat_desc))
                Text(stringResource(R.string.new_chat), fontFamily = AppFontFamily)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(R.string.previous_chats),
            style = MaterialTheme.typography.labelSmall,
            color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChatsList(viewModel, onItemClick)
    }
    AppAlertDialog()
}

@Composable
private fun ColumnScope.ChatsList(
    viewModel: ChatScreenViewModel,
    onItemClick: (Chat) -> Unit,
) {
    val chats by viewModel.getChats().collectAsState(emptyList())
    LazyColumn(modifier = Modifier.weight(1f)) {
        items(chats) { chat -> ChatListItem(chat, onItemClick) }
    }
}

@Composable
private fun LazyItemScope.ChatListItem(
    chat: Chat,
    onItemClick: (Chat) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(
                    if (androidx.compose.foundation.isSystemInDarkTheme()) 
                        Color(0xFF1E293B) 
                    else 
                        Color.White,
                    RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onItemClick(chat) }
                .animateItem(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                chat.name, 
                fontSize = 16.sp, 
                fontFamily = AppFontFamily,
                color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color.Black
            )
            Text(
                text = DateUtils.getRelativeTimeSpanString(chat.dateUsed.time).toString(),
                fontSize = 12.sp,
                fontFamily = AppFontFamily,
                color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B)
            )
        }
    }
}

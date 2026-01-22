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

package io.smollai.smollaiandroid.ui.screens.manage_tasks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.ui.theme.AppFontFamily

@Composable
fun TaskOptionsPopup(
    onDismiss: () -> Unit,
    onEditTaskClick: () -> Unit,
    onDeleteTaskClick: () -> Unit,
) {
    val context = LocalContext.current
    DropdownMenu(
        expanded = true,
        onDismissRequest = { onDismiss() },
    ) {
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = context.getString(R.string.edit_task_desc)) },
            text = { Text(context.getString(R.string.edit_task), fontFamily = AppFontFamily) },
            onClick = { onEditTaskClick() },
        )
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = context.getString(R.string.delete_task_desc)) },
            text = { Text(context.getString(R.string.delete_task), fontFamily = AppFontFamily) },
            onClick = { onDeleteTaskClick() },
        )
    }
}

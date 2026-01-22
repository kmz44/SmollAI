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

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.smollai.smollaiandroid.R
import io.smollai.smollaiandroid.data.LLMModel
import io.smollai.smollaiandroid.ui.components.DialogTitleText
import io.smollai.smollaiandroid.ui.components.SmallLabelText
import io.smollai.smollaiandroid.ui.components.createAlertDialog
import io.smollai.smollaiandroid.ui.screens.model_setup.ModernModelSetupActivity
import io.smollai.smollaiandroid.ui.theme.AppAccentColor
import io.smollai.smollaiandroid.ui.theme.AppFontFamily
import java.io.File

@Composable
fun SelectModelsList(
    onDismissRequest: () -> Unit,
    modelsList: List<LLMModel>,
    onModelListItemClick: (LLMModel) -> Unit,
    onModelDeleteClick: (LLMModel) -> Unit,
    showModelDeleteIcon: Boolean = true,
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier =
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DialogTitleText(text = context.getString(R.string.choose_model))
            SmallLabelText(
                context.getString(R.string.choose_model_description),
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(modelsList) {
                    ModelListItem(
                        model = it,
                        onModelListItemClick,
                        onModelDeleteClick,
                        showModelDeleteIcon,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    Intent(context, ModernModelSetupActivity::class.java).also {
                        it.putExtra("openChatScreen", true) // Redirigir al chat después de importar
                        context.startActivity(it)
                    }
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = context.getString(R.string.add_model_desc), tint = AppAccentColor)
                Text(context.getString(R.string.add_model), fontFamily = AppFontFamily)
            }
        }
    }
}

@Composable
private fun ModelListItem(
    model: LLMModel,
    onModelListItemClick: (LLMModel) -> Unit,
    onModelDeleteClick: (LLMModel) -> Unit,
    showModelDeleteIcon: Boolean,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.clickable { onModelListItemClick(model) }.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = model.name, fontSize = 12.sp, fontFamily = AppFontFamily, maxLines = 1)
            Text(
                text = "%.1f GB".format(File(model.path).length() / (1e+9)),
                fontSize = 10.sp,
                fontFamily = AppFontFamily,
                maxLines = 1,
            )
        }
        if (showModelDeleteIcon) {
            IconButton(
                onClick = {
                    createAlertDialog(
                        dialogTitle = context.getString(R.string.delete_model),
                        dialogText = context.getString(R.string.delete_model_confirmation, model.name),
                        dialogPositiveButtonText = context.getString(R.string.delete),
                        dialogNegativeButtonText = context.getString(R.string.cancel),
                        onPositiveButtonClick = { onModelDeleteClick(model) },
                        onNegativeButtonClick = {},
                    )
                },
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = context.getString(R.string.delete_model_desc),
                    tint = AppAccentColor,
                )
            }
        }
    }
}

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

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.smollai.smollaiandroid.data.Task
import io.smollai.smollaiandroid.data.TasksDB
import io.smollai.smollaiandroid.llm.ModelsRepository
import org.koin.android.annotation.KoinViewModel

// @KoinViewModel - Comentado porque no usamos tareas en smollai IA
class TasksViewModel(
    val modelsRepository: ModelsRepository,
    // val tasksDB: TasksDB, - Comentado porque no usamos tareas
) : ViewModel() {
    val showCreateTaskDialogState = mutableStateOf(false)
    val showEditTaskDialogState = mutableStateOf(false)
    val selectedTaskState = mutableStateOf<Task?>(null)

    fun addTask(
        name: String,
        systemPrompt: String,
        modelId: Long,
    ) {
        // tasksDB.addTask(name, systemPrompt, modelId) - Comentado porque no usamos tareas
    }

    fun updateTask(newTask: Task) {
        // tasksDB.updateTask(newTask) - Comentado porque no usamos tareas
    }

    fun deleteTask(taskId: Long) {
        // tasksDB.deleteTask(taskId) - Comentado porque no usamos tareas
    }
}

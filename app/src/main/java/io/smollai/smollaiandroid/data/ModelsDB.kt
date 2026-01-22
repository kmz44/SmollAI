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

package io.smollai.smollaiandroid.data

import io.objectbox.kotlin.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Single

@Single
class ModelsDB {
    private val modelsBox = ObjectBoxStore.store.boxFor(LLMModel::class.java)

    fun addModel(
        name: String,
        url: String,
        path: String,
    ): Long {
        val model = LLMModel(name = name, url = url, path = path)
        return modelsBox.put(model)
    }

    fun getModel(id: Long): LLMModel? = modelsBox.get(id)

    fun getModels(): Flow<List<LLMModel>> =
        modelsBox
            .query()
            .build()
            .flow()
            .flowOn(Dispatchers.IO)

    fun getModelsList(): List<LLMModel> = modelsBox.all

    fun deleteModel(id: Long) {
        modelsBox.remove(id)
    }
}

package fr.ratp.domain.use_cases

import androidx.paging.PagingData
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Word
import fr.ratp.domain.repositories.WordRepository
import kotlinx.coroutines.flow.Flow

open class GetWordsUseCase(private val repository: WordRepository) {
    companion object {
        const val LOAD_SIZE = 10
    }

    open operator fun invoke(query: Query): Flow<PagingData<Word>> =
        repository.loadWordList(query)
}

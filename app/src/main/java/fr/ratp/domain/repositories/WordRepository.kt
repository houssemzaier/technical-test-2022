package fr.ratp.domain.repositories

import androidx.paging.PagingData
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
      fun loadWordList(query: Query): Flow<PagingData<Word>>
}

package fr.ratp.infrastructure.repositories

import androidx.paging.PagingData
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Word
import fr.ratp.domain.repositories.WordRepository
import kotlinx.coroutines.flow.Flow

class WordRepositoryImpl(
    private val pagerProvider: PagerProvider,
) : WordRepository {

    override fun loadWordList(query: Query): Flow<PagingData<Word>> =
        pagerProvider.provide(query).flow
}

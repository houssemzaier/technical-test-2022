package fr.ratp.infrastructure.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Word
import fr.ratp.domain.use_cases.GetWordsUseCase
import fr.ratp.infrastructure.data_source.WordPagingSource
import kotlin.math.min

open class PagerProvider {
   open fun provide(query: Query): Pager<Int, Word> = Pager(
        config = PagingConfig(
            pageSize = GetWordsUseCase.LOAD_SIZE,
            enablePlaceholders = false,
            initialLoadSize = min(query.limit, GetWordsUseCase.LOAD_SIZE),
        ),
        pagingSourceFactory = {
            WordPagingSource(
                query = query,
            )
        },
    )
}

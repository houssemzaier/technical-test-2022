package fr.ratp.infrastructure.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import fr.ratp.domain.entities.Query
import fr.ratp.domain.use_cases.GetWordsUseCase
import fr.ratp.infrastructure.data_source.WordPagingSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WordRepositoryImplTest {

    private lateinit var sut: WordRepositoryImpl
    private val pagerProviderMock: PagerProvider = mock()

    @Before
    fun setUp() {
        sut = WordRepositoryImpl(pagerProviderMock)
    }

    @Test
    fun loadWordListTest() = runTest {
        whenever(pagerProviderMock.provide(Query.DEFAULT_QUERY)).thenReturn(
            Pager(
                config = PagingConfig(
                    pageSize = GetWordsUseCase.LOAD_SIZE,
                ),
                pagingSourceFactory = {
                    WordPagingSource(
                        query = Query.DEFAULT_QUERY,
                    )
                }
            )
        )

        sut.loadWordList(Query.DEFAULT_QUERY).first()

        verify(pagerProviderMock).provide(Query.DEFAULT_QUERY)
    }
}

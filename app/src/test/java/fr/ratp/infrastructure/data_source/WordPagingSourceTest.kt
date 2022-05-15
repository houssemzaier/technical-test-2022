package fr.ratp.infrastructure.data_source

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import fr.ratp.domain.entities.Query.Companion.DEFAULT_QUERY
import fr.ratp.domain.entities.Word
import fr.ratp.domain.use_cases.GetWordsUseCase
import fr.ratp.domain.use_cases.NumberTransformer
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WordPagingSourceTest {
    private lateinit var sut: WordPagingSource

    @Before
    fun setUp() {
    }

    @Test
    fun `check initial load result`() = runTest {
        sut = WordPagingSource(DEFAULT_QUERY)

        val loadResult = sut.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = GetWordsUseCase.LOAD_SIZE,
                placeholdersEnabled = false
            )
        )

        val loadResultExpected: PagingSource.LoadResult.Page<Int, Word> =
            PagingSource.LoadResult.Page(
                data = NumberTransformer.getWordListByRange(DEFAULT_QUERY, 1..10),
                prevKey = null,
                nextKey = 11,
            )

        assertThat(loadResult).isEqualTo(loadResultExpected)
    }

    @Test
    fun `check last load result`() = runTest {
        sut = WordPagingSource(DEFAULT_QUERY)

        val loadResult = sut.load(
            PagingSource.LoadParams.Refresh(
                key = DEFAULT_QUERY.limit,
                loadSize = GetWordsUseCase.LOAD_SIZE,
                placeholdersEnabled = false
            )
        )

        val loadResultExpected: PagingSource.LoadResult.Page<Int, Word> =
            PagingSource.LoadResult.Page(
                data = NumberTransformer.getWordListByRange(
                    DEFAULT_QUERY,
                    DEFAULT_QUERY.limit..DEFAULT_QUERY.limit,
                ),
                prevKey = DEFAULT_QUERY.limit-GetWordsUseCase.LOAD_SIZE,
                nextKey = null,
            )

        assertThat(loadResult).isEqualTo(loadResultExpected)
    }


    @Test
    fun `check in the middle load result`() = runTest {
        sut = WordPagingSource(DEFAULT_QUERY)
        val loadResult = sut.load(
            PagingSource.LoadParams.Refresh(
                key = 55,
                loadSize = GetWordsUseCase.LOAD_SIZE,
                placeholdersEnabled = false
            )
        )

        val loadResultExpected: PagingSource.LoadResult.Page<Int, Word> =
            PagingSource.LoadResult.Page(
                data = NumberTransformer.getWordListByRange(
                    DEFAULT_QUERY,
                    idsRange = 55 until 55+GetWordsUseCase.LOAD_SIZE,
                ),
                prevKey = 55 - GetWordsUseCase.LOAD_SIZE,
                nextKey = 55 + GetWordsUseCase.LOAD_SIZE ,
            )

        assertThat(loadResult).isEqualTo(loadResultExpected)
    }
}

package fr.ratp.domain.use_cases

import androidx.paging.PagingData
import fr.ratp.domain.entities.Query
import fr.ratp.domain.repositories.WordRepository
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetWordsUseCaseTest {

    private lateinit var sut: GetWordsUseCase
    private val repositoryMock: WordRepository = mock()

    @Before
    fun setUp() {
        sut = GetWordsUseCase(repositoryMock)
    }

    @Test
    fun invokeTest() {
        whenever(repositoryMock.loadWordList(Query.DEFAULT_QUERY)).thenReturn(flow {
            emit(PagingData.empty())
        })

        sut.invoke(Query.DEFAULT_QUERY)

        verify(repositoryMock).loadWordList(Query.DEFAULT_QUERY)
    }
}

package fr.ratp.presentation

import androidx.paging.PagingData
import fr.ratp.di.CoroutineProvider
import fr.ratp.domain.entities.Query
import fr.ratp.domain.use_cases.GetWordsUseCase
import fr.ratp.domain.use_cases.NumberTransformer
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WordViewModelTest {

    private lateinit var sut: WordViewModel
    private val getWordsUseCaseMock: GetWordsUseCase = mock()
    private val coroutineProviderMock: CoroutineProvider = mock()

    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun `verify that getWordsUseCase is getting the same query from viewModel`() = runTest  {

        val job = launch(dispatcher + CoroutineName("FAKE_VIEW_MODEL_SCOPE")) {
            val data = NumberTransformer.getWordListByRange(Query.DEFAULT_QUERY, 1..10)
            whenever(getWordsUseCaseMock.invoke(Query.DEFAULT_QUERY)).thenReturn(
               PagingData.from(data).asFlow()
             )
            whenever(coroutineProviderMock.provideViewModelScope(any<WordViewModel>())).thenReturn(
                this)
            whenever(coroutineProviderMock.provideDispatcherCpu()).thenReturn(dispatcher)

            sut = WordViewModel(getWordsUseCaseMock, coroutineProviderMock)
            sut.loadWords(Query.DEFAULT_QUERY.asFlow())

            sut.words.collect()

            verify(getWordsUseCaseMock).invoke(Query.DEFAULT_QUERY)
        }

         job.cancelAndJoin()
    }

    private fun <T> T.asFlow() = flowOf(this)
}


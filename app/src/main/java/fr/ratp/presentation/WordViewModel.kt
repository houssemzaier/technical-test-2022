package fr.ratp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import fr.ratp.di.CoroutineProvider
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Word
import fr.ratp.domain.use_cases.GetWordsUseCase
import fr.ratp.presentation.WordUiModel.Companion.toUiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WordViewModel(
    private val getWordsUseCase: GetWordsUseCase,
    private val coroutineProvider: CoroutineProvider,
) : ViewModel() {

    companion object {
        private val TAG = WordViewModel::class.java.simpleName
    }

    private val _words: MutableStateFlow<WordUiState> =
        MutableStateFlow(WordUiState.Success(PagingData.empty()))

    val words:  Flow<WordUiState> = _words.asStateFlow()

    private val querySharedFlow = MutableSharedFlow<Flow<Query>>()

    init {
        querySharedFlow
            .debounce(500L)
            .flatMapLatest { it }
            .distinctUntilChanged()
            .onEach {
                Log.i(TAG, "query $it")
            }
            .catch { cause ->
                Log.e(TAG, "an error was catch", cause)
                cause.localizedMessage?.let { _words.emit(WordUiState.Error(it)) }
            }
            .flatMapLatest {
                getWordsUseCase.invoke(it)
            }
            .map(::mapToUiPagingData)
            .cachedIn(coroutineProvider.provideViewModelScope(this))
            .flowOn(coroutineProvider.provideDispatcherCpu())
            .onEach {
                _words.emit(WordUiState.Success(it))
            }
            .launchIn(coroutineProvider.provideViewModelScope(this))

    }


    fun loadWords(query: Flow<Query>) = coroutineProvider.provideViewModelScope(this).launch {
        querySharedFlow.emit(query)
    }

    private fun mapToUiPagingData(pagingData: PagingData<Word>): PagingData<WordUiModel> =
        pagingData.map { word ->
            word.toUiModel()
        }

    sealed class WordUiState {
        data class Success(val wordUiModel: PagingData<WordUiModel>) : WordUiState()
        class Error(val errorMessage: String) : WordUiState()
    }
}

package fr.ratp.di

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import fr.ratp.domain.repositories.WordRepository
import fr.ratp.domain.use_cases.GetWordsUseCase
import fr.ratp.infrastructure.repositories.PagerProvider
import fr.ratp.infrastructure.repositories.WordRepositoryImpl
import fr.ratp.presentation.WordViewModel

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            val pagerProvider = PagerProvider()
            val wordRepository: WordRepository = WordRepositoryImpl(pagerProvider)
            val getWordsUseCase = GetWordsUseCase(wordRepository)

            @Suppress("UNCHECKED_CAST")
            return WordViewModel(getWordsUseCase, CoroutineProvider()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

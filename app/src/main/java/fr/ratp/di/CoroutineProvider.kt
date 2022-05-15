package fr.ratp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class CoroutineProvider {
    open fun provideDispatcherCpu(): CoroutineDispatcher = Dispatchers.Default

    open fun provideViewModelScope(viewModel: ViewModel): CoroutineScope = viewModel.viewModelScope
}

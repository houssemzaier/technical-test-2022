package fr.ratp.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState.Loading
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import fr.ratp.databinding.ActivityMainBinding
import fr.ratp.di.ViewModelFactory
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Query.Companion.DEFAULT_QUERY
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val textFieldInt1Flow = MutableStateFlow(DEFAULT_QUERY.firstNumber.toString())
    private val textFieldInt2Flow = MutableStateFlow(DEFAULT_QUERY.secondNumber.toString())
    private val textFieldLimitFlow = MutableStateFlow(DEFAULT_QUERY.limit.toString())

    private val textFieldString1Flow = MutableStateFlow(DEFAULT_QUERY.firstWord)
    private val textFieldString2Flow = MutableStateFlow(DEFAULT_QUERY.secondWord)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel: WordViewModel by viewModels {
            ViewModelFactory(owner = this@MainActivity)
        }

        setDefaultFormText(binding)
        val adapter = WordAdapter()
        binding.bindAdapter(adapter)
        binding.bindProgressIndicator(adapter)
        binding.observeInputEditTextForm()
        loadData(viewModel)
        collectAndDiplayWords(viewModel, adapter)
        handleErrorMessage(viewModel)
    }

    private fun collectAndDiplayWords(
        viewModel: WordViewModel,
        adapter: WordAdapter,
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.words.collectLatest { words: PagingData<WordUiModel> ->
                    adapter.submitData(words)
                }
            }
        }
    }

    private fun loadData(viewModel: WordViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val queryFlow = combine(
                    textFieldInt1Flow,
                    textFieldInt2Flow,
                    textFieldLimitFlow,
                    textFieldString1Flow,
                    textFieldString2Flow,
                ) { int1: String, int2: String, limit: String, string1: String, string2: String ->
                    Query(
                        firstNumber = int1.toInt(),
                        secondNumber = int2.toInt(),
                        limit = limit.toInt(),
                        firstWord = string1,
                        secondWord = string2,
                    )
                }
                viewModel.loadWords(queryFlow)
            }
        }
    }

    private fun setDefaultFormText(binding: ActivityMainBinding) {
        fun TextInputLayout.setDefaultText(text: String) {
            editText?.let {
                if (it.text.isEmpty())
                    it.setText(text)
            }
        }
        binding.form.apply {
            filledTextFieldInt1.setDefaultText(DEFAULT_QUERY.firstNumber.toString())
            filledTextFieldInt2.setDefaultText(DEFAULT_QUERY.secondNumber.toString())
            filledTextFieldLimit.setDefaultText(DEFAULT_QUERY.limit.toString())
            filledTextFieldString1.setDefaultText(DEFAULT_QUERY.firstWord)
            filledTextFieldString2.setDefaultText(DEFAULT_QUERY.secondWord)
        }
    }

    private fun handleErrorMessage(viewModel: WordViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorSharedFlow.collectLatest {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun ActivityMainBinding.observeInputEditTextForm() {
        fun TextInputLayout.observeAndEmit(textFieldFlow: MutableSharedFlow<String>) {
            this.editText?.doAfterTextChanged { editable ->
                editable?.let {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.CREATED) {
                            val string = it.toString()
                            if (string.isNotEmpty()) {
                                textFieldFlow.emit(string)
                            }
                        }
                    }
                }
            }
        }

        form.filledTextFieldInt1.observeAndEmit(textFieldInt1Flow)
        form.filledTextFieldInt2.observeAndEmit(textFieldInt2Flow)
        form.filledTextFieldLimit.observeAndEmit(textFieldLimitFlow)
        form.filledTextFieldString1.observeAndEmit(textFieldString1Flow)
        form.filledTextFieldString2.observeAndEmit(textFieldString2Flow)
    }

    private fun ActivityMainBinding.bindProgressIndicator(adapter: WordAdapter) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { loadStates ->
                    appendProgress.isVisible = loadStates.source.append is Loading
                }
            }
        }
    }

    private fun ActivityMainBinding.bindAdapter(adapter: WordAdapter) {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(list.context)
        val decoration = DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(decoration)
    }
}

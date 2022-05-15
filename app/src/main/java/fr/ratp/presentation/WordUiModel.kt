package fr.ratp.presentation

import fr.ratp.domain.entities.Word

data class WordUiModel(
    val text: String,
) {
    companion object {
        fun Word.toUiModel() = WordUiModel(text = this.text)
    }
}


package fr.ratp.domain.use_cases

import androidx.annotation.VisibleForTesting
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Word

object NumberTransformer {
    fun getWordListByRange(query: Query, idsRange: IntRange): List<Word> = idsRange.map { id ->
        Word(
            id = id,
            text = id.transform(
                query.firstNumber,
                query.secondNumber,
                query.firstWord,
                query.secondWord,
            ),
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun Int.transform(int1: Int, int2: Int, str1: String, str2: String): String = when {
        isMultipleOf(int1 * int2) -> str1 + str2
        isMultipleOf(int1) -> str1
        isMultipleOf(int2) -> str2
        else -> this.toString()
    }

    private fun Int.isMultipleOf(int: Int) = this.rem(int) == 0
}

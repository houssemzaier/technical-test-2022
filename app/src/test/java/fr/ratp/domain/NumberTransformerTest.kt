package fr.ratp.domain

import fr.ratp.domain.entities.Query.Companion.DEFAULT_QUERY
import fr.ratp.domain.use_cases.NumberTransformer.transform
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class NumberTransformerTest {

    @Test
    fun transformTest() {
        (1..16).map {
            it.transform(
                int1 = DEFAULT_QUERY.firstNumber,
                int2 = DEFAULT_QUERY.secondNumber,
                str1 = DEFAULT_QUERY.firstWord,
                str2 = DEFAULT_QUERY.secondWord,
            )
        }.toTypedArray().let {
            assertArrayEquals(it,
                arrayOf(
                    "1",
                    "2",
                    "fizz",
                    "4",
                    "buzz",
                    "fizz",
                    "7",
                    "8",
                    "fizz",
                    "buzz",
                    "11",
                    "fizz",
                    "13",
                    "14",
                    "fizzbuzz",
                    "16",
                )
            )
        }
    }
}

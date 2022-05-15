package fr.ratp.domain.entities

data class Query(
    val firstNumber: Int,
    val secondNumber: Int,
    val limit: Int,
    val firstWord: String,
    val secondWord: String,
) {
    companion object {
        val DEFAULT_QUERY = Query(
            firstNumber = 3,
            secondNumber = 5,
            limit = 1_000,
            firstWord = "fizz",
            secondWord = "buzz",
        )
    }
}

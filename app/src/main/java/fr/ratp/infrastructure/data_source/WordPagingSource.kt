package fr.ratp.infrastructure.data_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import fr.ratp.domain.entities.Query
import fr.ratp.domain.entities.Word
import fr.ratp.domain.use_cases.GetWordsUseCase
import fr.ratp.domain.use_cases.NumberTransformer
import kotlin.math.max
import kotlin.math.min

class WordPagingSource(
    private val query: Query,
) : PagingSource<Int, Word>() {

    private companion object {
        private const val STARTING_KEY = 1
        private val TAG = WordPagingSource::class.java.simpleName
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Word> {
        val startKey = params.key ?: STARTING_KEY
        val nextKey = getNextKeyOrNull(startKey + GetWordsUseCase.LOAD_SIZE, query.limit)

        //data range
        val idsRange = getIdsRange(startKey)

        Log.d(TAG, "loaded ids =>  $idsRange")

        val wordList = NumberTransformer.getWordListByRange(query, idsRange)

        return LoadResult.Page(
            data = wordList,
            prevKey = getPreviousKeyOrNull(startKey),
            nextKey = nextKey,
        )
    }

    /**
     * Makes sure that the range doesn't exceed the query's limit
     */
    private fun getIdsRange(startKey: Int) =
        startKey.until(min(startKey + GetWordsUseCase.LOAD_SIZE, query.limit.plus(1)))

    /**
     * Makes sure that NextKey doesn't exceed the query's limit
     */
    private fun getNextKeyOrNull(nextKey: Int, limit: Int): Int? =
        if (nextKey > limit) null
        else nextKey

    /**
     * Makes sure that previousKey is never less than [STARTING_KEY]
     */
    private fun getPreviousKeyOrNull(startKey: Int): Int? {
        if (startKey == STARTING_KEY) {
            return null
        }

        val validPrevKey = max(STARTING_KEY, startKey - GetWordsUseCase.LOAD_SIZE)
        return if (validPrevKey == STARTING_KEY) {
            null
        } else {
            validPrevKey
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Word>): Int? = null
}

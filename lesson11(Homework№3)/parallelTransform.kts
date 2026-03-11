package homework

import kotlinx.coroutines.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend fun <T, R> parallelTransform(
    items: List<T>,
    transform: suspend (T) -> R
): List<R> = withContext(Dispatchers.Default) {
    val deferreds = items.map { item ->
        async {
            transform(item)
        }
    }

    deferreds.awaitAll()
}

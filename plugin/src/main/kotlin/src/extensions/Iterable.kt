package src.extensions

/**
 * Compresses an iterable of objects in a single, line separated, string.
 */
fun <T> Iterable<T>.joinLines(
    transform: (T) -> String = { it.toString() }
) = this.joinToString(
    separator = "\n",
    transform = transform,
)

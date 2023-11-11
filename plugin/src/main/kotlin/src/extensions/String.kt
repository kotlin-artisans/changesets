package src.extensions

/**
 * Removes all matches found in the applied string.
 */
fun String.replace(match: String) = this.replace(match, "")
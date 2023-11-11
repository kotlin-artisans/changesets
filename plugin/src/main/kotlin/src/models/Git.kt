package src.models

import src.extensions.replace

/**
 * Builder like class to interface with Git command line.
 */
object Git {
    private val builder = Builder()

    /**
     * Adds a file to stage
     */
    fun add(path: String) = builder.add(path)

    /**
     *
     */
    class Builder {
        private val stage = arrayListOf<String>()

        fun add(path: String) = apply { stage.add(path) }

        fun commit(message: String) = chain(
            arrayListOf<() -> Int>()
                .apply {
                    addAll(stage.map { { systemCall("git add %%$it%%") } })
                    add { systemCall("git commit -m %%$message%%") }
                }
        ) {
            it != 0
        }
    }
}

/**
 * Executes a system call via [Runtime] API, blocking the result until the process finishes.
 */
private fun systemCall(command: String) = Runtime.getRuntime().exec(
    Regex("%%[^%]+%%|\\S+").findAll(command).map { it.value.replace("%%") }.toList().toTypedArray()
).waitFor()

/**
 * Executes a chain of calls until the result of the `terminate` predicate returns true or if it reaches
 * the end of the calls list.
 */
private fun <R> chain(
    calls: List<() -> R>,
    terminate: (R) -> Boolean
): Result<Unit> {
    for (call in calls) {
        val status = call()

        if (terminate(status)) {
            return Result.failure(
                IllegalStateException("call $call failed validation with status code: $status")
            )
        }
    }

    return Result.success(Unit)
}
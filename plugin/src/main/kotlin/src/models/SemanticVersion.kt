package src.models

import java.util.*

/**
 * Models a semantic version data and behaviour.
 */
data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int
) {

    /**
     * Creates a new version that is the result of incrementing the current version major.
     */
    fun incrementMajor() = this.copy(major = this.major + 1, minor = 0, patch = 0)

    /**
     * Creates a new version that is the result of incrementing the current version minor.
     */
    fun incrementMinor() = this.copy(minor = this.minor + 1, patch = 0)

    /**
     * Creates a new version that is the result of incrementing the current version patch.
     */
    fun incrementPatch() = this.copy(patch = patch + 1)

    /**
     * Represents the semantic version in its schema format (x.y.z).
     */
    override fun toString() = "$major.$minor.$patch"

    companion object {
        val regex = Regex("([0-9]+)\\.([0-9]+)\\.([0-9]+)")

        private const val REGEX_VALID_GROUP_MATCHES_COUNT = 4

        /**
         * Tries to create a [SemanticVersion] instance from a string. If parsing fails, an empty [Optional] instance
         * is returned.
         */
        fun of(value: String): Optional<SemanticVersion> {
            val matches = regex.matchEntire(value.trim())?.groupValues

            if (matches?.size != REGEX_VALID_GROUP_MATCHES_COUNT) {
                return Optional.empty()
            }

            return Optional.of(
                SemanticVersion(
                    major = matches[1].toInt(),
                    minor = matches[2].toInt(),
                    patch = matches[3].toInt(),
                )
            )
        }

        /**
         * Creates a [SemanticVersion] instance that starts at the very first version (0.0.0).
         */
        fun empty() = SemanticVersion(0, 0, 0)
    }
}

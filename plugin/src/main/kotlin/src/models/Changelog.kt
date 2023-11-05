package src.models

import src.extensions.joinLines
import java.time.LocalDate
import java.util.SortedSet

/**
 * Models a changelog file.
 */
data class Changelog(
    val releases: SortedSet<Release>
) {

    /**
     * Checks if the package this changelog is associated hasn't been released yet.
     */
    fun unreleased() = releases.size == 0

    /**
     * Returns the latest release found in the changelog.
     */
    fun latest(): Release = releases.first()

    /**
     * Includes a new release in the changelog.
     */
    fun include(release: Release) {
        releases.add(release)
    }

    /**
     * Returns the equivalent of a changelog file for this instance as a string.
     */
    override fun toString(): String {
        return releases.joinLines {
            """
                ## ${it.version} (${it.date})

                ${it.notes.joinLines()}

            """.trimIndent()
        }
    }

    companion object {

        /**
         * Parses the content of a changelog file.
         */
        fun parse(lines: List<String>): Changelog {
            val releases = arrayListOf<Release>()
            val releaseNotesHeaderRegex = Regex("## (${SemanticVersion.regex}) \\((.+)\\)")

            for (line in lines) {
                val match = releaseNotesHeaderRegex.matchEntire(line)

                if (match != null) {
                    val values = match.groupValues.drop(1)

                    val version = SemanticVersion.of(values.first()).get()
                    val date = LocalDate.parse(values.last())
                    val notes = arrayListOf<String>()

                    releases.add(
                        Release(version, date, notes)
                    )
                } else if (line.isNotEmpty()) {
                    releases.lastOrNull()?.let {
                        (it.notes as ArrayList<String>).add(line)
                    }
                }
            }

            return Changelog(
                sortedSetOf<Release>().apply {
                    addAll(releases)
                }
            )
        }
    }
}
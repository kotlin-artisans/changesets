package src.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import src.extensions.joinLines
import src.models.Changelog
import src.models.Git
import src.models.Release
import src.models.SemanticVersion
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Manages the release of a package
 */
open class ReleaseTask : DefaultTask() {

    /**
     * Whether the task should create a commit for CHANGELOG.md changes or not.
     *
     * Defaults to true.
     */
    @Input
    val commit: Boolean = true

    @TaskAction
    fun execute() {
        val changelogFile = project.rootProject.file("CHANGELOG.md")
        val changelogLines = changelogFile.extractLines()

        val changelog = Changelog.parse(changelogLines)
        val lastReleaseVersion = changelog.lastReleasedVersion()

        println(
            """
Last released version: $lastReleaseVersion

👉 Choose one of the following options:
- major (+)
- minor (-)
- patch (~)
            """.trimIndent()
        )

        val option = readln()

        val newVersion = when (option) {
            "+" -> lastReleaseVersion.incrementMajor()
            "-" -> lastReleaseVersion.incrementMinor()
            else -> lastReleaseVersion.incrementPatch()
        }

        val releaseNotes = arrayListOf<String>()

        while (true) {
            println(
                """
✍️ Write this version release notes (double enter to finish):
${releaseNotes.joinLines()}
""".trim()
            )

            releaseNotes.add(readln())

            if (releaseNotes.last().isEmpty()) {
                break
            }
        }

        val release = buildNewRelease(newVersion, releaseNotes)

        println(
            """
${release.version}

${release.notes.joinLines()}

🤚 Proceed to release version? (Y/n)
            """.trimIndent()
        )

        val isToReleaseNewVersion = !readln().lowercase(Locale.getDefault()).startsWith("n")

        if (isToReleaseNewVersion) {
            triggerNewRelease(changelog, changelogFile, release)
        }
    }

    /**
     * Extracts the lines of a file, defaulting to an empty list if the file does not exist.
     */
    private fun File.extractLines() = when (exists()) {
        true -> readLines()
        false -> arrayListOf<String>().apply {
            println("$name does not exist! Creating after task execution...")
        }
    }

    /**
     * Resolves the last version released in a [Changelog] instance, defaulting
     * to [SemanticVersion.empty] if the changelog hasn't been released yet.
     */
    private fun Changelog.lastReleasedVersion() = when (unreleased()) {
        false -> latest().version
        true -> SemanticVersion.empty()
    }

    /**
     * Builds a [Release] instance targeting a specific [SemanticVersion] and including a set
     * of release notes, being related at the current date.
     */
    private fun buildNewRelease(
        version: SemanticVersion,
        notes: ArrayList<String>
    ) = Release(
        version = version,
        date = LocalDate.now(),
        notes = notes.apply {
            if (notes.size == 1) {
                add("No release notes included in this release.")
            }
        }.filter { it.isNotEmpty() },
    )

    /**
     * Triggers the release of a new version for a changelog file.
     */
    private fun triggerNewRelease(
        changelog: Changelog,
        changelogFile: File,
        release: Release,
    ) {
        changelog.include(release)
        changelogFile.writeText(changelog.toString())

        if (commit) {
            Git.add(changelogFile.path)
                .commit("docs(changeset): release notes for version ${release.version}")
                .onFailure { it.printStackTrace() }
        }
    }

    override fun getDescription() = "Manages the release of a package"
    override fun getGroup() = "Changesets"
}

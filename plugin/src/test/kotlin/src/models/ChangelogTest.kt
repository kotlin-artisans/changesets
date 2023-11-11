package src.models

import src.extensions.joinLines
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChangelogTest {
    @Test
    fun `unreleased returns true if changelog does not contain releases`() {
        // Arrange
        val changelog = Changelog(sortedSetOf())

        // Act
        val unreleased = changelog.unreleased()

        // Assert
        assertTrue(unreleased)
    }

    @Test
    fun `unreleased returns false if changelog contains at least one release`() {
        // Arrange
        val changelog = Changelog(
            sortedSetOf(
                Release(
                    version = SemanticVersion.empty(),
                    date = LocalDate.now(),
                    notes = arrayListOf()
                )
            )
        )

        // Act
        val unreleased = changelog.unreleased()

        // Assert
        assertFalse(unreleased)
    }

    @Test
    fun `latest returns the most recent release even if changelog is created with unordered releases`() {
        // Arrange
        val oldestRelease = Release(
            version = SemanticVersion.empty(),
            date = LocalDate.now(),
            notes = arrayListOf()
        )

        val newestRelease = oldestRelease.copy(
            version = oldestRelease.version.incrementMajor()
        )

        val changelog = Changelog(
            sortedSetOf(
                oldestRelease,
                newestRelease
            )
        )

        // Act
        val latest = changelog.latest()

        // Assert
        assertEquals(
            newestRelease,
            latest,
        )
    }

    @Test
    fun `string representation of changelog matches changelog file pattern`() {
        // Arrange
        val oldestRelease = Release(
            version = SemanticVersion.empty(),
            date = LocalDate.now(),
            notes = arrayListOf(
                "a cool but powerful release note"
            )
        )

        val newestRelease = oldestRelease.copy(
            version = oldestRelease.version.incrementMajor()
        )

        val changelog = Changelog(
            sortedSetOf(
                oldestRelease,
                newestRelease
            )
        )

        // Act
        val toString = changelog.toString()

        // Assert
        assertEquals(
            """
                ## ${newestRelease.version} (${newestRelease.date})

                ${newestRelease.notes.joinLines()}

                ## ${oldestRelease.version} (${oldestRelease.date})

                ${oldestRelease.notes.joinLines()}

            """.trimIndent(),
            toString,
        )
    }

    @Test
    fun `parse returns empty changelog if content is empty`() {
        // Arrange
        val lines = arrayListOf<String>()

        // Act
        val changelog = Changelog.parse(lines)

        // Assert
        assertTrue(changelog.releases.isEmpty())
    }

    @Test
    fun `parse returns non empty changelog if content contains a release in a valid format`() {
        // Arrange
        val lines = arrayListOf(
            "## 0.0.1 (1970-01-01)",
            "",
            "a cool but powerful release note"
        )

        // Act
        val changelog = Changelog.parse(lines)

        // Assert
        assertEquals(
            Changelog(
                sortedSetOf(
                    Release(
                        version = SemanticVersion(0, 0, 1),
                        date = LocalDate.of(1970, 1, 1),
                        notes = arrayListOf("a cool but powerful release note")
                    )
                )
            ),
            changelog
        )
    }

    @Test
    fun `parse cannot parse release entries that don't match the valid format`() {
        // Arrange
        val lines = arrayListOf(
            "### 0.0.1 (1970-01-01)",
            "",
            "a cool but powerful release note"
        )

        // Act
        val changelog = Changelog.parse(lines)

        // Assert
        assertEquals(
            Changelog(
                sortedSetOf()
            ),
            changelog
        )
    }

    @Test
    fun `parse returns all recognized changelog entries`() {
        // Arrange
        val lines = arrayListOf(
            "## 0.0.3 (1970-01-01)",
            "",
            "a cool but powerful release note",
            "",
            "## 0.0.2",
            "",
            "a cool but powerful release note",
            "## 0.0.1 (1970-01-01)",
            "a cool but powerful release note"
        )

        // Act
        val changelog = Changelog.parse(lines)

        // Assert
        assertEquals(
            Changelog(
                sortedSetOf(
                    Release(
                        version = SemanticVersion(0, 0, 3),
                        date = LocalDate.of(1970, 1, 1),
                        notes = arrayListOf("a cool but powerful release note")
                    ),
                    Release(
                        version = SemanticVersion(0, 0, 1),
                        date = LocalDate.of(1970, 1, 1),
                        notes = arrayListOf("a cool but powerful release note")
                    )
                )
            ),
            changelog
        )
    }
}

package src.models

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SemanticVersionTest {
    @Test
    fun `incrementMajor resets minor and patch values while incrementing the major one`() {
        // Arrange
        val version = SemanticVersion(1, 2, 3)

        // Act
        val incremented = version.incrementMajor()

        // Assert
        assertEquals(
            SemanticVersion(2, 0, 0),
            incremented,
        )
    }

    @Test
    fun `incrementMinor keeps major value, resets patch value while incrementing the minor one`() {
        // Arrange
        val version = SemanticVersion(1, 2, 3)

        // Act
        val incremented = version.incrementMinor()

        // Assert
        assertEquals(
            SemanticVersion(1, 3, 0),
            incremented,
        )
    }

    @Test
    fun `incrementPatch keeps major and minor values, while incrementing the patch one`() {
        // Arrange
        val version = SemanticVersion(1, 2, 3)

        // Act
        val incremented = version.incrementPatch()

        // Assert
        assertEquals(
            SemanticVersion(1, 2, 4),
            incremented,
        )
    }

    @Test
    fun `of is able to parse semantic version string`() {
        // Arrange
        val value = "1.2.3"

        // Act
        val version = SemanticVersion.of(value)

        // Assert
        assertEquals(
            Optional.of(SemanticVersion(1, 2, 3)),
            version,
        )
    }

    @Test
    fun `of is able to parse semantic version string that contains prefix spaces`() {
        // Arrange
        val value = "    1.2.3"

        // Act
        val version = SemanticVersion.of(value)

        // Assert
        assertEquals(
            Optional.of(SemanticVersion(1, 2, 3)),
            version,
        )
    }

    @Test
    fun `of is able to parse semantic version string that contains suffix spaces`() {
        // Arrange
        val value = "    1.2.3      "

        // Act
        val version = SemanticVersion.of(value)

        // Assert
        assertEquals(
            Optional.of(SemanticVersion(1, 2, 3)),
            version,
        )
    }
}
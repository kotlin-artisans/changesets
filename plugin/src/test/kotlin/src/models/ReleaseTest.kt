package src.models

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReleaseTest {
    @Test
    fun `two instances are equal if on the same version`() {
        // Arrange
        val releaseX = Release(
            version = SemanticVersion.empty(),
            date = LocalDate.now(),
            notes = arrayListOf()
        )

        val releaseY = releaseX.copy(
            date = releaseX.date.plusDays(1)
        )

        // Act
        val same = releaseX == releaseY

        // Assert
        assertTrue(same)
    }

    @Test
    fun `two instances are different if not on the same version`() {
        // Arrange
        val releaseX = Release(
            version = SemanticVersion.empty(),
            date = LocalDate.now(),
            notes = arrayListOf()
        )

        val releaseY = releaseX.copy(
            version = releaseX.version.incrementMajor()
        )

        // Act
        val same = releaseX == releaseY

        // Assert
        assertFalse(same)
    }
}

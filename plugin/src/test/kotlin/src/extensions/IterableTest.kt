package src.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class IterableTest {
    @Test
    fun `joinLines compresses list into expected string`() {
        // Arrange
        val lines = arrayListOf("This line goes above", "This line goes below")

        // Act
        val joined = lines.joinLines()

        // Assert
        assertEquals(
            """
            ${lines[0]}
            ${lines[1]}
            """.trimIndent(),
            joined
        )
    }
}

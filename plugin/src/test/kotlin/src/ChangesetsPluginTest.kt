/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package src

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'src.greeting' plugin.
 */
class ChangesetsPluginTest {
    @Test fun `release task is registered`() {
        // Arrange
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("changesets")

        // Act
        val task = project.tasks.findByName("release")

        // Assert
        assertNotNull(task)
    }
}

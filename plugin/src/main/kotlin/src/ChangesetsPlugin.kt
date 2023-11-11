/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package src

import org.gradle.api.Plugin
import org.gradle.api.Project
import src.tasks.ReleaseTask

/**
 * Definition of Changesets plugin tasks.
 */
class ChangesetsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("release", ReleaseTask::class.java)
    }
}

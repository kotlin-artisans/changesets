import org.jetbrains.kotlin.incremental.createDirectory

plugins {
    `java-gradle-plugin`

    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
    id("org.jetbrains.dokka") version "1.9.10"

    // Applying own plugin to automate release process
    //id("io.github.kotlin-artisans.changesets") version "0.0.3"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    group = "io.github.kotlin-artisans"
    version = "0.0.3"

    val changesets by plugins.creating {
        id = "$group.changesets"
        displayName = "Plugin for managing releases of artifacts"
        description = "Plugin for managing releases of artifacts"
        tags.addAll(
            listOf("release", "publish", "changesets", "jitpack")
        )
        website.set("https://github.com/kotlin-artisans/changesets")
        vcsUrl.set("https://github.com/kotlin-artisans/changesets")

        implementationClass = "src.ChangesetsPlugin"
    }
}

tasks {
    detekt {
        config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        parallel = true
        autoCorrect = true
    }

    dokkaHtml {
        dokkaSourceSets {
            val name = gradlePlugin.plugins.names.first()
            val readmeFile = File.createTempFile("dokka", "readme").apply {
                writeText(
                    rootProject
                        .file("README.md")
                        .readLines()
                        .drop(1)
                        .fold("# Module $name") { p, c -> "$p\n$c" }
                )
            }

            configureEach {
                moduleName.set(name)
                includes.from(readmeFile)
            }
        }

        doLast {
            copy {
                from(rootProject.file("art"))
                into("$buildDir/dokka/html/art")
            }
        }
    }

    test {
        useJUnitPlatform()
    }

    val dokkaHtmlPublish by creating {
        dependsOn(dokkaHtml)

        val tempDirectory = temporaryDir

        doFirst {
            tempDirectory.createDirectory()
        }

        onlyIf {
            copy {
                dependsOn(build)
                from("$buildDir/dokka/html")
                into(tempDirectory)
            }

            copy {
                dependsOn(build)
                from(rootProject.file(".git"))
                into("$tempDirectory/.git")
            }

            exec {
                workingDir = tempDirectory
                executable = "git"
                args = arrayListOf("checkout", "-B", "gh-pages")
            }

            exec {
                workingDir = tempDirectory
                executable = "git"
                args = arrayListOf("add", ".")
            }

            exec {
                workingDir = tempDirectory
                executable = "git"
                args = arrayListOf("commit", "-m", "docs: publish dokka github pages")
            }

            exec {
                workingDir = tempDirectory
                executable = "git"
                args = arrayListOf("push", "--no-verify", "--force", "--set-upstream", "origin", "gh-pages")
            }

            true
        }

        doLast {
            tempDirectory.delete()
        }
    }
}
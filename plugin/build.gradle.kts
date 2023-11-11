plugins {
    `java-gradle-plugin`

    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("com.gradle.plugin-publish") version "1.2.1"

    // Applying own plugin to automate release process
    id("io.github.kotlin-artisans.changesets") version "0.0.3"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    group = "io.github.kotlin-artisans"
    version = "0.0.3"

    val changesets by plugins.creating {
        id = "io.github.kotlin-artisans.changesets"
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

tasks.named<Test>("test") {
    useJUnitPlatform()
}

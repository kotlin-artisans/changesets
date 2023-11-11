# changesets

Gradle plugin that manages the release process of artifacts 🚀

<p align="center"><img src="art/logo.svg" alt="svg of library logo" style="width: 30%"></p>

Note: this is an attempt to port most of the features provided in
the [changesets](https://github.com/changesets/changesets) tool, which is available only on Node.js
environments.

---

## How to use

To use this plugin in your project, you must first include the required repositories to pull the
plugin (`settings.gradle)`:

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven { url "https://jitpack.io" }
    }
}
```

Local and Jitpack repositories are optional, include them if you want to build from source (local) and snapshot
versions (Jitpack).

After that, include the plugin in your root `build.gradle`:

```groovy
plugins {
    id("changesets") version "0.0.1"
}
```

If all goes well, you can start using the tasks available in `changesets` group:

```
./gradlew tasks

...
Changesets tasks
----------------
release - Manages the release of a package
...
```

## Features

- Release the package by bumping the version

---

## Bugs and Contributions

Found any bug (including typos) in the package? Do you have any suggestion
or feature to include for future releases? Please create an issue via
GitHub in order to track each contribution. Also, pull requests are very
welcome!

To contribute, start by setting up your local development environment. The [setup.md](docs/setup.md)
document will onboard you on how to do so!

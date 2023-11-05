package src.models

import java.time.LocalDate

/**
 * Models a package release information.
 */
data class Release(
    val version: SemanticVersion,
    val date: LocalDate,
    val notes: ReleaseNotes,
): Comparable<Release> {

    /**
     * Version is the identity of a [Release], so an instance hash code is given by the [version] property.
     */
    override fun hashCode() = version.hashCode()

    /**
     * Provides compatibility to data structures that use [Comparable] to sort elements.
     */
    override fun compareTo(other: Release) = -this.version.hashCode().compareTo(other.version.hashCode())

    override fun equals(other: Any?) = hashCode() == other.hashCode()
}
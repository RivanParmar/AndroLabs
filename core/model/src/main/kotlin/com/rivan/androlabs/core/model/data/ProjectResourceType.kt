package com.rivan.androlabs.core.model.data

enum class ProjectResourceType(
    val serializedName: String,
    // TODO: descriptions should probably be string resources
    val description: String
) {
    Lab(
        serializedName = "Lab",
        description = "An Android Lab project."
    ),
    Project(
        serializedName = "Project",
        description = "An Android app project."
    ),
    // TODO: Remove this if creating libraries won't be supported!
    Library(
        serializedName = "Library",
        description = "An Android library project."
    ),
    JetpackCompose(
        serializedName = "Jetpack Compose",
        description = "A Jetpack Compose Android app project."
    ),
    Unknown(
        serializedName = "Unknown",
        description = "Unknown"
    )
}

fun String?.asProjectResourceType() = when (this) {
    null -> ProjectResourceType.Unknown
    else -> ProjectResourceType.values()
        .firstOrNull { type -> type.serializedName == this}
        ?: ProjectResourceType.Unknown
}
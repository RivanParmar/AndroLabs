pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "androlabs"
include(":app")

include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:testing")
include(":core:ui")

include(":feature:editor")
include(":feature:home")
include(":feature:nlw")
include(":feature:npw")
include(":feature:settings")

include(":sync:work")

include(":build-tools:gradle-base-services-groovy")
include(":build-tools:gradle-build-operations")

include(":build-tools:gradle-core-runtime:base-annotations")
include(":build-tools:gradle-core-runtime:base-services")
include(":build-tools:gradle-core-runtime:build-option")
include(":build-tools:gradle-core-runtime:cli")
include(":build-tools:gradle-core-runtime:file-temp")
include(":build-tools:gradle-core-runtime:files")
include(":build-tools:gradle-core-runtime:functional")
include(":build-tools:gradle-core-runtime:messaging")

/*include(":build-tools:javac")
include(":build-tools:jaxp:internal")
include(":build-tools:jaxp:xml")
include(":build-tools:kotlinc")

include(":build-tools:builder-core")
include(":build-tools:build-logic")*/

include(":build-tools:project")*/

include(":platform:wizard:template-api")
include(":platform:wizard:template-impl")

include(":platform:core-api")

include(":platform:project-api")

include(":platform:language-api")
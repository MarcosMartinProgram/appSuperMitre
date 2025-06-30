pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.mercadopago.com")

        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.mercadopago.com")
    }
}

rootProject.name = "SuperMitreApp"
include(":app")

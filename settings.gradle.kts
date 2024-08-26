pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://maven.google.com") }
//        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://maven.google.com") }
//        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Cfm_Joy_Manager"
include(":app")

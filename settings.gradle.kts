pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		maven("https://repo.dairy.foundation/releases")
	}
}

includeBuild("../routine") {
	dependencySubstitution {
		substitute(module("org.fishnpotatoes.routine:core")).using(project(":core"))
		substitute(module("org.fishnpotatoes.routine:util")).using(project(":util"))
		substitute(module("org.fishnpotatoes.routine:ftc")).using(project(":ftc"))
	}
}
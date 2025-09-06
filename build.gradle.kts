plugins {
    kotlin("kapt") version "2.1.0"
    id("dev.frozenmilk.teamcode") version "10.1.1-0.1.3"
}

ftc {
    kotlin
}

repositories {
    maven("https://maven.brott.dev/")
}

dependencies {
    implementation("com.acmerobotics.dashboard:dashboard:0.4.17") {
        exclude("org.firstinspires.ftc")
    }
    implementation("org.fishnpotatoes.routine:core")
    implementation("org.fishnpotatoes.routine:util")
    implementation("org.fishnpotatoes.routine:ftc")
}

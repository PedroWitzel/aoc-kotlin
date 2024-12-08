import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.allopen") version "2.0.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.37"
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11"
    }
}

// jhipster-needle-gradle-imports

plugins {
    checkstyle
    id("java")
    id("org.jetbrains.intellij.platform") version "2.1.0"
    // jhipster-needle-gradle-plugins
}

// jhipster-needle-gradle-properties

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = libs.versions.checkstyle.get()
}

// jhipster-needle-gradle-plugins-configurations

repositories {
    intellijPlatform {
      defaultRepositories()
      mavenCentral()
    }
    mavenCentral()
    // jhipster-needle-gradle-repositories
}

val profiles = (project.findProperty("profiles") as String? ?: "")
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
// jhipster-needle-profile-activation

dependencies {

    intellijPlatform {
      intellijIdeaCommunity("2024.3")

      bundledPlugin("com.intellij.java")


      pluginVerifier()
      zipSigner()
      instrumentationTools()

    }

    // jhipster-needle-gradle-implementation-dependencies
    // jhipster-needle-gradle-compile-dependencies
    // jhipster-needle-gradle-runtime-dependencies
    testImplementation(libs.junit.engine)
    testImplementation(libs.junit.params)
    testImplementation(libs.assertj)
    testImplementation(libs.mockito)
    testImplementation(libs.junit.platform.console.standalone)
    // jhipster-needle-gradle-test-dependencies
}

sourceSets {
}

sourceSets {
  main {
    java.srcDirs("src/main")
  }
  test {
    java.srcDirs("src/test")
  }
}

// jhipster-needle-gradle-free-configuration-blocks

tasks.test {
    filter {
        includeTestsMatching("**Test*")
        excludeTestsMatching("**IT*")
        excludeTestsMatching("**CucumberTest*")
    }
    useJUnitPlatform()
    // jhipster-needle-gradle-tasks-test
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    shouldRunAfter("test")
    filter {
        includeTestsMatching("**IT*")
        includeTestsMatching("**CucumberTest*")
    }
    useJUnitPlatform()
}

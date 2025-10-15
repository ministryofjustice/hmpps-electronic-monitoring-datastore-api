import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "9.1.3"
  kotlin("plugin.spring") version "2.2.20"
  jacoco
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.apache.commons:commons-lang3:3.19.0")
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.7.0")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:5.5.0")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

  implementation("software.amazon.awssdk:athena:2.35.7")
  implementation("software.amazon.awssdk:sts:2.35.7")
  implementation("org.json:json:20250517")
  implementation("com.fasterxml.jackson.core:jackson-annotations:2.20")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
  implementation("com.fasterxml.jackson.core:jackson-core:2.20.0")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.0")
  implementation("io.zeko:zeko-sql-builder:1.5.6")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:1.7.0")
  testImplementation("org.wiremock:wiremock-standalone:3.13.1")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.35") {
    exclude(group = "io.swagger.core.v3")
  }
}

kotlin {
  jvmToolchain(21)
}

tasks {
  register<Test>("unitTest") {
    filter {
      excludeTestsMatching("uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration*")
    }
  }

  register<Test>("integrationTest") {
    description = "Runs the integration tests, make sure that dependencies are available first by running `make serve`."
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["main"].output + configurations["testRuntimeClasspath"] + sourceSets["test"].output
    filter {
      includeTestsMatching("uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration*")
    }
  }

  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_21
    }
  }

  withType<Test> {
    finalizedBy("jacocoTestReport") // report is always generated after tests run
  }

  named<JacocoReport>("jacocoTestReport") {
    dependsOn("test")

    reports { html.required.set(true) }

    classDirectories.setFrom(fileTree(projectDir) { include("build/classes/kotlin/main/**") })
    sourceDirectories.setFrom(files("src/main/kotlin"))
    executionData.setFrom(files("build/jacoco/test.exec"))
  }

  testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
  }
}

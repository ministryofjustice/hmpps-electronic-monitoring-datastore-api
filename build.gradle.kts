plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "9.1.1"
  kotlin("plugin.spring") version "2.2.20"
  jacoco
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.apache.commons:commons-lang3:3.19.0")
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.7.0")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:5.4.11")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
  implementation("software.amazon.awssdk:athena:2.34.9")
  implementation("software.amazon.awssdk:sts:2.34.9")
  implementation("org.json:json:20250517")
  implementation("com.fasterxml.jackson.core:jackson-annotations:2.20")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
  implementation("com.fasterxml.jackson.core:jackson-core:2.20.0")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.0")
  implementation("io.zeko:zeko-sql-builder:1.5.6")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:1.7.0")
  testImplementation("org.wiremock:wiremock-standalone:3.13.1")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.34") {
    exclude(group = "io.swagger.core.v3")
  }
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
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
}

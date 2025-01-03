plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "6.1.2"
  kotlin("plugin.spring") version "2.1.0"
  jacoco
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.1.1")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:5.2.2")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")
  implementation("software.amazon.awssdk:athena:2.29.45")
  implementation("software.amazon.awssdk:sts:2.29.45")
  implementation("org.json:json:20240303")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:1.1.1")
  testImplementation("org.wiremock:wiremock-standalone:3.10.0")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.24") {
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

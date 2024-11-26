plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "6.0.8"
  kotlin("plugin.spring") version "2.0.21"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.0.8")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
//  implementation("aws.sdk.kotlin:s3:1.0.0")
  implementation("software.amazon.awssdk:athena:2.29.17")
  implementation("software.amazon.awssdk:sts:2.29.20")

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:1.0.8")
  testImplementation("org.wiremock:wiremock-standalone:3.9.2")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.23") {
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
}

plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "10.0.3"
  kotlin("plugin.spring") version "2.3.10"
  kotlin("plugin.jpa") version "2.3.10"
  id("jacoco")
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.apache.commons:commons-lang3:3.20.0")

  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:2.0.0")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-webclient")

  implementation("org.springframework.boot:spring-boot-starter-flyway")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:6.0.1")

  implementation("org.springframework.boot:spring-boot-starter-data-jpa:4.0.2")

  implementation("software.amazon.awssdk:athena:2.41.24")
  implementation("software.amazon.awssdk:sts:2.41.24")
  implementation("io.zeko:zeko-sql-builder:1.5.6")

  runtimeOnly("org.postgresql:postgresql:42.7.9")
  runtimeOnly("org.flywaydb:flyway-core")
  runtimeOnly("org.flywaydb:flyway-database-postgresql")

  testImplementation("com.h2database:h2:2.4.240")
  testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:2.0.0")
  testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
  testImplementation("org.wiremock:wiremock-standalone:3.13.2")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.37") {
    exclude(group = "io.swagger.core.v3")
  }

  testImplementation("org.mockito:mockito-core:5.21.0")
  testImplementation("org.mockito.kotlin:mockito-kotlin:6.2.3")

  implementation("org.json:json:20250517")
  implementation("com.fasterxml.jackson.core:jackson-annotations:2.21")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.21.0")
  implementation("com.fasterxml.jackson.core:jackson-core:2.21.0")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.21.0")

  testImplementation("org.testcontainers:postgresql:1.21.4")
  testImplementation("org.testcontainers:localstack:1.21.4")

  testImplementation(kotlin("test"))
}

kotlin {
  jvmToolchain(25)
  noArg {
    annotation("jakarta.persistence.Entity")
  }

  compilerOptions {
    freeCompilerArgs.addAll("-Xannotation-default-target=param-property")
  }
}

tasks {
  register<Test>("unitTest") {
    group = "verification"
    description = "Runs unit tests excluding integration tests"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["main"].output + configurations["testRuntimeClasspath"] + sourceSets["test"].output
    filter { excludeTestsMatching("uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration*") }

    extensions.configure(JacocoTaskExtension::class) {
      destinationFile = layout.buildDirectory.file("jacoco/unitTest.exec").get().asFile
    }
  }

  register<Test>("integrationTest") {
    group = "verification"
    description = "Runs the integration tests, make sure that dependencies are available first by running `make serve`."
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["main"].output + configurations["testRuntimeClasspath"] + sourceSets["test"].output
    filter { includeTestsMatching("uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration*") }

    extensions.configure(JacocoTaskExtension::class) {
      destinationFile = layout.buildDirectory.file("jacoco/integrationTest.exec").get().asFile
    }
  }

  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
  }

  testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
  }
}

tasks.register<JacocoReport>("jacocoUnitTestReport") {
  dependsOn("unitTest")
  executionData.setFrom(layout.buildDirectory.file("jacoco/unitTest.exec"))
  classDirectories.setFrom(sourceSets.main.get().output)
  sourceDirectories.setFrom(sourceSets.main.get().allSource)

  reports {
    html.required.set(true)
    html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/unit"))
    xml.required.set(true)
    xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/unit/jacoco.xml"))
  }

  doLast {
    val reportFile = reports.xml.outputLocation.get().asFile
    if (reportFile.exists()) {
      val content = reportFile.readText()
      val updatedContent = content.replaceFirst("name=\"${project.name}\"", "name=\"Unit Tests\"")
      reportFile.writeText(updatedContent)
    }
  }
}

tasks.register<JacocoReport>("jacocoTestIntegrationReport") {
  dependsOn("integrationTest")
  executionData.setFrom(layout.buildDirectory.file("jacoco/integrationTest.exec"))

  classDirectories.setFrom(sourceSets.main.get().output)
  sourceDirectories.setFrom(sourceSets.main.get().allSource)

  reports {
    html.required.set(true)
    html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/integration"))
    xml.required.set(true)
    xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/integration/jacoco.xml"))
  }

  doLast {
    val reportFile = reports.xml.outputLocation.get().asFile
    if (reportFile.exists()) {
      val content = reportFile.readText()
      val updatedContent = content.replaceFirst("name=\"${project.name}\"", "name=\"Integration Tests\"")
      reportFile.writeText(updatedContent)
    }
  }
}

tasks.register<JacocoReport>("combineJacocoReports") {
  dependsOn("jacocoUnitTestReport", "jacocoTestIntegrationReport")

  executionData(
    layout.buildDirectory.file("jacoco/unitTest.exec"),
    layout.buildDirectory.file("jacoco/integrationTest.exec"),
  )

  classDirectories.setFrom(sourceSets.main.get().output)
  sourceDirectories.setFrom(sourceSets.main.get().allSource)

  reports {
    html.required.set(true)
    html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/combined"))
    xml.required.set(true)
    xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/combined/jacoco.xml"))
  }

  doLast {
    val reportFile = reports.xml.outputLocation.get().asFile
    if (reportFile.exists()) {
      val content = reportFile.readText()
      val updatedContent = content.replaceFirst("name=\"${project.name}\"", "name=\"Combined Tests\"")
      reportFile.writeText(updatedContent)
    }
  }
}

tasks.named("check") {
  dependsOn("unitTest", "integrationTest", "combineJacocoReports")
}

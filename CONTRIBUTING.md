# raygun-spring-boot

## Setup

This project uses [google-java-format](https://github.com/google/google-java-format) through [Spotless plugin for Gradle](https://github.com/diffplug/spotless/tree/main/plugin-gradle). Please install the plugin in your IDE.

## Subprojects

This project is a Multi-Project Gradle project. To list all subprojects:

```sh
./gradlew projects
```

## Format

Each of the subproject has its own task for the Spotless plugin.

To check whether the current code comply with the formatter or not:

```sh
./gradlew raygun-spring-boot-starter:spotlessCheck
./gradlew raygun-spring-boot-starter-example:spotlessCheck
./gradlew raygun-spring-boot-starter-example-web:spotlessCheck
./gradlew raygun-spring-boot-starter-example-web-services:spotlessCheck
```

To apply the formatter:

```sh
./gradlew raygun-spring-boot-starter:spotlessApply
./gradlew raygun-spring-boot-starter-example:spotlessApply
./gradlew raygun-spring-boot-starter-example-web:spotlessApply
./gradlew raygun-spring-boot-starter-example-web-services:spotlessApply
```

## Test

```sh
./gradlew raygun-spring-boot-starter:test
./gradlew raygun-spring-boot-starter-example:test
./gradlew raygun-spring-boot-starter-example-web:test
./gradlew raygun-spring-boot-starter-example-web-services:test
```

## Code Coverage Verification

We mostly care on the starter subproject code coverage verification.

```sh
./gradlew raygun-spring-boot-starter:test raygun-spring-boot-starter:jacocoTestReport raygun-spring-boot-starter:jacocoTestCoverageVerification
```

The code coverage verification task can also be run by changing the project name appropriately.

## Javadoc

Public API that is intended to be used in user's code must have Javadoc.

## Publishing

Check the Maven publication version. For snapshot, the version should have `-SNAPSHOT` suffix. For release, the version should not have any prefix. Please commit and push to publish a new version.

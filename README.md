# AzAuth

[![Java CI](https://img.shields.io/github/actions/workflow/status/Azuriom/AzAuth/build.yml?branch=master&style=flat-square)](https://github.com/Azuriom/AzAuth/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.azuriom/azauth.svg?label=Maven%20Central&style=flat-square)](https://search.maven.org/search?q=g:%22com.azuriom%22%20AND%20a:%22azauth%22)
[![Chat](https://img.shields.io/discord/625774284823986183?color=5865f2&label=Discord&logo=discord&logoColor=fff&style=flat-square)](https://azuriom.com/discord)

A Java implementation of the Azuriom Auth API.

## Installation

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.azuriom</groupId>
        <artifactId>azauth</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.azuriom:azauth:1.0.0'
}
```

## Usage

You can find how to use AzAuth on our [documentation](https://azuriom.com/docs/api-auth).

## Dependencies

AzAuth uses [Google Gson](https://github.com/google/gson) to deserialize Json.

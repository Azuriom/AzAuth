# AzAuth

[![Java CI](https://img.shields.io/github/workflow/status/Azuriom/AzAuth/Java%20CI?style=flat-square)](https://github.com/Azuriom/AzAuth/actions)
[![Language grade](https://img.shields.io/lgtm/grade/java/github/Azuriom/AzAuth?label=code%20quality&logo=lgtm&logoWidth=18&style=flat-square)](https://lgtm.com/projects/g/Azuriom/AzAuth/context:java)
[![Chat](https://img.shields.io/discord/625774284823986183?color=5865f2&label=Discord&logo=discord&logoColor=fff&style=flat-square)](https://azuriom.com/discord)

A Java implementation of the Azuriom Auth API.

## Installation

### Maven
```xml
<repositories>
    <repository>
        <id>sonatype-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>com.azuriom</groupId>
        <artifactId>azauth</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Gradle
```groovy
repositories {
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
}
```
```groovy
dependencies {
    implementation 'com.azuriom:azauth:1.0-SNAPSHOT'
}
```

## Usage

You can find how to use AzAuth on our [documentation](https://azuriom.com/docs/api-auth).

## Example

### With OpenLauncherLib

```java
public static void auth(String username, String password) throws AuthException {
    AuthClient authenticator = new AuthClient("<url>");

    authInfos = authenticator.login(username, password, () -> {
        // Called when 2FA is enabled
        String code = null;

        while (code == null || code.isEmpty()) {
            Container parentComponent = LauncherFrame.getInstance().getLauncherPanel();
            code = JOptionPane.showInputDialog(parentComponent, "Enter your 2FA code", "2FA", JOptionPane.PLAIN_MESSAGE);
        }

        return code;
    }, AuthInfos.class);
}
```

## Dependencies

AzAuth uses [Google Gson](https://github.com/google/gson) to deserialize Json.

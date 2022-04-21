## Gradle

Better way is using Gradle tooling
API, [Embedding Gradle using the Tooling API](https://docs.gradle.org/current/userguide/third_party_integration.html#embedding)

| Java version | First Gradle version to support it |
|--------------|------------------------------------|
| 8            | 2.0                                |
| 9            | 4.3                                |
| 10           | 4.7                                |
| 11           | 5.0                                |
| 12           | 5.4                                |
| 13           | 6.0                                |
| 14           | 6.3                                |
| 15           | 6.7                                |
| 16           | 7.0                                |
| 17           | 7.3                                |

libs: [https://repo.gradle.org/ui/native/libs-releases](https://repo.gradle.org/ui/native/libs-releases)

```
repositories {
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
}

dependencies {
    implementation("org.gradle:gradle-tooling-api:$toolingApiVersion")
    // The tooling API need an SLF4J implementation available at runtime, replace this with any other implementation
    runtimeOnly("org.slf4j:slf4j-simple:1.7.10")
}
```

similar: [https://github.com/ninetwozero/gradle-to-js](https://github.com/ninetwozero/gradle-to-js)

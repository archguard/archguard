# SCA

> SCA（Software Composition Analysis）软件成分分析，通俗的理解就是通过分析软件包含的一些信息和特征来实现对该软件的识别、管理、追踪的技术。我们知道在当今软件开发中，引入开源软件(注1)到你的项目中，避免重复造轮子是大家都再熟悉不过的了，比如开源库中开源软件按每年21%速度在增长(来源Forrester报告)，开源安全威胁成为企业组织无法回避的话题，而应用SCA技术对应用程序进行安全检测，实现安全管理是最行之有效的方法之一。

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

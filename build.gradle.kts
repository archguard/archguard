allprojects {
    group = "com.thoughtworks.archguard"

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://repo.maven.apache.org/maven2/")
        }
    }
}

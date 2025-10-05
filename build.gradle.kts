plugins {
    id("java")
    id("application")
}

application.mainClass = "io.github.tors_0.Main"

group = "io.github.tors_0"
version = "1.1.2"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io/")
        content {
            includeGroup("com.github.MinnDevelopment")
        }
    }
    maven{
        url = uri("https://maven.scijava.org/content/repositories/public/")
    }

}
dependencies {
    implementation("club.minnced:discord-rpc-release:3.3.0")
    implementation("com.github.MinnDevelopment:java-discord-rpc:v2.0.2")

    // https://mvnrepository.com/artifact/args4j/args4j
    implementation("args4j:args4j:2.37")
}


tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "io.github.tors_0.Main"
    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
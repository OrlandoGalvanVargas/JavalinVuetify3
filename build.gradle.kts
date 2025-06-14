
plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "mx.edu.uttt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    /* Vue 3, Vuetify 3 & Material Design */
    implementation("org.webjars.npm:vue:3.5.4")
    implementation("org.webjars.npm:vuetify:3.7.0")
    implementation("org.webjars.npm:mdi__font:7.4.47")
    // https://mvnrepository.com/artifact/org.webjars/font-awesome
    implementation("org.webjars:font-awesome:6.5.2")
    // https://mvnrepository.com/artifact/org.webjars.npm/sweetalert2
    implementation("org.webjars.npm:roboto-fontface:0.10.0")
    // https://mvnrepository.com/artifact/org.webjars.npm/vuetify
    implementation("org.webjars.npm:sweetalert2:11.11.1")
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("org.firebirdsql.jdbc:jaybird:5.0.5.java11")
    implementation("com.github.seratch:kotliquery:1.9.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

/* Build a bundle Jar file */
tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "mx.edu.uttt.MainKt")
    }

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(configurations.runtimeClasspath.get()
        .onEach { println("add from dependencies: ${it.name}") }
        .filter { it.name.endsWith("jar") }
        .map { if (it.isDirectory) it else zipTree(it) }) {
        exclude(
            "META-INF/INDEX.LIST",
            "META-INF/*.SF",
            "META-INF/*.DSA",
            "META-INF/*.RSA"
        )
    }

    sourceSets.main.get()
        .allSource.forEach { println("add from sources: ${it.canonicalPath}") }
}

application {
    mainClass.set("mx.edu.uttt.MainKt")
}

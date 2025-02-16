
plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "1.9.22"
    application
}



group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation(kotlin("stdlib"))
    //implementation("khttp:khttp:1.0.0")  // Libraría para HTTP requests
}
application {
    // Asegúrate de que o nome da clase principal sexa correcto
    mainClass.set("org.example.MainKt")  // Coma "com.exemplo.MainKt" ou a ruta correcta
}
tasks.test {
    useJUnitPlatform()
}


/*
kotlin {
    jvmToolchain(23)
}*/

import org.gradle.internal.declarativedsl.analysis.DefaultDataClass.Empty.properties

plugins {
    id("java")
    id("application")
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("maven-publish")
    `java-library`
}

group = "org.openstreetmap.josm"
version = "1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

tasks.register<Jar>("jarExp") {
    archiveBaseName.set("patchExport") // Set base name for the JAR file
    archiveVersion.set("1.0.0") // Set plugin version
    archiveClassifier.set("") // Empty classifier for the main version
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Created-By" to "22.0.2 ",
            "Plugin-Mainversion" to "19342",
            "Plugin-Version" to "0.0.1",
            "Plugin-Class" to "org.openstreetmap.josm.plugins.patchExport.PatchExportPlugin",
            "Plugin-Description" to "Export changes as OSC patches",
            "Plugin-Date" to "2024-06-07 17:12:11 +0900",
            "Author" to "Andrew Brazhnikov",
            "Plugin-Link" to "https://wiki.openstreetmap.org/wiki/JOSM/Plugins/patchExport",
            "Plugin-Requires" to "PicLayer",
            "Plugin-Canloadatruntime" to "true"
        )
    }
    from(sourceSets.main.get().output) // Include the compiled classes and resources
}

dependencies {
    implementation(files("libs/josm-tested.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
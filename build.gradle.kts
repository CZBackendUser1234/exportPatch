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
    archiveBaseName.set("patchExport")
    archiveVersion.set("1.1.0")
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Plugin-Mainversion" to "19342",
            "Plugin-Version" to "1.0.0",
            "Plugin-Class" to "org.openstreetmap.josm.plugins.patchExport.PatchExportPlugin",
            "Plugin-Description" to "Export changes as OSC patches",
            "Plugin-Date" to "2025-03-17 15:12:11 +0300",
            "Author" to "Andrew Brazhnikov",
            "Plugin-Link" to "https://wiki.openstreetmap.org/wiki/JOSM/Plugins/patchExport",
            "Plugin-Canloadatruntime" to "true"
        )
    }
    from(sourceSets.main.get().output)
}

dependencies {
    implementation(files("libs/josm-tested.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
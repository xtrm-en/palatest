import net.minecraftforge.gradle.user.patch.UserPatchExtension

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.minecraftforge.net/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    dependencies {
        classpath("com.anatawa12.forge", "ForgeGradle", "1.2-1.0.9")
    }
}

plugins {
    id("net.kyori.blossom") version "1.3.0"
    `java-library`
    `maven-publish`
    signing
}
apply(plugin = "forge")

group = properties["group"] as String
version = properties["version"] as String
val minecraftVersion = properties["minecraft_version"] as String
val forgeVersion = properties["forge_version"] as String

val tokens = mapOf(
    "modid" to properties["modid"],
    "name" to properties["name"],
    "desc" to properties["description"],
    "version" to project.version,
    "mcversion" to minecraftVersion,
)

configure<UserPatchExtension> {
    version = "$minecraftVersion-$forgeVersion"
    runDir = "run"
}

blossom {
    val constants = "src/main/java/me/xtrm/paladium/forgedevtest/Constants.java"

    tokens.mapKeys {
        "@${it.key.toUpperCase()}@"
    }.forEach {
        replaceToken(it.key, it.value, constants)
    }
}

java {
    withJavadocJar()
    withSourcesJar()

    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<ProcessResources> {
        tokens.forEach { inputs.property(it.key, it.value) }
        filesMatching("mcmod.info") {
            expand(tokens)
        }
    }
}

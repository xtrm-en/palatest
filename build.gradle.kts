import net.minecraftforge.gradle.user.patch.ForgeUserPlugin
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
    `java-library`
}
apply<ForgeUserPlugin>()

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
    mappings = properties["forge_mappings"] as String
    runDir = "run"
    accessTransformer("src/main/resources/${properties["modid"]}_at.cfg")

    replaceIn("src/main/java/" +
        group.toString().replace(".", "/") +
        "/" +
        properties["modid"] +
        "/Constants.java")
    replace(tokens.mapKeys { "@${it.key.toUpperCase()}@" })
}

configurations {
    val shade by creating
    implementation.get().extendsFrom(shade)
}

dependencies {
    val shade by configurations

    shade("mysql", "mysql-connector-java", "5.1.32")
    shade("org.sql2o", "sql2o", "1.6.0")

    "1.18.24".let { lombokVersion ->
        compileOnly("org.projectlombok", "lombok", lombokVersion)
        annotationProcessor("org.projectlombok", "lombok", lombokVersion)
    }
}

tasks {
    compileJava {
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    getByName<Jar>("jar") {
        manifest.attributes += mapOf(
            "FMLAT" to "${project.properties["modid"]}_at.cfg"
        )
        configurations.getByName("shade").forEach { dep ->
            from(project.zipTree(dep)){
                exclude(
                    "META-INF/INDEX.LIST",
                    "META-INF/*.SF",
                    "META-INF/*.DSA",
                    "META-INF/*.RSA",
                    "module-info.class",
                    "META-INF/versions/9/module-info.class"
                )
            }
        }
        archiveClassifier.set("all")
    }

    // Source artifact, including everything the 'main' does but not compiled.
    create("sourcesJar", Jar::class) {
        group = "build"

        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)

        this.manifest.from(jar.get().manifest)
    }

    // The Javadoc artifact, containing the javadoc output.
    create("javadocJar", Jar::class) {
        group = "build"

        val javadoc = getByName("javadoc")

        archiveClassifier.set("javadoc")
        dependsOn(javadoc)
        from(javadoc)
    }

    // The unobfuscated jar
    create("devJar", Jar::class) {
        group = "build"

        archiveClassifier.set("dev")
        from(sourceSets["main"].output)

        this.manifest.from(jar.get().manifest)
    }

    withType<ProcessResources> {
        tokens.forEach { inputs.property(it.key, it.value) }
        filesMatching("mcmod.info") {
            expand(tokens)
        }
        rename("(.+_at.cfg)", "META-INF/$1")
    }
}

artifacts {
    archives(tasks["sourcesJar"])
    archives(tasks["javadocJar"])
    archives(tasks["devJar"])
}

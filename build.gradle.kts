plugins {
    id("java")
    id("java-library")
    id("com.diffplug.spotless") version ("6.11.0")
    id("com.github.johnrengelman.shadow") version ("7.1.1")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()

        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://maven.elmakers.com/repository/")
        maven("https://repo.kryptonmc.org/releases")

        maven("https://jitpack.io")
    }

    dependencies {
        api("com.github.SaiintBrisson.command-framework:bukkit:1.3.1")
        compileOnlyApi("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
        compileOnlyApi("com.github.retrooper.packetevents:spigot:2.0.2")
        compileOnlyApi("org.jetbrains:annotations:23.0.0")

        implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
    }

    tasks.shadowJar {
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_9
        targetCompatibility = JavaVersion.VERSION_1_9
    }

    spotless {
        java {
            removeUnusedImports()
            palantirJavaFormat()
            formatAnnotations()
            importOrder()
        }
    }
}
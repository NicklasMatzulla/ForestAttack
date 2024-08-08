/*
 * MIT License
 *
 * Copyright (c) 2024 Nicklas Matzulla
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("SpellCheckingInspection")

import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
import net.minecrell.pluginyml.paper.PaperPluginDescription


plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginYml)
    alias(libs.plugins.paperweight)
    id("java")
}

group = "de.nicklasmatzulla"
version = "1.1.1"
description = "The main system for ForestAttack, a game mode based on CraftAttack."
val website = "https://github.com/NicklasMatzulla/ForestAttack/"
val authors = listOf("Nicklas Matzulla")
val apiVersion = "1.21"
val foliaSupported = false

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi") // PlaceholderAPI
    maven("https://jitpack.io")
}

dependencies {
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    implementation(libs.annotations)
    paperweight.paperDevBundle(libs.versions.paperApi)
    implementation(libs.triumphGui)
    compileOnly(libs.placeholderApi)
    compileOnly(libs.luckperms)
    compileOnly(libs.premiumVanish)
    implementation(libs.hikaricp)
    implementation(libs.maraidbJdbc)

    testAnnotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testImplementation(libs.annotations)
    testImplementation(libs.triumphGui)
    testCompileOnly(libs.placeholderApi)
    testCompileOnly(libs.luckperms)
    testCompileOnly(libs.premiumVanish)
    testImplementation(libs.hikaricp)
    testImplementation(libs.maraidbJdbc)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

paperweight.reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    shadowJar {
        fun reloc(pkg: String, libName: String) = relocate(pkg, "de.nicklasmatzulla." + project.name.lowercase() + ".$libName")
    }
}

paper {
    name = project.name
    version = project.version.toString()
    description = project.description
    website = this@Build_gradle.website
    authors = this@Build_gradle.authors
    main = project.group.toString() + "." + project.name.lowercase() + "." + project.name
    generateLibrariesJson = true
    foliaSupported = this@Build_gradle.foliaSupported
    apiVersion = this@Build_gradle.apiVersion
    serverDependencies {
        register("PlaceholderAPI") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("LuckPerms") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("PremiumVanish") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}
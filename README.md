# PaperTemplate
PaperTemplate is a quick starter that makes it easy to create new programming projects using my libraries.

## Features
* Local programming server
* Automatic paper-plugin.yml creation
* Shadow configuration and dependency relocation

## Supported plugins and libraries
- **[Run-Task](https://github.com/jpenilla/run-task)**: run-task is a gradle plugin that allows you to run specific tasks, such as a Minecraft server in order to test plugins.
- **[Shadow](https://github.com/johnrengelman/shadow)**: Shadow is a plugin that facilitates the creation of uber JARs, combining all project dependencies and resources into a single, executable JAR file, simplifying deployment and distribution of Java applications.
- **[Lombok](https://github.com/projectlombok/lombok):** Lombok is a Java library that helps reduce boilerplate code by automatically generating common code structures (such as getters, setters, constructors) during compile time via annotations.
- **[Jetbrains Annotations](https://github.com/JetBrains/java-annotations)**: JetBrains Annotations provide a set of annotations that help improve code analysis, documentation, and IDE assistance by conveying additional information about nullability, threading, and more to the IntelliJ IDEA IDE.
- **[Paperweight](https://github.com/PaperMC/paperweight)**: Paperweight is a Gradle plugin that simplifies decompiling and deobfucating to use NMS code. The plugin provides support to deobfuscate the NMS code when compiling the plugin.

## Usage
1. Create a project using this template
2. Adjust the project name in the [settings.gradle.kts](./settings.gradle.kts) file.
3. Customize the `group id`, `description`, `website`, `list of authors`, `API version` and `Folia support` in the [build.gradle.kts](./build.gradle.kts)` file.
4. Rename your main class and its package to the following format: `group_id`.`lowercase_project_name`.`project_name`. The project name may only contain upper and lower case letters and numbers.
5. Adjust your [README.md](./README.md) file.
package org.defendev.jooq.demo.codegen

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction



abstract class GreetingTask extends DefaultTask {

    @Input
    abstract Property<String> getMessage()

    @OutputFile
    abstract RegularFileProperty getGreetOutFile()

    @TaskAction
    void run() {
        final File file = getGreetOutFile().get().asFile
        file.parentFile.mkdirs()
        file.write(getMessage().get())
        logger.lifecycle("Wrote greeting to ${file}")
    }

}

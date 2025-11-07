package org.defendev.jooq.demo.codegen

import org.gradle.api.Plugin
import org.gradle.api.Project



/*
 *
 * Primary reason why this Plugin exists is to demonstrate that multiple plugins can be developed
 * in the same repository and distributed through single publication.
 *
 *
 */
class GreetingPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        final GreetingExtension extension = project.extensions.create('greetingDsl', GreetingExtension)
        extension.message.convention("Hello from plugin")
        extension.greetOutFile.convention(project.layout.buildDirectory.file("greeting.txt"))

        project.tasks.register('multipocGreet', GreetingTask) {
            group = '020-jooq-codegen'
            description = "Writes a greeting to a file"
            message.set(extension.message)
            greetOutFile.set(extension.greetOutFile)
        }
    }

}

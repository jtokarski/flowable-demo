package org.defendev.jooq.demo.codegen

import org.gradle.api.Plugin
import org.gradle.api.Project



class MultipocJooqCodegenPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        final MultipocJooqCodegenExtension extension = project.extensions.create(
            'multipocJooqCodegenDsl', MultipocJooqCodegenExtension)

        extension.abcd.convention('This is A.B.C.D')

        project.tasks.register('multipocJooq', MultipocJooqCodegenTask) {
            /*
             * The group here is to be used as header in 'tasks' task output. It's not related with
             * Maven coordinates.
             *
             */
            group = '020-jooq-codegen'
            description = "Generates Jooq Classes"
            abcd.set(extension.abcd)
        }
    }

}

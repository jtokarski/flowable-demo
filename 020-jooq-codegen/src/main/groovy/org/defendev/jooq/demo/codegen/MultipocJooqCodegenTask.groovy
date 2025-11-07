package org.defendev.jooq.demo.codegen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.jooq.DSLContext



abstract class MultipocJooqCodegenTask extends DefaultTask {

    @Input
    abstract Property<String> getAbcd()

    @TaskAction
    void run() {
        logger.lifecycle("Wrote greeting to ${getAbcd()}")
        logger.lifecycle("::: DSLContext - ${DSLContext.getLocation()} :::")
        logger.lifecycle("Wrote greeting to ${getAbcd()}")
        logger.lifecycle("Wrote greeting to ${getAbcd()}")
    }

}

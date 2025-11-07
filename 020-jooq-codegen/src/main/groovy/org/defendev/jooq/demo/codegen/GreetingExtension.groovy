package org.defendev.jooq.demo.codegen

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property



abstract class GreetingExtension {

    abstract Property<String> getMessage()

    abstract RegularFileProperty getGreetOutFile()

}

package org.defendev.jooq.demo.codegen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.jooq.DSLContext
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Jdbc
import org.jooq.meta.jaxb.SchemaMappingType
import org.jooq.meta.jaxb.Target



/*
 * Potentially, consider adjusting identifier names generation (to camel-case table names):
 * https://www.jooq.org/doc/3.20/manual/code-generation/codegen-object-naming/codegen-generatorstrategy/
 */
abstract class MultipocJooqCodegenTask extends DefaultTask {

    @Input
    abstract Property<String> getAbcd()

    @TaskAction
    void run() {
        logger.lifecycle("Wrote greeting to ${getAbcd()}")
        logger.lifecycle("::: DSLContext - ${DSLContext.getLocation()} :::") // Yey! I have DSLContext in
                                                                             //  my plugin's classpath!
        logger.lifecycle("Wrote greeting to ${getAbcd()}")
        logger.lifecycle("Wrote greeting to ${getAbcd()}")

        final Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver("org.h2.Driver")
                .withUrl("jdbc:h2:tcp://localhost:9092/mem:multipoc")
                .withUser("sa")
                .withPassword("sa")
            )
            .withGenerator(new Generator()
                .withDatabase(new Database()
                    .withName("org.jooq.meta.h2.H2Database")
                    .withIncludes(".*")
                    /*
                     * On generating classess for multiple schemas/catalogs at once:
                     * https://www.jooq.org/doc/3.20/manual/code-generation/input-schema/
                     */
                    .withSchemata(
                        new SchemaMappingType().withInputSchema("amp_core")
                    )
                )
                .withTarget(new Target()
                    .withPackageName("org.defendev.flowable.demo.multipoc.jooq")
                    .withDirectory("C:\\dev\\flowable-demo\\019-accounting-multipoc\\src\\main\\java")
                )
            );

        GenerationTool.generate(configuration)
    }

}

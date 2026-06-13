package org.defendev.restassured.demo.spring;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.restassured.demo.EphemeralProfileManager;
import org.defendev.restassured.demo.ProfileFlags;
import org.defendev.restassured.demo.RestAssuredContextLoader;
import org.defendev.restassured.demo.RestAssuredProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;



/*
 * Intended to demonstrate all features Spring test setup dedicated for convenient usage of scratchpad-like projects:
 *   - support of Spring profiles - application-*.yaml files (intended as primary way of handling profiles)
 *   - support of Spring profiles - ProfileFlags utility bean (intended only for cases where profile files are
 *     insufficient)
 *   - support of Spring profiles - local switching by editing @ActiveProfiles
 *
 * Spring profile files are organized as follows:
 *   - environment profile files (application-local.yaml, application-qa.yaml, application-prod.yaml) should
 *     contain non-changing properties, not to be edited locally
 *   - application-test.yaml, application-ephemeral.yaml - local changes go here
 *
 * Separate aspect of using Spring features is using UriComponents as main URL templating engine.
 *
 */
@ActiveProfiles({RestAssuredProfile.local})
@ContextConfiguration(
    classes = {SpringSetupConfig.class},
    loader = RestAssuredContextLoader.class
)
@ExtendWith(SpringExtension.class)
public class SpringSetupTest {

    private static final Logger log = LogManager.getLogger();

    private DemoMemory mem;

    @BeforeEach
    public void setUp(@Autowired ProfileFlags profileFlags) {
        mem = new DemoMemory(profileFlags);
    }

    @AfterEach
    public void tearDown() {
        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
            mem.print();
        }));
    }

    @Test
    public void shouldAccessPropertiesFromProfileYamFiles(
        @Value("${resta014.label}") String environmentLabel
    ) {
        log.info("Environment label: " + environmentLabel);
    }

    @Test
    public void shouldAccessProfileFlags(@Autowired ProfileFlags profileFlags) {
        log.info("local profile active: " + profileFlags.local());
        log.info("   qa profile active: " + profileFlags.qa());
        log.info(" prod profile active: " + profileFlags.prod());
    }

    @Test
    public void shouldRenderApplicationEphemeralYamlFile() {
        final Map<String, Map<String, String>> newProperties = ofEntries(
            entry(
                "vend00r",
                ofEntries(entry("access-token", RandomStringUtils.secure().nextAlphanumeric(32)))
            ),
            entry(
                "partn3r",
                ofEntries(entry("session-id", RandomStringUtils.secure().nextAlphanumeric(24).toUpperCase()))
            )
        );
        EphemeralProfileManager.save(newProperties);
    }

    @Test
    public void shouldMemorizeGeneratedValue() {
        mem.generatedId = UUID.randomUUID().toString();
        throw new RuntimeException("Exception for " + mem.generatedId);
    }

}

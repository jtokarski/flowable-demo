package org.defendev.restassured.demo;

import org.apache.commons.lang3.Validate;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;



/*
 * Only supports the YAML with 2 levels:
 *   - level 1: only map (organizing) properties
 *   - level 2: only string properties (all values are here)
 * For my purposes I'll never need more than that.
 *
 */
public class EphemeralProfileManager {

    private static final String PROFILE_FILE_BASE_NAME = "application-ephemeral";

    private static final String RELATED_FILE_NAME = PROFILE_FILE_BASE_NAME + ".dist.yaml";

    private static final String PROFILE_FILE_NAME = PROFILE_FILE_BASE_NAME + ".yaml";

    private static final String PROFILE_FILE_RELATIVE_PATH = "src/test/resources/" + PROFILE_FILE_NAME;

    private static final String MODULE_DIRECTORY_NAME = "014-rest-assured";

    public static void save(Map<String, Map<String, String>> newProperties) {
        Validate.isTrue(nonNull(newProperties), "newProperties must not be null");

        final LoaderOptions loaderOptions = new LoaderOptions();
        final Yaml yaml = new Yaml(loaderOptions);
        final Path targetPath = establishTargetPath();
        try {
            final Map<String, Map<String, String>> loadedProperties = yaml.load(Files.newInputStream(targetPath));
            final Map<String, Map<String, String>> outputProperties = new HashMap<>();
            if (nonNull(loadedProperties)) {
                loadedProperties.forEach((String key, Map<String, String> loadedPropertiesNested) -> {
                    final Map<String, String> outputPropertiesNested = new HashMap<>(loadedPropertiesNested);
                    outputProperties.put(key, outputPropertiesNested);
                });
            }
            newProperties.forEach((key, newPropertiesNested) -> {
                if (outputProperties.containsKey(key)) {
                    outputProperties.get(key).putAll(newPropertiesNested);
                } else {
                    final Map<String, String> outValue = outputProperties.computeIfAbsent(key, k -> new HashMap<>());
                    outValue.putAll(newPropertiesNested);
                }
            });
            justWrite(outputProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path establishTargetPath() {
        final ClassPathResource relatedResource = new ClassPathResource(RELATED_FILE_NAME);
        try {
            if (relatedResource.isFile()) {
                final Path relatedResourcePath = relatedResource.getFile().toPath();
                Path projectPath = null;
                Path traverse = relatedResourcePath;
                for (int i = 0; i < relatedResourcePath.getNameCount(); i++) {
                    traverse = traverse.getParent();
                    if (MODULE_DIRECTORY_NAME.equals(traverse.getFileName().toString())) {
                        projectPath = traverse;
                        break;
                    }
                }
                if (isNull(projectPath)) {
                    throw new RuntimeException(format("Module directory [%s] not found in related resource path [%s]",
                        PROFILE_FILE_NAME, relatedResourcePath));
                }
                final Path profileFilePath = projectPath.resolve(PROFILE_FILE_RELATIVE_PATH);
                final Path profileFileParentDirPath = profileFilePath.getParent();
                if (!Files.exists(profileFileParentDirPath)) {
                    throw new RuntimeException("Profile file parent directory does not exist: " +
                        profileFileParentDirPath);
                }
                if (!Files.exists(profileFilePath)) {
                    Files.createFile(profileFilePath);
                }
                return profileFilePath;
            } else {
                throw new RuntimeException(format("Unable to establish target path - not a file: %s",
                    RELATED_FILE_NAME));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void justWrite(Map<String, Map<String, String>> content) {
        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setIndent(2);
        dumperOptions.setPrettyFlow(true);
        final Path targetPath = establishTargetPath();

        final BufferedWriter targetFileWriter;
        try {
            targetFileWriter = Files.newBufferedWriter(targetPath, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final Yaml yaml = new Yaml(dumperOptions);
        yaml.dump(content, targetFileWriter);
    }

}

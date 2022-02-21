package com.thoughtworks.archguard.scanner.common.domain.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModuleUtil {
    private static Pattern pomPattern = Pattern.compile(".*<artifactId>.*</artifactId>");
    private static Pattern gradlePattern = Pattern.compile(".*dependency.*\".*:.*:.*");

    private ModuleUtil() {
    }

    public static Module getModule(Path path) throws NoModuleException, IOException {
        File file = path.toFile();
        File parentFile = file.getParentFile();
        int i = path.getNameCount();
        while (!Arrays.asList(Objects.requireNonNull(getParentFileList(parentFile))).contains("src") || i < 1) {
            parentFile = parentFile.getParentFile();
            i--;
        }
        String name = parentFile.getName();
        String modulePath = parentFile.getPath();
        return new Module(name, modulePath, getModuleDependencies(parentFile));
    }

    private static Set<String> getModuleDependencies(File parentFile) throws IOException {
        Path pomPath = Paths.get(parentFile.getPath(), "pom.xml");
        if (pomPath.toFile().isFile()) {
            return Files.lines(pomPath).map(pomPattern::matcher)
                    .filter(Matcher::matches)
                    .map(m -> m.group(0)).map(c -> c.split("<artifactId>")[1]
                            .split("</artifactId>")[0])
                    .collect(Collectors.toSet());
        }
        Path gradlePath = Paths.get(parentFile.getPath(), "build.gradle");
        if (gradlePath.toFile().isFile()) {
            return Files.lines(gradlePath).map(gradlePattern::matcher)
                    .filter(Matcher::matches)
                    .map(m -> m.group(0)).map(c -> c.split(":")[1]).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private static String[] getParentFileList(File parentFile) throws NoModuleException {
        if (parentFile == null) {
            throw new NoModuleException();
        }
        String[] list = parentFile.list();
        if (list == null) {
            throw new NoModuleException();
        }
        return list;
    }
}

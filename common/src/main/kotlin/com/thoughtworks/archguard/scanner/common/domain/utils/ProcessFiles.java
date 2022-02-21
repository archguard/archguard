package com.thoughtworks.archguard.scanner.common.domain.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static java.nio.file.FileVisitResult.CONTINUE;

public class ProcessFiles extends SimpleFileVisitor<Path> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Consumer<Path> process;
    private final ExecutorService pool;

    public ProcessFiles(Consumer<Path> process, ExecutorService pool) {
        this.pool = pool;
        this.process = process;
    }

    public ProcessFiles(Consumer<Path> process) {
        this.pool = null;
        this.process = process;
    }

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) {

        if (attr.isRegularFile()) {
            if (pool != null) {
                pool.submit(() -> process.accept(file));
            } else {
                process.accept(file);
            }
        }
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                              IOException exc) {
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        logger.error(exc.toString());
        return CONTINUE;
    }


}

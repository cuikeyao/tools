package com.cuikeyao.gitbot.util;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GitUtilsTest {

    @BeforeEach
    void setUp() {
        GitUtils.projectPath = Path.of(System.getProperty("user.dir"));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void cloneRepository() throws GitAPIException, IOException, InterruptedException {
        GitUtils.cloneRepository();
    }

    @Test
    @Order(2)
    void commitChanges() throws GitAPIException, IOException {
        GitUtils.commitChanges();
    }

    @Test
    @Order(3)
    void pushChanges() throws IOException, InterruptedException {
        GitUtils.pushChanges();
    }

    @Test
    void deleteProject() throws IOException {
//        GitUtils.deleteProject();
    }
}
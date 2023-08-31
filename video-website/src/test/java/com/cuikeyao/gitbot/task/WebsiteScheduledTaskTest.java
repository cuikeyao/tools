package com.cuikeyao.gitbot.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebsiteScheduledTaskTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUrl() {
        WebsiteScheduledTask websiteScheduledTask = new WebsiteScheduledTask();
        websiteScheduledTask.refresh();
    }
}
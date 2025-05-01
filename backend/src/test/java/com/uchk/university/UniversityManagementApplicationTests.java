package com.uchk.university;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // This ensures the application-test.properties is used
public class UniversityManagementApplicationTests {

    @Test
    void contextLoads() {
        // This will now use the test profile properties
    }
}
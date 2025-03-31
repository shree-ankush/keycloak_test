package com.setup.demo;

import com.setup.demo.UserDTO;
import com.setup.demo.CsvReaderService;
import com.setup.demo.KeycloakUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class CsvUserImporter {

    public static void main(String[] args) {
        SpringApplication.run(CsvUserImporter.class, args);
    }

    @Bean
    CommandLineRunner runCsvImport(CsvReaderService csvReaderService, KeycloakUserService userService) {
        return args -> {
            String filePath = "src/main/resources/users.csv"; // ✅ Ensure this file exists

            try {
                // ✅ Read CSV file
                List<UserDTO> users = csvReaderService.readCsv(filePath);
                System.out.println("📄 CSV File Read Successfully. Processing Users...");

                for (UserDTO user : users) {
                    String userId = userService.createUser(user);
                    if (userId != null) {
//                        userService.sendVerificationEmail(userId);
                        System.out.println("✅ User Created: " + user.getEmail());
                    } else {
                        System.out.println("❌ Failed to Create User: " + user.getEmail());
                    }
                }
                System.out.println("🎉 CSV User Import Completed!");

            } catch (IOException e) {
                System.err.println("❌ Error Reading CSV File: " + e.getMessage());
            }
        };
    }
}


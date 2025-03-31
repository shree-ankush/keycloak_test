package com.setup.demo;

import com.setup.demo.UserDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakUserService {

    private final Keycloak keycloak;
    private final String realm = "myrealm"; // ✅ Change this to your Keycloak realm name

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
        Keycloak keycloak2 = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("myrealm")
                .grantType("password")
                .clientId("admin-cli")  // Make sure client has admin permissions
                .username("admin321")
                .password("admin321")
                .build();

        String token = keycloak2.tokenManager().getAccessToken().getToken();
        System.out.println("Access Token: " + token);
    }




    public String createUser(UserDTO userDTO) {
        try {
            // ✅ Create UserRepresentation
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEnabled(true);
            user.setEmailVerified(false);

            System.out.println(" " + user.getEmail());

            // ✅ Get Realm Resource
            RealmResource realmResource = keycloak.realm(realm);
            Response response = realmResource.users().create(user);

            // ✅ Check if user was created successfully
            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                System.out.println("✅ User Created: " + userDTO.getEmail());

                // ✅ Set Password
                setPassword(userId, userDTO.getPassword());
                return userId;
            } else {
                // ✅ Print exact error from Keycloak
                String errorMessage = response.readEntity(String.class);
                System.err.println("❌ User creation failed: " + response.getStatus() + " - " + errorMessage);
                throw new RuntimeException("User creation failed: " + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace(); // ✅ Print the full error stack
            throw new RuntimeException("Exception while creating user", e);
        }
    }

    private void setPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false); // Set to true if you want users to reset passwords on first login

        keycloak.realm(realm).users().get(userId).resetPassword(credential);
        System.out.println("🔑 Password Set for User: " + userId);
    }
//
//    public void sendVerificationEmail(String userId) {
//        keycloak.realm(realm).users().get(userId).sendVerifyEmail();
//        System.out.println("📧 Verification Email Sent to User ID: " + userId);
//    }
}

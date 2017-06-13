/*
 * Copyright 2015-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.authentication;

import java.io.IOException;
import uk.theretiredprogrammer.nbpcglibrary.authentication.dataobjects.User;
import uk.theretiredprogrammer.nbpcglibrary.authentication.dataobjects.Role;
import uk.theretiredprogrammer.nbpcglibrary.authentication.dataobjects.Application;
import uk.theretiredprogrammer.nbpcglibrary.authentication.dataobjects.Userrole;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import uk.theretiredprogrammer.nbpcglibrary.authentication.dataobjects.ApplicationRoot;
import uk.theretiredprogrammer.nbpcglibrary.authentication.dataobjects.UserRoot;
import uk.theretiredprogrammer.nbpcglibrary.data.entityreferences.EntityReference;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 * Authentication - User Authentication, Role Authentication, Permission Testing
 * and Password encoding
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class Authentication {

    private static String roleName;
    private static EntityReference<Integer, User, UserRoot> user;
    private static EntityReference<Integer, Application, ApplicationRoot> application;

    /**
     * Authenticate User and get Role.
     *
     * @param applicationname the application name (for role extraction)
     * @param username the user name
     * @param password the password
     * @return true if user name / password authenticates and user has a role
     * for this application
     */
    public static boolean authenticate(String applicationname, String username, String password) {
        return authenticate(applicationname, username) ? user.get().getEncodedpassword().equals(Authentication.encodePassword(password)) : false;
    }

    /**
     * Authenticate User and get Role.
     *
     * @param applicationname the application name (for role extraction)
     * @param username the user name
     * @return true if user name is defined and user has a role for this
     * application
     */
    public static boolean authenticate(String applicationname, String username) {
            Application.EM emApplication = Application.getEM();
            User.EM emUser = User.getEM();
            try {
                application = new EntityReference<>("application",emApplication.getEntityPersistenceProvider().findOne("application", applicationname), emApplication);
                user = new EntityReference<>("user", emUser.getEntityPersistenceProvider().findOne("username", username), emUser);
            } catch (Exception ex){
                return false; 
            }
            User e = user.get();
            return e.getEnabled() && findRole(e);
    }

    private static boolean findRole(User user) {
        for (Userrole ur : user.getUserroles()) {
            Role r = ur.getRole();
            if (r.getParent().getPK().equals(application.get().getPK())) {
                roleName = r.getRole();
                return true;
            }
        }
        return false; // no role for this user for this app
    }

    /**
     * Get User.
     *
     * @return the User for the currently authenticated user
     */
    public static User getUser() {
        return user.get();
    }

    /**
     * Get the current User Role
     *
     * @return the Role for the currently authenticated user
     */
    public static String getRole() {
        return roleName;
    }

    /**
     * Test if the currently authenticated user has a permission
     *
     * @param permissionName the permission to be tested
     * @return true if the currently authenticated user has the permission
     * @throws IOException if problems getting entity
     */
    public static boolean hasPermission(String permissionName) throws IOException {
        return (user.get().getUserpermissions().stream().map((up) -> up.getPermission()).anyMatch((p) ->
                (p.getParent().getPK().equals(application.getPK()) && p.getPermission().equals(permissionName))));
    }

    /**
     * Encode a password from plain text to encoded format
     *
     * @param password the plain text password
     * @return the encoded password
     */
    public static String encodePassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new LogicException("Encode(): failure getting Message Digest", ex);
        }
        md.update(("ELhbS9rcKO" + password.trim() + "pz7WGfrVBP").getBytes());
        byte digest[] = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Check if password meets required password strength of Weak.
     *
     * @param password the plain text password
     * @return true if password passes test
     */
    public static boolean weakPasswordCheck(String password) {
        return password.length() != 0;
    }

    /**
     * Check if password meets required password strength of Weak.
     *
     * @param password the plain text password
     * @return true if password passes test
     */
    public static boolean strongPasswordCheck(String password) {
        if (password.length() < 8) {
            return false;
        }
        charTypeCounter(password);
        return ucCount > 0 && lcCount > 0 && digitCount > 0 && otherCount > 0;
    }

    /**
     * Check if password meets required password strength of Weak.
     *
     * @param password the plain text password
     * @return true if password passes test
     */
    public static boolean veryStrongPasswordCheck(String password) {
        if (password.length() < 12) {
            return false;
        }
        charTypeCounter(password);
        return ucCount > 1 && lcCount > 1 && digitCount > 1 && otherCount > 1;
    }

    private static int ucCount;
    private static int lcCount;
    private static int digitCount;
    private static int otherCount;

    private static void charTypeCounter(String password) {
        ucCount = 0;
        lcCount = 0;
        digitCount = 0;
        otherCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                ucCount++;
            } else if (Character.isLowerCase(c)) {
                lcCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else {
                otherCount++;
            }
        }
    }
}

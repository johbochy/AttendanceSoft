package com.propscout.utils;

import com.propscout.data.models.User;

import java.io.*;

public class AuthHelper {

    private static final String AUTH_FILE = "login.ser";

    /**
     * Check whether the /auth/login.ser file exists in which case a user has logged in
     *
     * @return boolean of the the file exists if it exist return true else false
     */
    public static boolean isLoggedIn() {

        return new File(AUTH_FILE).exists();

    }

    /**
     * Serialize the object to a file and pretend to have logged
     *
     * @param user the currently authenticated user
     */
    public static void login(User user) {

        try (
                FileOutputStream fileOutputStream = new FileOutputStream(AUTH_FILE);
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        ) {

            outputStream.writeObject(user);
            System.out.println("You are currently logged in as " + user.getName() + " ...");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete the login file if it exists
     */
    public static void logout() {
        if (isLoggedIn()) {
            if (new File(AUTH_FILE).delete()) {
                System.out.println("You have been successfully logged out");
            }
        }
    }

    /**
     * Return the authenticated user from the auth file, else, null
     *
     * @return an instance of User read from file, if not null
     */
    public static User getLoggedInUser() {

        User user = null;

        try (
                FileInputStream fileInputStream = new FileInputStream(AUTH_FILE);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)
        ) {

            user = (User) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user;
    }

}

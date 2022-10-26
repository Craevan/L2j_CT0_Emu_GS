package com.l2j.emu.accountmanager;

import com.l2j.emu.Config;
import com.l2j.emu.commons.crypt.BCrypt;
import com.l2j.emu.commons.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SQLAccountManager {
    private static final String INSERT_OR_UPDATE_ACCOUNT = "INSERT INTO accounts(login, password, access_level) " +
            "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE password = VALUES(password), access_level = VALUES(access_level)";
    private static final String UPDATE_ACCOUNT_LEVEL = "UPDATE accounts SET access_level = ? WHERE login = ?";
    private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE login = ?";

    private static String userName = "";
    private static String password = "";
    private static String level = "";
    private static String mode = "";

    public static void main(String[] args) {
        Config.loadAccountManager();
        ConnectionPool.init();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Please choose an option:");
                System.out.println();
                System.out.println("1 - Create new account or update existing one (change pass and access level)");
                System.out.println("2 - Change access level");
                System.out.println("3 - Delete existing account");
                System.out.println("4 - List accounts and access levels");
                System.out.println("5 - Exit");

                while (!(mode.equals("1") ||
                        mode.equals("2") ||
                        mode.equals("3") ||
                        mode.equals("4") ||
                        mode.equals("5"))) {
                    System.out.println("Your choice:");
                    mode = scanner.next();
                }
                if (mode.equals("1") || mode.equals("2") || mode.equals("3")) {
                    while (userName.trim().length() == 0) {
                        System.out.print("Username: ");
                        userName = scanner.next().toLowerCase();
                    }
                    if (mode.equals("1")) {
                        while (password.trim().length() == 0) {
                            System.out.println("Password: ");
                            password = scanner.next();
                        }
                    }
                    if (mode.equals("1") || mode.equals("2")) {
                        while (level.trim().length() == 0) {
                            System.out.print("Access level: ");
                            level = scanner.next();
                        }
                    }
                }
                switch (mode) {
                    case "1" -> addOrUpdateAccount(userName.trim(), password.trim(), level.trim());
                    case "2" -> changeAccountLevel(userName.trim(), level.trim());
                    case "3" -> {
                        System.out.print("WARNING: This will not delete the game server data (characters, items, etc..)");
                        System.out.print(" it will only delete the account login server data.");
                        System.out.println();
                        System.out.print("Do you really want to delete this account? Y/N: ");
                        String answer = scanner.next();
                        if (answer != null && answer.equalsIgnoreCase("y")) {
                            deleteAccount(userName.trim());
                        } else {
                            System.out.println("Deletion cancelled");
                        }
                    }
                    case "4" -> {
                        mode = "";
                        System.out.println();
                        System.out.println("Please choose a listing mode:");
                        System.out.println();
                        System.out.println("1 - Banned accounts only (accessLevel < 0)");
                        System.out.println("2 - GM/privileged accounts (accessLevel > 0");
                        System.out.println("3 - Regular accounts only (accessLevel = 0)");
                        System.out.println("4 - List all");
                        while (!(mode.equals("1") || mode.equals("2") || mode.equals("3") || mode.equals("4"))) {
                            System.out.print("Your choice: ");
                            mode = scanner.next();
                        }
                        System.out.println();
                        printAccountInfo(mode);
                    }
                    case "5" -> System.exit(0);
                }
                userName = "";
                password = "";
                level = "";
                mode = "";
                System.out.println();
            }
        }
    }

    private static void printAccountInfo(String level) {
        int count = 0;
        String query = "SELECT login, access_level FROM accounts";
        if ("1".equals(level)) {
            query = query.concat("WHERE access_level < 0");
        } else if ("2".equals(level)) {
            query = query.concat("WHERE access_level > 0");
        } else if ("3".equals(level)) {
            query = query.concat("WHERE access_level = 0");
        }
        query = query.concat("ORDER BY login ASC");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("login")
                        + " -> " + resultSet.getInt("access_level"));
                count++;
            }
            System.out.println("Displayed accounts: " + count);
        } catch (SQLException sqlException) {
            System.err.println("There was error while displaying accounts:");
            System.err.println(sqlException.getMessage());
        }
    }

    private static void addOrUpdateAccount(String account, String password, String level) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_OR_UPDATE_ACCOUNT)) {
            final String hashed = BCrypt.hashPw(password);
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, hashed);
            preparedStatement.setString(3, level);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Account " + account + " has been created or updated");
            } else {
                System.out.println("Account " + account + " doesn't exist");
            }
        } catch (SQLException sqlException) {
            System.err.println("There was error while adding/updating account:");
            System.err.println(sqlException.getMessage());
        }
    }

    private static void changeAccountLevel(String account, String level) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_LEVEL)) {
            preparedStatement.setString(1, level);
            preparedStatement.setString(2, account);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Account: " + account + " updated successfully");
            } else {
                System.out.println("Account: " + account + " doesn't exist");
            }
        } catch (SQLException sqlException) {
            System.err.println("There was error while updating account:");
            System.err.println(sqlException.getMessage());
        }
    }

    private static void deleteAccount(String account) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT)) {
            preparedStatement.setString(1, account);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Account: " + account + " deleted successfully");
            } else {
                System.out.println("Account: " + account + " doesn't exist");
            }

        } catch (SQLException sqlException) {
            System.err.println("There was error while deleting account:");
            System.err.println(sqlException.getMessage());
        }
    }
}

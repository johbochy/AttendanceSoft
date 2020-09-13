package com.propscout.data.adapters;

import com.propscout.data.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UsersAdapter extends AbstractDatabaseAdapter {

    /**
     * Persist a user to the database
     *
     * @param user with details to be persisted
     * @return boolean of whether user was inserted to the database, true if inserted otherwise false
     */
    public boolean add(User user) {

        open();
        String sql = "INSERT INTO users(name, username, email, mobile, role, avatar, fingerprint, password)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, null, ?);";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getMobile());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setString(6, user.getAvatar());
            preparedStatement.setString(7, user.getPassword());

            int result = preparedStatement.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get all users from thr database
     *
     * @return a ResultSet with all database users
     */
    public ResultSet getAllUsers() {
        open();

        String sql = "SELECT * FROM users;";

        try {

            Statement statement = conn.createStatement();

            return statement.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a user from the database that matches the passed string
     *
     * @param login either email or username
     * @return the User gotten from the database
     */
    public User getByEmailOrUsername(String login) {

        open();
        String sql = "SELECT * FROM users\n" +
                "WHERE email = ? OR username = ? LIMIT 1;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, login);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");
                String role = rs.getString("role");
                String avatar = rs.getString("avatar");
                String password = rs.getString("password");
                int login_count = rs.getInt("login_count");
                String created_at = rs.getString("created_at");
                String updated_at = rs.getString("updated_at");

                User user = new User(id, name, username, email, mobile, role, avatar, null, password, login_count, created_at, updated_at);

                close();

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Gets all users of a specified role from the database
     *
     * @param role the specific role that one wants
     * @return ResultSet of the users
     */
    public ResultSet getUserByRole(String role) {

        open();
        String sql = "SELECT * FROM users WHERE role = ?;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, role);

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateUser(User user) {

        open();

        String sql = "UPDATE users\n" +
                "SET name = ?, username = ?, email = ?, mobile = ?, role = ?, avatar = ?, password = ?\n" +
                "WHERE id = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getMobile());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setString(6, user.getAvatar());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setInt(8, user.getId());


            int result = preparedStatement.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getAllUsersCount() {

        int usersCount = 0;
        open();
        String sql = "SELECT COUNT(*) AS users_count FROM users;";

        try (Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery(sql);


            if (rs.next()) {
                usersCount = rs.getInt("users_count");
            }

            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersCount;
    }

    public Optional<User> findUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        open();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");
                String role = rs.getString("role");
                String avatar = rs.getString("avatar");
                String password = rs.getString("password");
                int login_count = rs.getInt("login_count");
                String created_at = rs.getString("created_at");
                String updated_at = rs.getString("updated_at");

                User user = new User(id, name, username, email, mobile, role, avatar, null, password, login_count, created_at, updated_at);

                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return Optional.empty();
    }

    public boolean deleteUserById(int id) {

        Optional<User> optionalUser = findUserById(id);

        if (optionalUser.isPresent()) {
            open();

            String sql = "DELETE FROM users WHERE id = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

                preparedStatement.setInt(1, id);

                final int result = preparedStatement.executeUpdate();

                return result > 0;

            } catch (SQLException e) {
                System.err.println("Error: " + e.getLocalizedMessage());
                e.printStackTrace();
            } finally {
                close();
            }
        }

        return false;
    }
}

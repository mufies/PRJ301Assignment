package DAO;

import Model.User;

public interface UserDAO {
    boolean registerUser(String username, String password);
    User loginUser(String username, String password);

}

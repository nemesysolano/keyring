package com.yareakh.keyring.service;

/**
 * <p>Contains signin, signout operations</p>
 */
public interface AuthenticationService {

    /**
     *
     * @param name Key pair name
     * @param password Key pair password
     * @return JWT Token.
     */
    String signup(String name, String password);


    /**
     *
     * @param jwtToken JWT TOken
     */
    void signoff(String jwtToken);
}

package com.licon.liconserver.customer;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/19 13:30
 */
public class UserDetailsRepository {
    protected final Map<String,UserDetails> users = new HashMap<>();

    public void createUser(UserDetails userDetails){
        Assert.notNull(userDetails,"userDetails is not null!");
        users.putIfAbsent(userDetails.getUsername().toLowerCase(),userDetails);
    }

    public void deleteUser(String username){
        users.remove(username.toLowerCase());
    }

    public void updateUser(UserDetails userDetails){
        users.put(userDetails.getUsername().toLowerCase(), userDetails);
    }

    public boolean userExists(String username){
        return users.containsKey(username.toLowerCase());
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext()
                .getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context "
                            + "for current user.");
        }

        String username = currentUser.getName();



        UserDetails user = users.get(username);

        if (user == null) {
            throw new IllegalStateException("Current user doesn't exist in database.");
        }

        System.out.println("密码已经修改，但是无法更新！");
    }

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails user = users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), user.getAuthorities());
    }
}

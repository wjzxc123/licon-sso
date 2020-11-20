package com.licon.liconserver.customer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/19 13:46
 */
@Configuration
public class CustomerSecurityConfig {
    @Bean
    UserDetailsRepository userDetailsRepository(){
        UserDetailsRepository userDetailsRepository =
                new UserDetailsRepository();
        userDetailsRepository.createUser(
                User.withUsername("root")
                .password("{noop}root")
                //.authorities(AuthorityUtils.NO_AUTHORITIES)
                .authorities("ROLL_ADMIN")
                .build()
        );
        return userDetailsRepository;
    }

    @Bean
    UserDetailsManager userDetailsManager(
            UserDetailsRepository userDetailsRepository){
        UserDetailsManager userDetailsManager = new UserDetailsManager() {
            @Override
            public void createUser(UserDetails user) {
                userDetailsRepository.createUser(user);
            }

            @Override
            public void updateUser(UserDetails user) {
                userDetailsRepository.updateUser(user);
            }

            @Override
            public void deleteUser(String username) {
                userDetailsRepository.deleteUser(username);
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                userDetailsRepository.changePassword(oldPassword,newPassword);
            }

            @Override
            public boolean userExists(String username) {
                return userDetailsRepository.userExists(username);
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userDetailsRepository.loadUserByUsername(username);
            }
        };
        return userDetailsManager;
    }
}

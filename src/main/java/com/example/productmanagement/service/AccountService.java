package com.example.productmanagement.service;

import com.example.productmanagement.model.Account;
import com.example.productmanagement.model.Role;
import com.example.productmanagement.repository.AccountRepository;
import com.example.productmanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginName(loginName)
            .orElseThrow(() -> new UsernameNotFoundException("Account not found with login: " + loginName));

        Collection<GrantedAuthority> authorities = account.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return User.builder()
            .username(account.getLoginName())
            .password(account.getPassword())
            .authorities(authorities)
            .build();
    }

    public void registerUser(String loginName, String rawPassword) {
        if (loginName == null || loginName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        String normalizedLogin = loginName.trim();
        if (accountRepository.existsByLoginName(normalizedLogin)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseGet(() -> {
                Role role = new Role();
                role.setName("ROLE_USER");
                return roleRepository.save(role);
            });

        Account account = new Account();
        account.setLoginName(normalizedLogin);
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.setRoles(Collections.singletonList(userRole));
        accountRepository.save(account);
    }
}

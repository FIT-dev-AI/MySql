package com.example.productmanagement.service;

import com.example.productmanagement.model.Account;
import com.example.productmanagement.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

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
}

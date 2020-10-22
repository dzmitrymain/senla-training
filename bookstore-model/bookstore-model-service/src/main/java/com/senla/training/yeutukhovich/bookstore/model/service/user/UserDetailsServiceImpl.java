package com.senla.training.yeutukhovich.bookstore.model.service.user;

import com.senla.training.yeutukhovich.bookstore.model.dao.user.UserDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.User;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String SECURITY_ROLE_PREFIX = "ROLE_";

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException(MessageConstant.USER_NOT_FOUND.getMessage()));
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(SECURITY_ROLE_PREFIX + user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                roles);
    }
}

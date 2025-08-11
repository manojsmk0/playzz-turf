package com.example.gakusei.playzz.turf.configure;

import com.example.gakusei.playzz.turf.model.Role;
import com.example.gakusei.playzz.turf.model.Users;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        Optional<Users> optionalUsers = userRepo.findByUserName(userName);
            if(optionalUsers.isEmpty()){
                throw new UsernameNotFoundException("User not found with username " +userName);
            }
                Users users = optionalUsers.get();
            return new User(
                    optionalUsers.get().getUserName(),
                    optionalUsers.get().getPassword(),
                    mapRolesToAuthorities(optionalUsers.get().getRole())
            );
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}

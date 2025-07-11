package com.company.projectjwt1.service;

import com.company.projectjwt1.entity.Client;
import com.company.projectjwt1.entity.CustomUserDetails;
import com.company.projectjwt1.exceptions.LoginCredentialException;
import com.company.projectjwt1.repo.ClientRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.warn("************** Custom User Details service method -> load User by username");
        Optional<Client> clientOpt = clientRepo.findByEmailId(username);
        if(clientOpt.isEmpty())
//            throw new UsernameNotFoundException("User not found");
            throw new LoginCredentialException("User not found");
        Client client = clientOpt.get();
        return new CustomUserDetails(client);
    }
}

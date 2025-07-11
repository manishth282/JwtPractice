package com.company.projectjwt1.service;

import com.company.projectjwt1.entity.Client;
import com.company.projectjwt1.exceptions.ClientNotRegisterException;
import com.company.projectjwt1.payload.ClientRequest;
import com.company.projectjwt1.payload.ClientResponse;
import com.company.projectjwt1.payload.LoginRequest;
import com.company.projectjwt1.repo.ClientRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Value("${exception.clientNotRegisterException}")
    private String clientNotRegisterException;
    @Value("${success.clientRegister}")
    private String clientRegister;

    @Transactional
    public Map<String, String> registerClient(ClientRequest clientRequest) throws ClientNotRegisterException {

        Map<String, String> response = new HashMap<>();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        clientRequest.setPassword(passwordEncoder.encode(clientRequest.getPassword()));

        Client client = clientRepo.save(modelMapper.map(clientRequest, Client.class));
        if (client.getId() == null)
            throw new ClientNotRegisterException(clientNotRegisterException);
        response.put("message",clientRegister );
        return response;
    }

    public List<ClientResponse> getClients() {
        List<ClientResponse> list = new ArrayList<>();
        for (Client client : clientRepo.findAll()) {
            list.add(modelMapper.map(client, ClientResponse.class));
        }
        return list;
    }

    /** This part is to verify login and send jwt token */
    public Map<String, String> login(LoginRequest loginRequest) {
        logger.warn("This is login method inside service class");
        Map<String, String> response = new HashMap<>();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(!authenticate.isAuthenticated()) {
            logger.error("This is Authentication fails");
            throw new RuntimeException("Please give valid credentials");
        }
        response.put("token",jwtService.generateToken(loginRequest.getUsernameOrEmail()));
        return response;
    }
}

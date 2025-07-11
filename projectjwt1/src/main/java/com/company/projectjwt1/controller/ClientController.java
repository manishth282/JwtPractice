package com.company.projectjwt1.controller;

import com.company.projectjwt1.exceptions.ClientNotRegisterException;
import com.company.projectjwt1.payload.ClientRequest;
import com.company.projectjwt1.payload.ClientResponse;
import com.company.projectjwt1.payload.LoginRequest;
import com.company.projectjwt1.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClientController {

    Logger logger = LoggerFactory.getLogger(ClientController.class);
    @Autowired
    private ClientService clientService;

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerClient(@Valid @RequestBody ClientRequest clientRequest) throws ClientNotRegisterException {
        Map<String, String> response = clientService.registerClient(clientRequest);
            return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest){
        logger.warn("This is login method calls");
        return new ResponseEntity<>(clientService.login(loginRequest), HttpStatus.OK);
    }

    @GetMapping("/clients")
    public ResponseEntity<List> getClients(){
        List<ClientResponse> list = clientService.getClients();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}

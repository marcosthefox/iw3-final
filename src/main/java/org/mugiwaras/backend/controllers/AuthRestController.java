package org.mugiwaras.backend.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;
import org.mugiwaras.backend.auth.IUserBusiness;
import org.mugiwaras.backend.auth.User;
import org.mugiwaras.backend.auth.UserJsonSerializer;
import org.mugiwaras.backend.auth.custom.CustomAuthenticationManager;
import org.mugiwaras.backend.auth.filter.AuthConstants;
import org.mugiwaras.backend.auth.response.UserLoginResponse;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.util.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AuthRestController extends BaseRestController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private IUserBusiness userBusiness;

    @PostMapping(value = Constants.URL_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginExternalOnlyToken(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestParam(value = "json", defaultValue = "false") Boolean json) {
        Authentication auth = null;
        StdSerializer<User> ser = null;
        String result = "";
        try {
            auth = authManager.authenticate(((CustomAuthenticationManager) authManager).AuthWrap(username, password));
        } catch (AuthenticationServiceException e0) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        User user = (User) auth.getPrincipal();
        String token = JWT.create().withSubject(user.getUsername()).withClaim("internalId", user.getIdUser()).withClaim("roles", new ArrayList<String>(user.getAuthoritiesStr())).withClaim("email", user.getEmail()).withClaim("version", "1.0.0").withExpiresAt(new Date(System.currentTimeMillis() + AuthConstants.EXPIRATION_TIME)).sign(Algorithm.HMAC512(AuthConstants.SECRET.getBytes()));
        if (json) {
            ser = new UserJsonSerializer(User.class, false, token);

            try {
                result = JsonUtiles.getObjectMapper(User.class, ser, null).writeValueAsString(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<String>(result, HttpStatus.OK);
        }

        UserLoginResponse response = UserLoginResponse.builder().token(token).matriz(user.getMatriz()).build();
        return new ResponseEntity<UserLoginResponse>(response, HttpStatus.OK);
//        return new ResponseEntity<String>(token, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = Constants.URL_REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody User user) {
        try {
            user.setRoles(new HashSet<>());
            User result = userBusiness.add(user);
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        }
        catch(NotFoundException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(BusinessException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}

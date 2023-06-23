package org.mugiwaras.backend.auth;

import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface IUserBusiness {

    public List<User> list() throws BusinessException;

    public User load(String usernameOrEmail) throws NotFoundException, BusinessException;

    public void changePassword(String usernameOrEmail, String oldPassword, String newPassword, PasswordEncoder pEncoder)
            throws BadPasswordException, NotFoundException, BusinessException;

    public void disable(String usernameOrEmail) throws NotFoundException, BusinessException;

    public void enable(String usernameOrEmail) throws NotFoundException, BusinessException;

    User add(User user) throws NotFoundException, BusinessException;


}

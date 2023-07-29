package org.mugiwaras.backend.auth;

import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

public interface IRoleBusiness {
    Role add(Role role) throws NotFoundException, BusinessException;

    Role load(String name) throws NotFoundException, BusinessException;
}

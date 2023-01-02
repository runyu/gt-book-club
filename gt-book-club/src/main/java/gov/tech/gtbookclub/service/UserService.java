package gov.tech.gtbookclub.service;

import gov.tech.gtbookclub.model.dto.UserModel;
import gov.tech.gtbookclub.model.entity.User;

public interface UserService {

    User createUser(UserModel user);

    User readUser(String email);

    User updateUser(UserModel user);

    void deleteUser(String email);

}

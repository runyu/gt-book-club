package gov.tech.gtbookclub.service;

import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.model.request.CreateUserRequest;
import gov.tech.gtbookclub.model.request.UpdateUserRequest;

public interface UserService {

    User createUser(CreateUserRequest createUserRequest);

    User readUser();

    User updateUser(String email, UpdateUserRequest updateUserRequest);

    void deleteUser(String email);

    User getLoggedUser();

    User findUser(String email);
}

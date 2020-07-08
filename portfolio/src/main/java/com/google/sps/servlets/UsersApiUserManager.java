package com.google.sps.servlets; 

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

class UsersApiUserManager implements UserManager {

  public boolean userIsLoggedIn() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.isUserLoggedIn();
  }

  public String currentUserEmail() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.getCurrentUser().getEmail();
  }
}
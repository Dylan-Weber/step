package com.google.sps.servlets; 

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

class UsersApiUserManager implements UserManager {

  private UserService userService;

  UsersApiUserManager() {
    userService = UserServiceFactory.getUserService();
  }

  public boolean userIsLoggedIn() {
    return userService.isUserLoggedIn();
  }

  public String currentUserEmail() {
    return userService.getCurrentUser().getEmail();
  }
}
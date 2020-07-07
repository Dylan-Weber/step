package com.google.sps.servlets;

interface UserManager {
  boolean userIsLoggedIn();
  String currentUserEmail();
}
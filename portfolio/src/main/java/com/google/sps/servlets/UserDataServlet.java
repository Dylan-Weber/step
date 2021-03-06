// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson; 

@WebServlet("/user-data")
public class UserDataServlet extends HttpServlet { 
  
  private UserManager userManager;
  private NicknameService nicknameService;

  @Override 
  public void init() {
    userManager = new UsersApiUserManager();
    nicknameService = new DatastoreNicknameService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    Map<String, Object> params = new HashMap<>();

    boolean loggedIn = userManager.userIsLoggedIn();
    params.put("loggedIn", loggedIn);
    if (loggedIn) {
      String email = userManager.currentUserEmail();
      params.put("email", email);
      String nickname = nicknameService.getNicknameFromEmail(email);
      params.put("nickname", nickname);
    }

    Gson gson = new Gson();
    String userData = gson.toJson(params);
    response.getWriter().println(userData);
  }

  @Override 
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String nickname = request.getParameter("nickname");
    boolean loggedIn = userManager.userIsLoggedIn();
    if (loggedIn) {
      String id = userManager.currentUserId();
      String email = userManager.currentUserEmail();
      nicknameService.setNickname(id, email, nickname);
    }
  }
}
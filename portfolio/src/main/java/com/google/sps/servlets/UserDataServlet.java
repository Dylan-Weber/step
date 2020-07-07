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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    Map<String, Object> params = new HashMap<>();

    UserManager userManager = new UsersApiUserManager();
    boolean loggedIn = userManager.userIsLoggedIn();
    params.put("loggedIn", loggedIn);
    if (loggedIn) {
      String email = userManager.currentUserEmail();
      params.put("email", email);
    }

    Gson gson = new Gson();
    String userData = gson.toJson(params);
    response.getWriter().println(userData);
  }
}
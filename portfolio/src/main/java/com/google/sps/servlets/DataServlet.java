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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson; 

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<String> comments = new ArrayList<>();
    comments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. - Julius Caesar");
    comments.add("Suspendisse diam risus, sollicitudin sed feugiat sit amet, suscipit eget arcu. - Nero");
    comments.add("Donec finibus faucibus risus et consequat. - Constantine");

    response.setContentType("application/json;");
    Gson gson = new Gson();
    
    String commentsJson = gson.toJson(comments);

    response.getWriter().println(commentsJson);
  }
}
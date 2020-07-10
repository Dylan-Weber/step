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

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@WebServlet("/steam-games-data")
public class SteamGamesDataServlet extends HttpServlet {

  private List<Map<String, Object>> gameData;

  @Override
  public void init() {
    gameData = new ArrayList<>();
    try (Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/games-features.csv"))) {
      scanner.nextLine(); // Skips the header
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] cells = line.split(",");
        Map<String, Object> gameAttributes = new HashMap<>();
        String gameName = cells[2];
        Integer owners = Integer.valueOf(cells[15]);
        Boolean vrSupport = Boolean.valueOf(cells[42]);

        gameAttributes.put("name", gameName);
        gameAttributes.put("owners", owners);
        gameAttributes.put("vrSupport", vrSupport);
        gameData.add(gameAttributes);
      }
    }
    gameData.sort((a, b) -> ((Integer) b.get("owners") - (Integer) a.get("owners")));
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String unparsedCount = request.getParameter("count");
    int count;
    if (unparsedCount == null) {
      count = gameData.size();
    } else {
      count = Integer.valueOf(unparsedCount);
    }

    List<Map<String, Object>> limitedGameData = gameData.stream()
      .limit(count)
      .collect(Collectors.toList());
    Gson gson = new Gson();
    String json = gson.toJson(limitedGameData);
    response.getWriter().println(json);
  }
}

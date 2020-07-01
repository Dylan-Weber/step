package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.google.gson.Gson; 

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet { 

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    
    int commentCount = Integer.parseInt(getParameter(request, "count", "10"));
    int pageNumber = Integer.parseInt(getParameter(request, "page", "1"));

    CommentService commentHandler = new DatastoreCommentService();
    List<String> comments = commentHandler.getCommentsFromDatabase(commentCount, pageNumber);

    Gson gson = new Gson();
    String commentsJson = gson.toJson(comments);

    response.getWriter().println(commentsJson);
  }

  @Override 
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getParameter(request, "comment", "");
    if (!comment.equals("")) {
      CommentService commentHandler = new DatastoreCommentService();
      commentHandler.addCommentToDatabase(comment);
    }
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson; 

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private CommentService commentService;
  private UserManager userManager;
  private NicknameService nicknameService;

  @Override
  public void init() {
    commentService = new DatastoreCommentService();
    userManager = new UsersApiUserManager();
    nicknameService = new DatastoreNicknameService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    
    int commentCount = Integer.parseInt(getParameter(request, "count", "10"));
    int pageNumber = Integer.parseInt(getParameter(request, "page", "1"));

    List<Comment> commentsWithEmailAuthors = commentService.getComments(commentCount, pageNumber);
    int numberOfPages = (int) Math.max(1, commentService.getNumberOfPages(commentCount));
    List<Comment> commentsWithNicknameAuthors = getCommentsWithNicknameAuthors(commentsWithEmailAuthors);
    CommentSectionData data = new CommentSectionData(commentsWithNicknameAuthors, numberOfPages);
    
    Gson gson = new Gson();
    String commentsJson = gson.toJson(data);

    response.getWriter().println(commentsJson);
  }

  private List<Comment> getCommentsWithNicknameAuthors(List<Comment> commentsWithEmailAuthors) {
    return commentsWithEmailAuthors
      .stream()
      .map(comment -> getSingleCommentWithNicknameAuthor(comment))
      .collect(Collectors.toList());
  }

  private Comment getSingleCommentWithNicknameAuthor(Comment commentWithEmailAuthor) {
    String email = commentWithEmailAuthor.author;
    String nickname = nicknameService.getNicknameFromEmail(email);
    String content = commentWithEmailAuthor.content;
    return new Comment(nickname, content);
  }

  @Override 
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getParameter(request, "comment", "");
    if (!comment.equals("") && userManager.userIsLoggedIn()) {
      commentService.addCommentToDatabase(comment, userManager.currentUserEmail());
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
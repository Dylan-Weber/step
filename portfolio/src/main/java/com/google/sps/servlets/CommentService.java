package com.google.sps.servlets;

import java.util.List;

interface CommentService {

  List<Comment> getComments(int commentCount, int pageNumber);

  void addCommentToDatabase(String commentText, String email);

  void deleteAllComments();

  int getNumberOfPages(int commentsPerPage);
}

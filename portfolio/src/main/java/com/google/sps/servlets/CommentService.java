package com.google.sps.servlets;

import java.util.List;

interface CommentService {

  List<String> getComments(int commentCount, int pageNumber);

  void addCommentToDatabase(String commentText);

  void deleteAllComments();

  int getNumberOfPages(int commentsPerPage);
}

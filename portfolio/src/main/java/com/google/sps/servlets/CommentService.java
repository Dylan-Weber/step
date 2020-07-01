package com.google.sps.servlets;

import java.util.List;

interface CommentService {

  List<String> getCommentsFromDatabase(int commentCount, int pageNumber);

  void addCommentToDatabase(String commentText);

  void deleteAllComments();

  int getNumberOfPages(int commentsPerPage);
}

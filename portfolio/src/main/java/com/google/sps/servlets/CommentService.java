package com.google.sps.servlets;

import java.util.List;

interface CommentService {

  List<String> getCommentsFromDatabase(int commentCount);

  void addCommentToDatabase(String commentText);

  void deleteAllComments();
}

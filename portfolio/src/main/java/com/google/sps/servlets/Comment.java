package com.google.sps.servlets;

final class Comment {
  final String author;
  final String content;

  public Comment(String author, String content) {
    this.author = author;
    this.content = content;
  }
}
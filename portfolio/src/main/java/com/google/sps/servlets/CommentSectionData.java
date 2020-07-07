package com.google.sps.servlets;

import java.util.List;

final class CommentSectionData {
    final List<Comment> comments;
    final int numberOfPages;

    CommentSectionData(List<Comment> comments, int numberOfPages) {
        this.comments = comments;
        this.numberOfPages = numberOfPages;
    }
}
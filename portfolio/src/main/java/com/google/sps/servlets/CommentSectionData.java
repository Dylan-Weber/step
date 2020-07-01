package com.google.sps.servlets;

import java.util.List;

final class CommentSectionData {
    final List<String> comments;
    final int numberOfPages;

    CommentSectionData(List<String> comments, int numberOfPages) {
        this.comments = comments;
        this.numberOfPages = numberOfPages;
    }
}
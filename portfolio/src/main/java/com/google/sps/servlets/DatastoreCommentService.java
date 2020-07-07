package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;

import java.lang.Math;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

class DatastoreCommentService implements CommentService {

  private final Query COMMENT_QUERY = new Query("Comment")
    .addSort("timestamp", SortDirection.DESCENDING);

  private final Query KEYS_ONLY_COMMENT_QUERY = new Query("Comment")
    .addSort("timestamp", SortDirection.DESCENDING).setKeysOnly();
  
  private PreparedQuery buildPreparedResults(Query query) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    return results;
  }

  private List<Comment> fetchCommentsWithOptions(PreparedQuery results, FetchOptions options) {
    return results.asList(options)
            .stream()
            .map(e -> new Comment((String) e.getProperty("author"), (String) e.getProperty("text")))
            .collect(Collectors.toList());
  }

  @Override
  public List<Comment> getComments(int commentCount, int pageNumber) {
    PreparedQuery results = buildPreparedResults(COMMENT_QUERY);
    int offset = (pageNumber - 1) * commentCount;
    FetchOptions options = 
      FetchOptions.Builder
        .withLimit(commentCount)
        .offset(offset);
    return fetchCommentsWithOptions(results, options);
  }

  @Override
  public void addCommentToDatabase(String commentText, String email) {
    Entity commentEntity = new Entity("Comment");
    long timestamp = System.currentTimeMillis();
    commentEntity.setProperty("author", email);
    commentEntity.setProperty("text", commentText);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
  }

  @Override
  public void deleteAllComments() {
    PreparedQuery results = buildPreparedResults(KEYS_ONLY_COMMENT_QUERY);
    FetchOptions options = FetchOptions.Builder.withDefaults();
    List<Entity> commentEntities = results.asList(options);
    
    List<Key> commentKeys = 
        results.asList(options)
            .stream()
            .map(e -> e.getKey())
            .collect(Collectors.toList());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.delete(commentKeys);
  }

  @Override
  public int getNumberOfPages(int commentsPerPage) {
    PreparedQuery results = buildPreparedResults(COMMENT_QUERY);
    FetchOptions options = FetchOptions.Builder.withDefaults();
    int numberOfComments = results.countEntities(options);
    return (int) Math.ceil((float) numberOfComments / commentsPerPage);
  }
}

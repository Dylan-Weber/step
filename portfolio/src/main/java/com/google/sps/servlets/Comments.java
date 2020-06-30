package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import java.util.stream.Collectors;

import java.util.List;
import java.util.ArrayList;

public final class Comments {

private static final Query COMMENT_QUERY = new Query("Comment")
    .addSort("timestamp", SortDirection.DESCENDING);

private static final Query KEYS_ONLY_COMMENT_QUERY = new Query("Comment")
    .addSort("timestamp", SortDirection.DESCENDING).setKeysOnly();

  //Empty private constructor to make sure the class isn't instantiable
  private Comments() { } 
  
  private static PreparedQuery buildPreparedResults(Query query) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    return results;
  }

  private static List<String> fetchCommentsWithOptions(PreparedQuery results, FetchOptions options) {
    List<String> comments = 
        results.asList(options)
            .stream()
            .map(e -> (String) e.getProperty("text"))
            .collect(Collectors.toList());
    return comments;
  }

  static List<String> getCommentsFromDatabase(FetchOptions options) {
    PreparedQuery results = buildPreparedResults(COMMENT_QUERY);
    return fetchCommentsWithOptions(results, options);
  }

  static void addCommentToDatabase(String commentText) {
    Entity commentEntity = new Entity("Comment");
    long timestamp = System.currentTimeMillis();
    commentEntity.setProperty("text", commentText);
    commentEntity.setProperty("timestamp", timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
  }

  static void deleteAllComments() {
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
}


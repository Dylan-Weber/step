package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;

import java.util.List;
import java.util.ArrayList;

public final class Comments {

  //Empty private constructor to make sure the class isn't instantiable
  private Comments() { } 
  
  private static PreparedQuery buildPreparedResults() {
    Query query = new Query("Comment")
    .addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    return results;
  }

  private static List<String> fetchCommentsWithOptions(PreparedQuery results, FetchOptions options) {
    List<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable(options)) {
        String comment = (String) entity.getProperty("text");
        comments.add(comment);
    }
    return comments;
  }

  static List<String> getCommentsFromDatabase(FetchOptions options) {
    PreparedQuery results = buildPreparedResults();
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
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = buildPreparedResults();
    List<Entity> commentEntities = results.asList();
    
    List<Key> commentKeys = new ArrayList<>();
    for (Entity commentEntity : commentEntities) {
      commentKeys.add(commentEntity.getKey());
    }

    datastore.delete(commentKeys);
  }
}


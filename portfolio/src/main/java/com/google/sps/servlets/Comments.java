protected final class Comments {

  //Empty private constructor to make sure the class isn't instantiable
  private Comments() { } 
  
  protected List<String> getCommentsFromDatabase(int count) {
    Query query = new Query("Comment")
    .addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<String> comments = new ArrayList<>();
    FetchOptions options = FetchOptions.Builder.withLimit(count);

    for (Entity entity : results.asIterable(options)) {
    String comment = (String) entity.getProperty("text");
    comments.add(comment);
    }
    return comments;
  }

  protected void addCommentToDatabase(String commentText) {
    Entity commentEntity = new Entity("Comment");
    long timestamp = System.currentTimeMillis();
    commentEntity.setProperty("text", commentText);
    commentEntity.setProperty("timestamp", timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
  }
}


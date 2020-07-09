package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

class DatastoreNicknameService implements NicknameService {
  public void setNickname(String id, String email, String nickname) {
    Entity userDataEntity = new Entity("UserData", id);
    userDataEntity.setProperty("id", id);
    userDataEntity.setProperty("email", email);
    userDataEntity.setProperty("nickname", nickname);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(userDataEntity);
  }
  
  public String getNicknameFromEmail(String email) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserData")
            .setFilter(new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return email;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }
}
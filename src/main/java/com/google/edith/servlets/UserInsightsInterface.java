package com.google.edith.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import java.util.List;
import java.util.Optional;

public interface UserInsightsInterface {
  void createUserStats();
  void updateUserStats(List<Key> newItems);
  List<WeekInfo> aggregateUserData();
  String createJson();
  Optional<Entity> retreiveUserStats();
}
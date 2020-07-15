package com.google.edith.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Optional;

public interface UserInsightsInterface {
  void createUserStats();
  void updateUserStats(List<Key> newItems);
  ImmutableMap<String, String> aggregateUserData();
  String createJson();
  Optional<Entity> retreiveUserStats();
}
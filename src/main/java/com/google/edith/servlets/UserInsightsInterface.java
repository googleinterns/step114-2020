package com.google.edith.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import java.util.List;
import java.util.Optional;

/**
 * This interface defines the methods that all implmenting
 * classes should have. 
 */
public interface UserInsightsInterface {
  void createUserStats(String userId);
  void updateUserStats(String userId, List<Key> newItems);
  List<WeekInfo> aggregateUserData(String userId);
  String createJson(String userId);
  Optional<Entity> retreiveUserStats(String userId);
}

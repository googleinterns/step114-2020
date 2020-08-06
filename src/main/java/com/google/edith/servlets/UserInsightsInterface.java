package com.google.edith.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import java.util.List;
import java.util.Optional;

/** This interface defines the methods that all implmenting classes should have. */
public interface UserInsightsInterface {
  /**
   * This should only be called each time a new user makes an accout.
   *
   * @param userId current userId
   */
  void createUserStats(String userId);

  /**
   * Updates the Items list in the UserStats Entity in datastore corresponding to this user.
   *
   * @param userId - current userId
   * @param items - Non-null list of item Keys to be added to the current Item list.
   */
  void updateUserStats(String userId, List<Key> newItems);

  /**
   * Copmiles the spending using the Item list found in this user's UserStats Entity in datastore.
   * TODO (malachibre): Allow for various time periods (only calculates weekly aggregate now).
   *
   * @param userId current userId
   * @return A list of {@code WeekInfo} objects relating a time period to the spending in that time
   *     period.
   */
  List<WeekInfo> aggregateUserData(String userId);

  /**
   * Creates a Json string that contains the weekly aggregate for this user and the items this user
   * purchased.
   *
   * @param userId current userId
   * @return a Json formatted String of items and an aggregate.
   */
  String createJson(String userId);

  /**
   * Finds the UserStats entity corresponding to {@code userId} in datastore and returns this entity
   * contained inside an Optional.
   *
   * @param userId current userId
   * @return UserStats entity for this user
   */
  Optional<Entity> retreiveUserStats(String userId);
}

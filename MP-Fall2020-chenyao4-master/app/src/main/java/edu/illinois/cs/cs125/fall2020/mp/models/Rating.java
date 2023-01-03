package edu.illinois.cs.cs125.fall2020.mp.models;

/**
 * Rating class for starting client rating of courses.
 */
public class Rating {
  /** Meaning the course has not been rated yet. */
  public static final double NOT_RATED = -1.0;
  private String id;
  private double rating;
  /**
   * @param setId     the number for this Summary
   * @param setRating      the title for this Summary
   */
  public Rating(final String setId, final double setRating) {
    id = setId;
    rating = setRating;
  }
  /**.
   * empty constructor
   */
  public Rating() { }
  /**
   * Get the id.
   *
   * @return the id.
   */
  public String getId() {
    return id;
  }
  /**
   * Get the rating for this course.
   *
   * @return the rating for this course
   */
  public double getRating() {
    return rating;
  }
}

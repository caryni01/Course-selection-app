package edu.illinois.cs.cs125.fall2020.mp.models;
/**
 * Model holding the course information after click.
 */
public final class Course extends Summary {
  private String description;
    /**
     * Get the description for this Summary.
     *
     * @return the description for this Summary
     */
  public String getDescription() {
    return description;
  }
    /**.
     * Create an empty constructor
     */
  public Course() {}
    /**
     * Create a Summary with the provided fields.
     *
     * @param setDescription year for this course
     */
  public Course(final String setDescription) {
    description = setDescription;
  }
}

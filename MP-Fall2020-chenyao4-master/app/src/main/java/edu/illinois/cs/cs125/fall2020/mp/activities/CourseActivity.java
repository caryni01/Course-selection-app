package edu.illinois.cs.cs125.fall2020.mp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.concurrent.CompletableFuture;

import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.databinding.ActivityCourseBinding;
import edu.illinois.cs.cs125.fall2020.mp.models.Course;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import edu.illinois.cs.cs125.fall2020.mp.network.Client;
/**
 * Callback fired when a user clicks on a course in the list view.
 *
 *
 */
public final class CourseActivity extends AppCompatActivity implements Client.CourseClientCallbacks {
  private static final String TAG = CourseActivity.class.getSimpleName();

  private ActivityCourseBinding binding;
  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    Log.i(TAG, "Course Activity Started");
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    String json = intent.getStringExtra("COURSE");
    Log.d(TAG, json);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Summary summary = new Summary();
    try {
      summary = objectMapper.readValue(json, Summary.class);
    } catch (Exception e) {
      Log.d(TAG, "exception");
    }

    CourseableApplication application = (CourseableApplication) getApplication();
    application.getCourseClient().getCourse(summary, this);
    String clientID = application.getclientID();

    Client client = Client.start();
    CompletableFuture<Course> completableFuture = new CompletableFuture<>();
    client.getCourse(summary, new Client.CourseClientCallbacks() {
        @Override
        public void courseResponse(final Summary summary, final Course course) {
            completableFuture.complete(course);
        }
    });
    Course course = new Course();
    try {
      course = completableFuture.get();
    } catch (Exception e) {
      Log.d(TAG, "exception");
    }

    CompletableFuture<Rating> completableFuture2 = new CompletableFuture<>();
    client.getRating(summary, clientID, new Client.CourseClientCallbacks() {
      @Override
      public void yourRating(final Summary summary, final Rating rating) {
        completableFuture2.complete(rating);
      }
    });
    Rating rating = new Rating();
    try {
      rating = completableFuture2.get();
    } catch (Exception e) {
      Log.d(TAG, "exception");
    }

    binding = DataBindingUtil.setContentView(this, R.layout.activity_course);

    final RatingBar bar = findViewById(R.id.rating);
    Summary finalSummary = summary;
    bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
      @Override
      public void onRatingChanged(final RatingBar ratingBar, final float v, final boolean b) {
        double rating = bar.getRating();
        Rating instance = new Rating(clientID, rating);
        client.postRating(finalSummary, instance, new Client.CourseClientCallbacks() {
          @Override
          public void yourRating(final Summary summary, final Rating rating) {
            completableFuture2.complete(rating);
          }
        });
      }
    });
    String fullTitile = summary.getDepartment() + " " + summary.getNumber()
            + ": " + summary.getTitle();
    binding.textview1.setText(fullTitile);
    binding.textview2.setText(course.getDescription());
    binding.rating.setRating((float) rating.getRating());
  }
}

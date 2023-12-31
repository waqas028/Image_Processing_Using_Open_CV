// Generated by view binder compiler. Do not edit!
package com.example.open_cv_android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.open_cv_android.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityImageProcessingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button cartoonFilterButton;

  @NonNull
  public final ConstraintLayout filterConstraintLayout;

  @NonNull
  public final Button grayFilterButton;

  @NonNull
  public final Button houghButton;

  @NonNull
  public final RecyclerView imageProcessingRecycleView;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final Button medianFilterButton;

  @NonNull
  public final Button saveImageButton;

  @NonNull
  public final SeekBar seekBar;

  @NonNull
  public final Button shareImageButton;

  @NonNull
  public final Button thresholdingFilterButton;

  private ActivityImageProcessingBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button cartoonFilterButton, @NonNull ConstraintLayout filterConstraintLayout,
      @NonNull Button grayFilterButton, @NonNull Button houghButton,
      @NonNull RecyclerView imageProcessingRecycleView, @NonNull ImageView imageView,
      @NonNull Button medianFilterButton, @NonNull Button saveImageButton, @NonNull SeekBar seekBar,
      @NonNull Button shareImageButton, @NonNull Button thresholdingFilterButton) {
    this.rootView = rootView;
    this.cartoonFilterButton = cartoonFilterButton;
    this.filterConstraintLayout = filterConstraintLayout;
    this.grayFilterButton = grayFilterButton;
    this.houghButton = houghButton;
    this.imageProcessingRecycleView = imageProcessingRecycleView;
    this.imageView = imageView;
    this.medianFilterButton = medianFilterButton;
    this.saveImageButton = saveImageButton;
    this.seekBar = seekBar;
    this.shareImageButton = shareImageButton;
    this.thresholdingFilterButton = thresholdingFilterButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityImageProcessingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityImageProcessingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_image_processing, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityImageProcessingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cartoonFilterButton;
      Button cartoonFilterButton = ViewBindings.findChildViewById(rootView, id);
      if (cartoonFilterButton == null) {
        break missingId;
      }

      id = R.id.filterConstraintLayout;
      ConstraintLayout filterConstraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (filterConstraintLayout == null) {
        break missingId;
      }

      id = R.id.grayFilterButton;
      Button grayFilterButton = ViewBindings.findChildViewById(rootView, id);
      if (grayFilterButton == null) {
        break missingId;
      }

      id = R.id.houghButton;
      Button houghButton = ViewBindings.findChildViewById(rootView, id);
      if (houghButton == null) {
        break missingId;
      }

      id = R.id.imageProcessingRecycleView;
      RecyclerView imageProcessingRecycleView = ViewBindings.findChildViewById(rootView, id);
      if (imageProcessingRecycleView == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.medianFilterButton;
      Button medianFilterButton = ViewBindings.findChildViewById(rootView, id);
      if (medianFilterButton == null) {
        break missingId;
      }

      id = R.id.saveImageButton;
      Button saveImageButton = ViewBindings.findChildViewById(rootView, id);
      if (saveImageButton == null) {
        break missingId;
      }

      id = R.id.seekBar;
      SeekBar seekBar = ViewBindings.findChildViewById(rootView, id);
      if (seekBar == null) {
        break missingId;
      }

      id = R.id.shareImageButton;
      Button shareImageButton = ViewBindings.findChildViewById(rootView, id);
      if (shareImageButton == null) {
        break missingId;
      }

      id = R.id.thresholdingFilterButton;
      Button thresholdingFilterButton = ViewBindings.findChildViewById(rootView, id);
      if (thresholdingFilterButton == null) {
        break missingId;
      }

      return new ActivityImageProcessingBinding((ConstraintLayout) rootView, cartoonFilterButton,
          filterConstraintLayout, grayFilterButton, houghButton, imageProcessingRecycleView,
          imageView, medianFilterButton, saveImageButton, seekBar, shareImageButton,
          thresholdingFilterButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

/*
 * Copyright (C) 2016 JetRadar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetradar.multibackstack;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager;

public class BackStackEntry implements Parcelable {
  public final String fname;
  public final SavedState state;
  public final Bundle args;

  public BackStackEntry(@NonNull String fname, @Nullable SavedState state, @Nullable Bundle args) {
    this.fname = fname;
    this.state = state;
    this.args = args;
  }

  @NonNull
  public static BackStackEntry create(@NonNull FragmentManager fm, @NonNull Fragment f) {
    String fname = f.getClass().getName();
    SavedState state = fm.saveFragmentInstanceState(f);
    Bundle args = f.getArguments();
    return new BackStackEntry(fname, state, args);
  }

  @NonNull
  public Fragment toFragment(@NonNull Context context) {
    Fragment f = Fragment.instantiate(context, fname);
    f.setInitialSavedState(state);
    f.setArguments(args);
    return f;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(fname);
    out.writeBundle(args);

    if (state == null) {
      out.writeInt(NO_STATE);
    } else if (state.getClass() == SavedState.class) {
      out.writeInt(SAVED_STATE);
      state.writeToParcel(out, flags);
    } else {
      out.writeInt(PARCELABLE_STATE);
      out.writeParcelable(state, flags);
    }
  }

  private BackStackEntry(Parcel in) {
    final ClassLoader loader = getClass().getClassLoader();
    fname = in.readString();
    args = in.readBundle(loader);

    switch (in.readInt()) {
      case NO_STATE:
        state = null;
        break;
      case SAVED_STATE:
        state = SavedState.CREATOR.createFromParcel(in);
        break;
      case PARCELABLE_STATE:
        state = in.readParcelable(loader);
        break;
      default:
        throw new IllegalStateException();
    }
  }

  private static final int NO_STATE = -1;
  private static final int SAVED_STATE = 0;
  private static final int PARCELABLE_STATE = 1;

  public static final Creator<BackStackEntry> CREATOR = new Creator<BackStackEntry>() {

    @Override
    public BackStackEntry createFromParcel(Parcel in) {
      return new BackStackEntry(in);
    }

    @Override
    public BackStackEntry[] newArray(int size) {
      return new BackStackEntry[size];
    }
  };
}
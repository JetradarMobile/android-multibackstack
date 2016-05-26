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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;

public class BackStackManager {
  private static final int FIRST_INDEX = 0;
  private static final int UNDEFINED_INDEX = -1;
  protected final LinkedList<BackStack> backStacks = new LinkedList<>();

  public void push(int hostId, @NonNull BackStackEntry entry) {
    BackStack backStack = peekBackStack(hostId);
    if (backStack == null) {
      backStack = new BackStack(hostId);
      backStacks.addFirst(backStack); // push
    }
    backStack.push(entry);
  }

  @Nullable
  public BackStackEntry pop(int hostId) {
    return pop(peekBackStack(hostId));
  }

  @Nullable
  public Pair<Integer, BackStackEntry> pop() {
    BackStack backStack = peekBackStack();
    if (backStack == null) {
      return null;
    }
    BackStackEntry entry = pop(backStack);
    return Pair.create(backStack.hostId, entry);
  }

  @Nullable
  protected BackStackEntry pop(@Nullable BackStack backStack) {
    if (backStack == null) {
      return null;
    }
    BackStackEntry entry = backStack.pop();
    if (backStack.empty()) {
      backStacks.remove(backStack);
    }
    return entry;
  }

  @Nullable
  public BackStackEntry popRoot(int hostId) {
    BackStack backStack = peekBackStack(hostId);
    if (backStack == null) {
      return null;
    }
    BackStackEntry entry = null;
    while (!backStack.empty()) {
      entry = pop(backStack);
    }
    return entry;
  }

  public void clear() {
    backStacks.clear();
  }

  public boolean empty() {
    return backStacks.isEmpty();
  }

  @Nullable
  protected BackStack peekBackStack(int hostId) {
    int index = indexOfBackStack(hostId);
    if (index == UNDEFINED_INDEX) {
      return null;
    }
    BackStack backStack = backStacks.get(index);
    if (index != FIRST_INDEX) {
      backStacks.remove(index);
      backStacks.addFirst(backStack); // push
    }
    return backStack;
  }

  @Nullable
  protected BackStack peekBackStack() {
    return backStacks.peek();
  }

  protected int indexOfBackStack(int hostId) {
    int size = backStacks.size();
    for (int i = 0; i < size; ++i) {
      if (backStacks.get(i).hostId == hostId) return i;
    }
    return UNDEFINED_INDEX;
  }

  public Parcelable saveState() {
    return new BackStackManagerState(backStacks.toArray(new BackStack[backStacks.size()]));
  }

  public void restoreState(Parcelable state) {
    if (state == null) return;
    BackStackManagerState bsmState = (BackStackManagerState) state;
    backStacks.addAll(Arrays.asList(bsmState.backStacks));
  }

  static class BackStackManagerState implements Parcelable {
    final BackStack[] backStacks;

    public BackStackManagerState(BackStack[] backStacks) {
      this.backStacks = backStacks;
    }

    private BackStackManagerState(Parcel in) {
      backStacks = in.createTypedArray(BackStack.CREATOR);
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      out.writeTypedArray(backStacks, flags);
    }

    public static final Creator<BackStackManagerState> CREATOR
        = new Creator<BackStackManagerState>() {

      @Override
      public BackStackManagerState createFromParcel(Parcel in) {
        return new BackStackManagerState(in);
      }

      @Override
      public BackStackManagerState[] newArray(int size) {
        return new BackStackManagerState[size];
      }
    };
  }
}
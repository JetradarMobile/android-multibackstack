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

import java.util.Stack;

public class BackStack implements Parcelable {
  public final int hostId;
  private final Stack<BackStackEntry> entriesStack = new Stack<>();

  public BackStack(int hostId) {
    this.hostId = hostId;
  }

  public void push(@NonNull BackStackEntry entry) {
    entriesStack.push(entry);
  }

  @Nullable
  public BackStackEntry pop() {
    return empty() ? null : entriesStack.pop();
  }

  public boolean empty() {
    return entriesStack.empty();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeInt(hostId);
    int size = entriesStack.size();
    out.writeInt(size);
    for (int i = 0; i < size; i++) {
      entriesStack.get(i).writeToParcel(out, flags);
    }
  }

  private BackStack(Parcel in) {
    hostId = in.readInt();
    int size = in.readInt();
    for (int i = 0; i < size; i++) {
      entriesStack.push(BackStackEntry.CREATOR.createFromParcel(in));
    }
  }

  public static final Creator<BackStack> CREATOR = new Creator<BackStack>() {

    @Override
    public BackStack createFromParcel(Parcel in) {
      return new BackStack(in);
    }

    @Override
    public BackStack[] newArray(int size) {
      return new BackStack[size];
    }
  };
}
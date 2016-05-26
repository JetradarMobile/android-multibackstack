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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class BackStackActivity extends AppCompatActivity {
  private static final String STATE_BACK_STACK = "back_stack";

  protected BackStackManager backStackManager;

  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);
    backStackManager = new BackStackManager();
  }

  @Override
  protected void onDestroy() {
    backStackManager = null;
    super.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(STATE_BACK_STACK, backStackManager.saveState());
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    backStackManager.restoreState(savedInstanceState.getParcelable(STATE_BACK_STACK));
  }

  protected boolean pushToBackStack(int hostId, @NonNull Fragment fragment) {
    try {
      BackStackEntry entry = BackStackEntry.create(getSupportFragmentManager(), fragment);
      backStackManager.push(hostId, entry);
      return true;
    } catch (Exception e) {
      Log.e("MultiBackStack", "Failed to add fragment to back stack", e);
      return false;
    }
  }

  @Nullable
  protected Fragment popFromBackStack(int hostId) {
    BackStackEntry entry = backStackManager.pop(hostId);
    return entry != null ? entry.toFragment(this) : null;
  }

  @Nullable
  protected Pair<Integer, Fragment> popFromBackStack() {
    Pair<Integer, BackStackEntry> pair = backStackManager.pop();
    return pair != null ? Pair.create(pair.first, pair.second.toFragment(this)) : null;
  }

  @Nullable
  protected Fragment popRootFromBackStack(int hostId) {
    BackStackEntry entry = backStackManager.popRoot(hostId);
    return entry != null ? entry.toFragment(this) : null;
  }
}
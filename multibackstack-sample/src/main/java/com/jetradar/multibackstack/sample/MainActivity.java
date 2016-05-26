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

package com.jetradar.multibackstack.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jetradar.multibackstack.BackStackActivity;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener;

public class MainActivity extends BackStackActivity implements OnTabSelectedListener {
  private static final String STATE_TAB_ID = "tab_id";
  private static final int MAIN_TAB_ID = 0;

  private BottomNavigationBar bottomNavigationBar;
  private Fragment curFragment;
  private int curTabId;

  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.activity_main);
    setUpBottomNavigationBar();
  }

  private void setUpBottomNavigationBar() {
    bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation);
    bottomNavigationBar
        .addItem(new BottomNavigationItem(R.drawable.ic_search_24dp, R.string.search))
        .addItem(new BottomNavigationItem(R.drawable.ic_favorite_24dp, R.string.favorite))
        .addItem(new BottomNavigationItem(R.drawable.ic_profile_24dp, R.string.profile))
        .initialise();

    bottomNavigationBar.setTabSelectedListener(this);
    if (getSupportFragmentManager().findFragmentById(R.id.content) == null) {
      bottomNavigationBar.selectTab(MAIN_TAB_ID, false);
      showFragment(fragmentByTab(MAIN_TAB_ID));
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    curFragment = getSupportFragmentManager().findFragmentById(R.id.content);
    curTabId = savedInstanceState.getInt(STATE_TAB_ID);
    bottomNavigationBar.selectTab(curTabId, false);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putInt(STATE_TAB_ID, curTabId);
    super.onSaveInstanceState(outState);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onBackPressed() {
    if (!backStackManager.empty()) {
      Pair<Integer, Fragment> pair = popFromBackStack();
      backTo(pair.first, pair.second);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onTabSelected(int position) {
    if (curFragment != null) {
      pushToBackStack(curTabId, curFragment);
    }
    curTabId = position;
    curFragment = popFromBackStack(curTabId);
    if (curFragment == null) {
      curFragment = fragmentByTab(curTabId);
    }
    replaceFragment(curFragment);
  }

  @Override
  public void onTabReselected(int position) {
    backToRoot();
  }

  @Override
  public void onTabUnselected(int position) {}

  private Fragment fragmentByTab(int tabId) {
    switch (tabId) {
      case 0:
        return ItemListFragment.newInstance(getString(R.string.search));
      case 1:
        return ItemListFragment.newInstance(getString(R.string.favorite));
      case 2:
        return ItemListFragment.newInstance(getString(R.string.profile));
      default:
        throw new IllegalArgumentException();
    }
  }

  public void showFragment(@NonNull Fragment fragment) {
    showFragment(fragment, true);
  }

  public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
    if (curFragment != null && addToBackStack) {
      pushToBackStack(curTabId, curFragment);
    }
    curFragment = fragment;
    replaceFragment(fragment);
  }

  private void backTo(int tabId, @NonNull Fragment fragment) {
    curTabId = tabId;
    bottomNavigationBar.selectTab(curTabId, false);
    curFragment = fragment;
    replaceFragment(fragment);
    getSupportFragmentManager().executePendingTransactions();
  }

  private void backToRoot() {
    Fragment originalRootFragment = fragmentByTab(curTabId);
    if (curFragment.getClass() != originalRootFragment.getClass()) {
      Fragment rootFragment = popRootFromBackStack(curTabId);
      if (rootFragment == null || rootFragment.getClass() != originalRootFragment.getClass()) {
        rootFragment = originalRootFragment;
      }
      curFragment = rootFragment;
      replaceFragment(rootFragment);
    }
  }

  private void replaceFragment(@NonNull Fragment fragment) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction tr = fm.beginTransaction();
    tr.replace(R.id.content, fragment);
    tr.commit();
  }
}
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
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends ListFragment {
  private static final String ARG_SECTION = "section";

  private List<String> items;

  public ItemListFragment() {}

  public static ItemListFragment newInstance(String section) {
    ItemListFragment fragment = new ItemListFragment();
    Bundle args = new Bundle();
    args.putString(ARG_SECTION, section);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String section = getArguments().getString(ARG_SECTION);
    items = createItemsForSection(section);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setUpActionBar();

    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    ((MainActivity) getActivity()).showFragment(ItemFragment.newInstance(items.get(position)));
  }

  private List<String> createItemsForSection(String section) {
    int itemsNumber = 10;
    List<String> items = new ArrayList<>(itemsNumber);
    for (int i = 0; i < itemsNumber; i++) {
      items.add(section + " " + (i + 1));
    }
    return items;
  }

  @SuppressWarnings("ConstantConditions")
  private void setUpActionBar() {
    ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setTitle(R.string.app_name);
  }
}
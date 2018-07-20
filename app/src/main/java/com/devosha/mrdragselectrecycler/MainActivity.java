package com.devosha.mrdragselectrecycler;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.afollestad.materialcab.MaterialCab;
/*
MainActivity. Implements MainAdapter.Listener and Material.Callback interfaces
 */
public class MainActivity extends AppCompatActivity implements RecyclerAdapter.Listener, MaterialCab.Callback {

    /*
    Instance Fields
     */
  private DragSelectRecyclerView dragSelectRecyclerView;
  private RecyclerAdapter adapter;
  private MaterialCab cab;

  /*
  Our onCreate method:
    1. Reference toolbar from our layout.
    2. Initialize adapter and callbacks
    3. Reference DragSelectRecyclerView and set its layout manager.
   */
  @SuppressLint("InlinedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
	Toolbar toolbar=findViewById(R.id.main_toolbar);
    toolbar.setBackgroundColor(Color.parseColor("#f39c12"));

    setSupportActionBar(toolbar);

    // Setup adapter and callbacks
    adapter = new RecyclerAdapter(this);

    // Setup the RecyclerView
    dragSelectRecyclerView = findViewById(R.id.list);
    dragSelectRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_width)));
    dragSelectRecyclerView.setAdapter(adapter);

    cab = MaterialCab.restoreState(savedInstanceState, this, this);

  }

  /*
  Save materialCab state
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (cab != null) {
      cab.saveState(outState);
    }
  }

  /*
  Recycler item clicked so toggle selection state
   */
  @Override
  public void onClick(int index) {
    adapter.toggleSelected(index);
  }

  /*
  RecyclerView longClicked so activate DragSelect
   */
  @Override
  public void onLongClick(int index) {
    dragSelectRecyclerView.setDragSelectActive(true, index);
  }

  /*
  RecyclerView item selection has changed so:
    1. If MaterialCab is null then instantiate it.
    2. Set menu and start/display the contextual actionbar.
    3. Set the color of text title in the contextual action bar.
    4. Set the title.
    5. If cab was not null then reset it and finish it.
   */
  @Override
  public void onSelectionChanged(int count) {
    if (count > 0) {
      if (cab == null) {
        cab = new MaterialCab(this, R.id.cab_stub)
                .setMenu(R.menu.cab)
                .setCloseDrawableRes(R.drawable.ic_close)
                .start(this);
        cab.getToolbar().setTitleTextColor(Color.BLACK);
		cab.getToolbar().setBackgroundColor(Color.parseColor("#f39c12"));
      }
      cab.setTitleRes(R.string.cab_title_x, count);
    } else if (cab != null && cab.isActive()) {
      cab.reset().finish();
      cab = null;
    }
  }

  /*
  MaterialCab is created.
   */
  @Override
  public boolean onCabCreated(MaterialCab cab, Menu menu) {
    return true;
  }

  /*
  MaterialCab item is clicked so:
  1. Get the clicked menu item.
  2. If it's done then traverse selected items and add them to a stringBuilder.
  3. Show the selected items in a Toast message.
   */
  @SuppressLint("DefaultLocale")
  @Override
  public boolean onCabItemClicked(MenuItem item) {
    if (item.getItemId() == R.id.done) {
      StringBuilder sb = new StringBuilder();
      int traverse = 0;
      for (Integer index : adapter.getSelectedIndices()) {
        if (traverse > 0)
        {
            sb.append(", ");
        }
        sb.append(adapter.getItem(index));
        traverse++;
      }
      Toast.makeText(this,String.format("Selected Spaceship (%d): %s", adapter.getSelectedIndices().size(), sb.toString()), Toast.LENGTH_LONG) .show();
      adapter.clearSelected();
    }
    return true;
  }

  /*
  back key clicked so clear selected items if they are not empty already.
   */
  @Override
  public void onBackPressed() {
    if (!adapter.getSelectedIndices().isEmpty()) {
      adapter.clearSelected();
    } else {
      super.onBackPressed();
    }
  }

  /*
  MaterialCab to be finish so clear selected items from the adapter.
   */
  @Override
  public boolean onCabFinished(MaterialCab cab) {
    adapter.clearSelected();
    return true;
  }
}

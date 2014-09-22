package dk.d3m.dbs;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import dk.d3m.dbs.exceptions.TagIsNullException;
import dk.d3m.dbs.model.JellyArrayAdapter;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.Sale;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.model.Tag;
import dk.d3m.dbs.model.TagRegister;
import dk.d3m.dbs.networking.RefreshHandler;
import dk.d3m.dbs.tools.FileTool;


public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private TagRegister tagRegister;
    private RefreshHandler refreshHandler;
    private JellyArrayAdapter<Sale> saleAdapter;
    private ListView saleListView;
    private SwipeRefreshLayout swipeLayout;
    private SharedPreferences prefs;

    private ArrayAdapter<Tag> mDrawerAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("### - onCreate - ###");

        initSharedPreferences();
        createRegisters();
        FileTool.loadRegisters(this, pictureRegister, saleRegister, tagRegister);
        initSwipeToRefresh();
        initSaleListView();
        initDrawer();
        initRefreshHandler();
    }

    private void initSharedPreferences() {
        // Get a new preference
        prefs = getPreferences(0);

        // Get editor
        SharedPreferences.Editor editor = prefs.edit();

        // Put default localUN
        if(!prefs.contains("localun")) {
            editor.putInt("localun", 0);
        }

        // Put default host
        if(!prefs.contains("host")) {
            editor.putString("host", "194.255.32.70");
        }

        // Put default port
        if(!prefs.contains("port")) {
            editor.putInt("port", 6666);
        }

        // Commit
        editor.commit();
    }

    private void createRegisters() {
        pictureRegister = new PictureRegister();
        tagRegister = new TagRegister();
        saleRegister = new SaleRegister(pictureRegister, tagRegister);
    }

    private void initSwipeToRefresh() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initSaleListView() {
        saleAdapter = new JellyArrayAdapter<Sale>(this, R.layout.sale_list, saleRegister.getObjects()) {
            @Override
            public void constructRowView(View rowView, Sale object) {
                TextView name = (TextView)rowView.findViewById(R.id.name);
                ImageView image = (ImageView)rowView.findViewById(R.id.image);

                name.setText(object.getName());
                image.setImageBitmap(object.getPicture().getBitmap());
            }
        };
        saleListView = (ListView)findViewById(R.id.saleListView);
        saleListView.setAdapter(saleAdapter);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerAdapter = new ArrayAdapter<Tag>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                tagRegister.getObjects());
        mDrawerList.setAdapter(mDrawerAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initRefreshHandler() {
        refreshHandler = new RefreshHandler(this, saleAdapter, mDrawerAdapter, pictureRegister, saleRegister, tagRegister, prefs);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        System.out.println("### - onPrepareOptionsMenu - ###");

        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        System.out.println("### - onPostCreate - ###");
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("### - onConfigurationChanged - ###");
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("### - onStart - ###");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("### - onStop - ###");
        FileTool.saveRegisters(this, pictureRegister, saleRegister, tagRegister);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("### - onCreateOptionsMenu - ###");
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("### - onOptionsItemSelected - ###");

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        Intent intent;
        switch(id) {
            case R.id.action_help:
                intent = new Intent("dk.d3m.dbs.HELP");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        System.out.println("### - onRefresh - ###");
        refreshHandler.refreshContent();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
            try {
                selectItem(parent, view, position, id);
            } catch (TagIsNullException e) {
                e.printStackTrace();
            }
        }

        private void selectItem(AdapterView parent, View view, int position, long id) throws TagIsNullException {
            Tag tag = tagRegister.getObjects().get(position);
            if(tag != null) {

            } else {
                throw new TagIsNullException("Tag was null on selectItem in NavDrawer");
            }
        }
    }
}

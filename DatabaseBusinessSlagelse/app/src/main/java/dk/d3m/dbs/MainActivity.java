package dk.d3m.dbs;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import dk.d3m.dbs.model.DrawerItemClickListener;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.SaleArrayAdapter;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.model.TagArrayAdapter;
import dk.d3m.dbs.model.TagRegister;
import dk.d3m.dbs.networking.RefreshHandler;
import dk.d3m.dbs.tools.FileTool;


public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private TagRegister tagRegister;
    private RefreshHandler refreshHandler;
    private SaleArrayAdapter saleAdapter;
    private TagArrayAdapter tagAdapter;
    private ListView saleListView;
    private ListView tagListView;
    private SwipeRefreshLayout swipeLayout;
    private SharedPreferences prefs;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createRegisters();
        FileTool.loadRegisters(this, pictureRegister, saleRegister, tagRegister);
        initSharedPreferences();
        initSwipeToRefresh();
        initSaleListView();
        initDrawer();
        initRefreshHandler();
    }

    private void createRegisters() {
        pictureRegister = new PictureRegister();
        saleRegister = new SaleRegister(pictureRegister);
        tagRegister = new TagRegister();
    }

    private void initSharedPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.contains("host")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("host", getString(R.string.pref_host_defaultValue));
            editor.commit();
        }
        if(!prefs.contains("port")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("port", getString(R.string.pref_port_defaultValue));
            editor.commit();
        }
        if(!prefs.contains("refreshOnStart")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("refreshOnStart", true);
            editor.commit();
        }
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
        saleAdapter = new SaleArrayAdapter(this, saleRegister);
        saleListView = (ListView)findViewById(R.id.saleListView);
        saleListView.setAdapter(saleAdapter);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        String[] mPlanetTitles = getResources().getStringArray(R.array.testArray);
        tagAdapter = new TagArrayAdapter(this, tagRegister);
        tagListView = (ListView)findViewById(R.id.left_drawer);
        tagListView.setAdapter(tagAdapter);
        tagListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void initRefreshHandler() {
        refreshHandler = new RefreshHandler(this, saleAdapter, tagAdapter, pictureRegister, saleRegister, tagRegister);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FileTool.loadRegisters(this, pictureRegister, saleRegister, tagRegister);
        if(prefs.getBoolean("refreshOnStart", true)) {
            swipeLayout.setRefreshing(true);
            refreshHandler.refreshContent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileTool.saveRegisters(this, pictureRegister, saleRegister, tagRegister);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FileTool.saveRegisters(this, pictureRegister, saleRegister, tagRegister);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        Intent intent;
        switch(id) {
            case R.id.action_settings:
                intent = new Intent("dk.d3m.dbs.SETTINGS");
                startActivity(intent);
                break;
            case R.id.action_help:
                intent = new Intent("dk.d3m.dbs.HELP");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        refreshHandler.refreshContent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("I AM PAUSED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("I AM RESMUED");
    }
}

package dk.d3m.dbs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.logging.FileHandler;

import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.RegisterHandler;
import dk.d3m.dbs.model.SaleArrayAdapter;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.networking.RefreshHandler;
import dk.d3m.dbs.tools.FileTool;


public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private RefreshHandler refreshHandler;
    private SaleArrayAdapter saleAdapter;
    private ListView saleListView;
    private SwipeRefreshLayout swipeLayout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileTool.loadRegisters(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(!prefs.contains("host")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("host", "192.168.0.31");
            editor.commit();
        }
        if(!prefs.contains("port")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("port", "6666");
            editor.commit();
        }
        if(!prefs.contains("refreshOnStart")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("refreshOnStart", true);
            editor.commit();
        }

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        pictureRegister = RegisterHandler.getPictureRegisterInstance();
        saleRegister = RegisterHandler.getSaleRegisterInstance();
        saleAdapter = new SaleArrayAdapter(this, saleRegister.getObjects());
        saleListView = (ListView)findViewById(R.id.saleListView);
        saleListView.setAdapter(saleAdapter);
        refreshHandler = new RefreshHandler(this, saleAdapter);

        if(prefs.getBoolean("refreshOnStart", true)) {
            swipeLayout.setRefreshing(true);
            refreshHandler.refreshContent();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                Intent intent = new Intent("dk.d3m.dbs.SETTINGS");
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
        FileTool.saveRegisters(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("I AM RESMUED");
        FileTool.loadRegisters(this);
    }
}

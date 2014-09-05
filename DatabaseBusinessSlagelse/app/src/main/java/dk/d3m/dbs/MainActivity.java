package dk.d3m.dbs;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import dk.d3m.dbs.model.JellyArrayAdapter;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.Sale;
import dk.d3m.dbs.model.SaleRegister;
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
            editor.putString("host", "192.168.0.31");
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
        saleRegister = new SaleRegister(pictureRegister);
        tagRegister = new TagRegister();
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

    private void initRefreshHandler() {
        refreshHandler = new RefreshHandler(this, saleAdapter, pictureRegister, saleRegister, tagRegister, prefs);
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
        System.out.println("### - onRefresh - ###");
        refreshHandler.refreshContent();
    }
}

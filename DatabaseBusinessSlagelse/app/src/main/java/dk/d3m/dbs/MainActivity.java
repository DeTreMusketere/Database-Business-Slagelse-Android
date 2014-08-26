package dk.d3m.dbs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.networking.RefreshHandler;


public class MainActivity extends Activity {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private RefreshHandler refreshHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictureRegister = new PictureRegister();
        saleRegister = new SaleRegister(pictureRegister);
        refreshHandler = new RefreshHandler(this, pictureRegister, saleRegister);
    }

    public void getUNbtn(View view) {
        refreshHandler.refreshContent();
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
}

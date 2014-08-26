package dk.d3m.dbs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.networking.ConnectorAsync;


public class MainActivity extends Activity {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private ConnectorAsync connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictureRegister = new PictureRegister();
        saleRegister = new SaleRegister(pictureRegister);
        connector = new ConnectorAsync(true, pictureRegister, saleRegister);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                connector.getCurrrentUpdateNumber();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package dk.d3m.dbs.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import dk.d3m.dbs.R;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.RegisterHandler;
import dk.d3m.dbs.model.SaleArrayAdapter;
import dk.d3m.dbs.model.SaleRegister;

/**
 * Created by Patrick on 26-08-2014.
 */
public class RefreshHandler {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private Activity context;
    private int localUN = 0;
    private SaleArrayAdapter saleAdapter;
    private SwipeRefreshLayout swipeLayout;
    protected SharedPreferences prefs;

    public RefreshHandler(Activity context,SaleArrayAdapter saleAdapter) {
        this.pictureRegister = RegisterHandler.getPictureRegisterInstance();
        this.saleRegister = RegisterHandler.getSaleRegisterInstance();
        this.saleAdapter = saleAdapter;
        this.context = context;
        swipeLayout = (SwipeRefreshLayout) context.findViewById(R.id.swipe_container);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.localUN = prefs.getInt("localun", 0);
    }

    public void refreshContent() {
        RefreshContentAsyncTask r = new RefreshContentAsyncTask();
        r.execute();
    }

    private class RefreshContentAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            System.out.println("RefreshHandler: Running PreExecute");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            System.out.println("RefreshHandler: Running DoInBackground");
            Connector connector = new Connector(context, false);

            boolean connected = connector.connect();
            if(connected) {
                int serverUN = connector.getCurrrentUpdateNumber();
                System.out.println("ServerUN: " + serverUN);
                System.out.println("LocalUN: " + localUN);
                connector.disconnect();

                if(serverUN > localUN) {
                    System.out.println("We need an update...");
                    connected = connector.connect();
                    if(connected) {
                        boolean updated = connector.updateRegisters(localUN);
                        connector.disconnect();
                        if (!updated) {
                            return false;
                        } else {
                            System.out.println("OPDATERE");
                            localUN = serverUN;
                        }
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("We are fully updated!");
                }
                System.out.println("RefreshHandler: Finished DoInBackground");
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            System.out.println("RefreshHandler: Running PostExecute");
            swipeLayout.setRefreshing(false);
            if(!b) {
                Toast.makeText(context, "Could not refresh content", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("localun", localUN);
                editor.commit();
                saleAdapter.notifyDataSetChanged();
                System.out.println("Sales: " + saleRegister.getObjects().size());
                System.out.println("Pictures: " + pictureRegister.getObjects().size());
            }
            System.out.println("RefreshHandler: Finished PostExecute");
        }

    }

}

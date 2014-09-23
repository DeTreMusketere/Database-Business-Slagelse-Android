package dk.d3m.dbs.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import dk.d3m.dbs.R;
import dk.d3m.dbs.model.JellyArrayAdapter;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.Sale;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.model.Tag;
import dk.d3m.dbs.model.TagRegister;

/**
 * Created by Patrick on 26-08-2014.
 */
public class RefreshHandler {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private TagRegister tagRegister;
    private JellyArrayAdapter<Sale> saleAdapter;
    private ArrayAdapter<Tag> mDrawerAdapter;
    private ListView mDrawerList;

    private SwipeRefreshLayout swipeLayout;

    private Activity context;

    private SharedPreferences prefs;

    private int localUN = 0;
    private int serverUN = 0;

    public RefreshHandler(Activity context, JellyArrayAdapter<Sale> saleAdapter, ArrayAdapter<Tag> mDrawerAdapter, ListView mDrawerList, PictureRegister pictureRegister, SaleRegister saleRegister, TagRegister tagRegister, SharedPreferences prefs) {
        this.pictureRegister = pictureRegister;
        this.saleRegister = saleRegister;
        this.tagRegister = tagRegister;
        this.saleAdapter = saleAdapter;
        this.mDrawerAdapter = mDrawerAdapter;
        this.mDrawerList = mDrawerList;
        this.context = context;
        this.prefs = prefs;
        swipeLayout = (SwipeRefreshLayout) context.findViewById(R.id.swipe_container);

        localUN = prefs.getInt("localun", 0);
    }

    public void refreshContent() {
        RefreshContentAsyncTask r = new RefreshContentAsyncTask();
        r.execute();
    }

    private class RefreshContentAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("RefreshHandler: Running PreExecute");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            System.out.println("RefreshHandler: Running DoInBackground");
            Connector connector = new Connector(context, prefs, false, pictureRegister, saleRegister, tagRegister);

            boolean connected = connector.connect();
            if(connected) {
                serverUN = connector.getCurrrentUpdateNumber();
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
            super.onPostExecute(b);
            System.out.println("RefreshHandler: Running PostExecute");
            swipeLayout.setRefreshing(false);
            if(!b) {
                Toast.makeText(context, "Could not refresh content", Toast.LENGTH_SHORT).show();
                serverUN = 0;
            } else {
                localUN = serverUN;
                serverUN = 0;

                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt("localun", localUN);
                editor.commit();

                System.out.println("Objects: " + saleRegister.getObjects().size());
                System.out.println("Sorted: " + saleRegister.getSorted().size());

                saleAdapter.clear();
                saleRegister.sort(null);
                //saleAdapter.addAll(saleRegister.getSorted());

                System.out.println("Objects: " + saleRegister.getObjects().size());
                System.out.println("Sorted: " + saleRegister.getSorted().size());

                RefreshHandler.this.saleAdapter.notifyDataSetChanged();
                RefreshHandler.this.mDrawerAdapter.notifyDataSetChanged();
            }
            System.out.println("RefreshHandler: Finished PostExecute");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            swipeLayout.setRefreshing(false);
            Toast.makeText(context, "Refresh was cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}

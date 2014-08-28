package dk.d3m.dbs.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import dk.d3m.dbs.R;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.SaleRegister;

/**
 * Created by Patrick on 26-08-2014.
 */
public class RefreshHandler {

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private Activity context;
    private int localUN = 0;
    private ProgressBar progressBar;
    //protected SharedPreferences prefs;

    public RefreshHandler(Activity context, PictureRegister pictureRegister, SaleRegister saleRegister) {
        this.pictureRegister = pictureRegister;
        this.saleRegister = saleRegister;
        this.context = context;
        this.progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
        //prefs = PreferenceManager.getDefaultSharedPreferences(context);
        //this.localUN = prefs.getInt("localun", 0);
        this.localUN = 0;
    }

    public void refreshContent() {
        System.out.println("LocalUN: " + localUN);
        RefreshContentAsyncTask r = new RefreshContentAsyncTask();
        r.execute();
    }

    private class RefreshContentAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Connector connector = new Connector(context, false, pictureRegister, saleRegister);

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
                            //localUN = serverUN;
                        }
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("We are fully updated!");
                }

                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            //SharedPreferences.Editor editor = prefs.edit();
            //editor.putInt("localun", localUN);
            //editor.commit();
            if(!b) {
                Toast.makeText(context, "Could not refresh content", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

package dk.d3m.dbs.networking;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.SaleRegister;

/**
 * Created by Patrick on 25-08-2014.
 */
public class ConnectorAsync extends Connector {

    public ConnectorAsync(String address, int port, boolean auto, PictureRegister pictureRegister, SaleRegister saleRegister) {
        super(address, port, auto, pictureRegister, saleRegister);
    }

    public ConnectorAsync(boolean auto, PictureRegister pictureRegister, SaleRegister saleRegister) {
        super(auto, pictureRegister, saleRegister);
    }

    @Override
    public int getCurrrentUpdateNumber() {
        GetUpdateNumberAsync task = new GetUpdateNumberAsync();
        task.execute();
        int back = -1;
        try {
            back = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return back;
    }

    @Override
    public void updateRegisters(int updateNumber) {
        UpdateRegistersAsync task = new UpdateRegistersAsync();
        task.execute(updateNumber);
    }

    private class GetUpdateNumberAsync extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            return getCurrrentUpdateNumber();
        }
    }

    private class UpdateRegistersAsync extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            updateRegisters(params[0]);
            return null;
        }
    }
}

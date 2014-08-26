package dk.d3m.dbs.model;

import android.content.Context;
import android.view.View;

import dk.d3m.dbs.R;

/**
 * Created by Patrick on 26-08-2014.
 */
public class SaleArrayAdapter extends JellyArrayAdapter<Sale> {

    public SaleArrayAdapter(Context context, Register<Sale> register) {
        super(context, R.layout.sale_list, register);
    }

    @Override
    public void constructRowView(View rowView, Sale object) {

    }
}

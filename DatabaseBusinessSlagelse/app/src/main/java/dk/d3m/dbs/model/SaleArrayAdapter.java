package dk.d3m.dbs.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dk.d3m.dbs.R;

/**
 * Created by Patrick on 26-08-2014.
 */
public class SaleArrayAdapter extends JellyArrayAdapter<Sale> {

    public SaleArrayAdapter(Context context, List<Sale> objects) {
        super(context, R.layout.sale_list, objects);
    }

    @Override
    public void constructRowView(View rowView, Sale object) {
        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(object.getName());

        ImageView image = (ImageView)rowView.findViewById(R.id.image);
        image.setImageBitmap(object.getPicture().getBitmap());
    }
}

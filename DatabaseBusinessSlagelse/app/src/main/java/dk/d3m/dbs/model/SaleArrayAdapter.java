package dk.d3m.dbs.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import dk.d3m.dbs.R;

/**
 * Created by Patrick on 26-08-2014.
 */
public class SaleArrayAdapter extends JellyArrayAdapter<Sale> {

    public SaleArrayAdapter(Context context, Register<Sale> register) {
        super(context, R.layout.sale_list, register.getObjects());
    }

    @Override
    public void constructRowView(View rowView, Sale object) {
        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(object.getName());
        if(object.getPicture() == null) {
            System.out.println("PICTURE IS NULL");
        }
        ImageView image = (ImageView)rowView.findViewById(R.id.image);
        image.setImageBitmap(object.getPicture().getBitmap());
    }
}

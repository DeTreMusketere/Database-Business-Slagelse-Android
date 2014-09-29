package dk.d3m.dbs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import dk.d3m.dbs.model.Sale;


public class SaleDetailsActivity extends Activity {

    private TextView titleTV;
    private TextView priceTV;
    private TextView descTV;
    private ImageView pictureIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);

        titleTV = (TextView)this.findViewById(R.id.titleTV);
        priceTV = (TextView)this.findViewById(R.id.prisTV);
        descTV = (TextView)this.findViewById(R.id.descTV);
        pictureIV = (ImageView)this.findViewById(R.id.image);

        Sale sale;

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            sale = (Sale) extra.get("sale");
            if(sale != null) {
                titleTV.setText(sale.getName());
                priceTV.setText(String.valueOf(sale.getPrice()) + " kr.");
                descTV.setText(sale.getDescription());
                pictureIV.setImageBitmap(sale.getPicture().getBitmap());
            } else {
                System.out.println("SALE WAS NULL????");
            }
        } else {
            System.out.println("EXTRA WAS NULL????");
        }


    }
}

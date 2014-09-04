package dk.d3m.dbs.model;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import dk.d3m.dbs.R;

/**
 * Created by Patrick on 02-09-2014.
 */
public class TagArrayAdapter extends JellyArrayAdapter<Tag> {

    public TagArrayAdapter(Context context, Register<Tag> register) {
        super(context, R.layout.tag_list, register.getObjects());
    }

    @Override
    public void constructRowView(View rowView, Tag object) {
        TextView tagTitleTV = (TextView)rowView.findViewById(R.id.tagTitleTV);
        tagTitleTV.setText(object.getName());
    }
}

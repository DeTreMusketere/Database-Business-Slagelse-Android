package dk.d3m.dbs.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Patrick on 25-08-2014.
 */
public abstract class JellyArrayAdapter<DATATYPE extends Data> extends ArrayAdapter<DATATYPE> {

    protected Context context;
    protected List<DATATYPE> objects;
    protected int viewActivity;
    protected int viewActivitySelected;

    public JellyArrayAdapter(Context context, int viewActivity, List<DATATYPE> objects) {
        super(context, viewActivity, objects);
        this.objects = objects;
        this.context = context;
        this.viewActivity = viewActivity;
    }

    public JellyArrayAdapter(Context context, int viewActivitySelected, int viewActivity, List<DATATYPE> objects) {
        super(context, viewActivitySelected, viewActivity, objects);
        this.objects = objects;
        this.context = context;
        this.viewActivity = viewActivity;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        System.out.println("Array objects size: " + objects.size());
    }

    /**
     * Constructs a rowView
     * @param rowView
     * @param object
     */
    public abstract void constructRowView(View rowView, DATATYPE object);

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(viewActivity, parent, false);
        DATATYPE object = objects.get(position);

        constructRowView(rowView, object);

        return rowView;
    }

}

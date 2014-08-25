package dk.d3m.dbs.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Patrick on 25-08-2014.
 */
public abstract class ArrayAdapter<DATATYPE extends Data> extends android.widget.ArrayAdapter<DATATYPE> {

    protected Context context;
    protected Register<DATATYPE> register;
    protected int viewActivity;

    public ArrayAdapter(Context context, int viewActivity, Register<DATATYPE> register) {
        super(context, viewActivity, register.getObjects());
        this.register = register;
        this.context = context;
        this.viewActivity = viewActivity;
    }

    public abstract void constructRowView(View rowView, DATATYPE object);

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(viewActivity, parent, false);
        DATATYPE object = register.get(position);

        constructRowView(rowView, object);

        return rowView;
    }

}

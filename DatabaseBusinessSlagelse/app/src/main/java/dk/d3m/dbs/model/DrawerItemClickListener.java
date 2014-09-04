package dk.d3m.dbs.model;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Patrick on 02-09-2014.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {

    }
}

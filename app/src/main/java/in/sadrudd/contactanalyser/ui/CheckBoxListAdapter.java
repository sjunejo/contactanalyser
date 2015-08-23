package in.sadrudd.contactanalyser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import in.sadrudd.contactanalyser.R;

/**
 * Created by sjunjo on 23/08/15.
 */
public class CheckBoxListAdapter extends ArrayAdapter<String> {

    private String[] data;

    public CheckBoxListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CheckBoxListAdapter(Context context, String[] data){
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            v = layoutInflater.inflate(R.layout.listview_checkbox, null);
        }
        String str = getItem(position);
        CheckBox cbContact = (CheckBox) v.findViewById(R.id.cb_contact);
        cbContact.setText(str);

        return v;
    }
}

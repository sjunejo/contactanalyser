package in.sadrudd.contactanalyser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import in.sadrudd.contactanalyser.R;

/**
 * Created by sjunjo on 25/08/15.
 */
public class EditTextListAdapter extends ArrayAdapter<String> {

    private String[] textViewItems;

    public EditTextListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public EditTextListAdapter(Context context, String[] tvItems){
        super(context, 0, tvItems);
    }

    static class ViewHolder {
        CheckBox cbData;
        boolean isSelected;
        String text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View v = convertView;
        if (v == null){
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            v = layoutInflater.inflate(R.layout.listview_checkbox, null);
            holder.cbData = (CheckBox) v.findViewById(R.id.cb_contact);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        return v;
    }

}

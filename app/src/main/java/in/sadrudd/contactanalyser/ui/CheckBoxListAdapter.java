package in.sadrudd.contactanalyser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Set;
import java.util.TreeSet;

import in.sadrudd.contactanalyser.R;

/**
 * Created by sjunjo on 23/08/15.
 */
public class CheckBoxListAdapter extends ArrayAdapter<String> implements CompoundButton.OnCheckedChangeListener {

    private String[] data;
    private boolean[] isCheckedArray;

    private Set<String> setOfContactsCheckedForRemoval;

    public CheckBoxListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CheckBoxListAdapter(Context context, String[] data){
        super(context, 0, data);
        isCheckedArray = new boolean[data.length];
        setOfContactsCheckedForRemoval = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    }

    static class ViewHolder {
        CheckBox cbContact;
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
            holder.cbContact = (CheckBox) v.findViewById(R.id.cb_contact);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        String str = getItem(position);
        holder.cbContact.setTag(position);
        holder.cbContact.setOnCheckedChangeListener(this);
        holder.cbContact.setText(str);
        holder.cbContact.setChecked(isCheckedArray[position]);
        return v;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String contactName = buttonView.getText().toString();
        int position = (Integer) buttonView.getTag();
        isCheckedArray[position] = buttonView.isChecked();
        if (isChecked)
            setOfContactsCheckedForRemoval.add(contactName);
        else
            setOfContactsCheckedForRemoval.remove(contactName);
    }

    public String[] getSetOfContactsCheckedForRemoval(){
        return setOfContactsCheckedForRemoval.toArray(
                new String[setOfContactsCheckedForRemoval.size()]);
    }
}

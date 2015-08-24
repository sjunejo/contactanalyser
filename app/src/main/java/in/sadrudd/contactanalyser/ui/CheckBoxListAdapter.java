package in.sadrudd.contactanalyser.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.utils.Constants;

/**
 * Created by sjunjo on 23/08/15.
 */
public class CheckBoxListAdapter extends ArrayAdapter<String> implements CompoundButton.OnCheckedChangeListener {

    private String[] currentListOfContacts;
    private boolean[] isCheckedArray;

    private Set<String> setOfContactsCheckedForRemoval;

    public CheckBoxListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CheckBoxListAdapter(Context context, String[] currentListOfContacts){
        super(context, 0, currentListOfContacts);
        this.currentListOfContacts = currentListOfContacts;
        isCheckedArray = new boolean[currentListOfContacts.length];
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
        // BELOW IF STATEMENT IS CHEAP HACK. TODO REPLACE CHEAP HACK
        if (position < currentListOfContacts.length){
            String str = currentListOfContacts[position];
            holder.cbContact.setTag(position);
            holder.cbContact.setOnCheckedChangeListener(this);
            holder.cbContact.setText(str);
            holder.cbContact.setChecked(isCheckedArray[position]);
        }

        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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

    public void removeFromContactsList(String[] contactsRemoved){
        List<String> tempArrayList = new ArrayList<String>();
        List<String> tempArrayList2 = new ArrayList<String>();
        // contactsRemoved is never going to be larger than currentListOfContacts!!
        // We can abuse this by only having the equivalent of a single loop.
        for (int i = 0; i < contactsRemoved.length; i++){
            tempArrayList2.add(contactsRemoved[i]);
            tempArrayList.add(currentListOfContacts[i]);
        }
        for (int i = contactsRemoved.length; i < currentListOfContacts.length; i++){
            tempArrayList.add(currentListOfContacts[i]);
        }
        tempArrayList.removeAll(tempArrayList2);
        this.currentListOfContacts = tempArrayList.toArray(new String[tempArrayList.size()]);
        this.notifyDataSetChanged();
        Log.d(Constants.TAG, "CLOC: " + Arrays.toString(currentListOfContacts));
        isCheckedArray = new boolean[currentListOfContacts.length];
        setOfContactsCheckedForRemoval = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    }


}

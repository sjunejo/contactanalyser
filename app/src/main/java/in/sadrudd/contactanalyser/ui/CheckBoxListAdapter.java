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

    private String[] currentListViewItems;
    private boolean[] isCheckedArray;

    private Set<String> itemsChecked;

    public CheckBoxListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CheckBoxListAdapter(Context context, String[] currentListViewItems){
        super(context, 0, currentListViewItems);
        this.currentListViewItems = currentListViewItems;
        isCheckedArray = new boolean[currentListViewItems.length];
        itemsChecked = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
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
        // BELOW IF STATEMENT IS CHEAP HACK. TODO REPLACE CHEAP HACK
        if (position < currentListViewItems.length){
            String str = currentListViewItems[position];
            holder.cbData.setTag(position);
            holder.cbData.setOnCheckedChangeListener(this);
            holder.cbData.setText(str);
            holder.cbData.setChecked(isCheckedArray[position]);
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
            itemsChecked.add(contactName);
        else
            itemsChecked.remove(contactName);
    }

    public String[] getCheckBoxItemsChecked(){
        return itemsChecked.toArray(
                new String[itemsChecked.size()]);
    }

    public void removeFromListView(String[] itemsRemoved){
        List<String> tempArrayList = new ArrayList<String>();
        List<String> tempArrayList2 = new ArrayList<String>();
        // itemsRemoved is never going to be larger than currentListViewItems!!
        // We can abuse this by only having the equivalent of a single loop.
        for (int i = 0; i < itemsRemoved.length; i++){
            tempArrayList2.add(itemsRemoved[i]);
            tempArrayList.add(currentListViewItems[i]);
        }
        for (int i = itemsRemoved.length; i < currentListViewItems.length; i++){
            tempArrayList.add(currentListViewItems[i]);
        }
        tempArrayList.removeAll(tempArrayList2);
        this.currentListViewItems = tempArrayList.toArray(new String[tempArrayList.size()]);
        this.notifyDataSetChanged();
        Log.d(Constants.TAG, "CLOC: " + Arrays.toString(currentListViewItems));
        isCheckedArray = new boolean[currentListViewItems.length];
        itemsChecked = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    }


}

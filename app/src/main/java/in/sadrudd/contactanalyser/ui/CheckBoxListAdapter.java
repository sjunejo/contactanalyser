package in.sadrudd.contactanalyser.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Set;
import java.util.TreeSet;

import in.sadrudd.contactanalyser.R;

/**
 * Created by sjunjo on 23/08/15.
 */
public class CheckBoxListAdapter extends RecyclerView.Adapter<CheckBoxListAdapter.StringHolder> {

    private String[] currentListViewItems;
    private boolean[] isCheckedArray;

    private Set<String> itemsChecked;

    public CheckBoxListAdapter(String[] currentListViewItems){
        this.currentListViewItems = currentListViewItems;
        isCheckedArray = new boolean[currentListViewItems.length];
        itemsChecked = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    }

    public String[] getCheckBoxItemsChecked(){
        return itemsChecked.toArray(
                new String[itemsChecked.size()]);
    }

    @Override
    public StringHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listview_checkbox, null);
        return new StringHolder(view);
    }

    @Override
    public void onBindViewHolder(StringHolder holder, int position) {
        holder.bindStringAndIsChecked(position);
    }

    @Override
    public int getItemCount() {
        return currentListViewItems.length;
    }

    public class StringHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        private final CheckBox cbData;

        public StringHolder(View itemView){
            super(itemView);
            cbData = (CheckBox) itemView.findViewById(R.id.cb_contact);
            cbData.setOnCheckedChangeListener(this);
        }

        public void bindStringAndIsChecked(int position){
            cbData.setText(currentListViewItems[position]);
            cbData.setChecked(isCheckedArray[position]);
            cbData.setTag(position);
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
    }


}

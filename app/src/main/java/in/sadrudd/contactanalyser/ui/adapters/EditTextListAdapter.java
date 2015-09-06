package in.sadrudd.contactanalyser.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import in.sadrudd.contactanalyser.R;

/**
 * Created by sjunjo on 25/08/15.
 */
public class EditTextListAdapter extends RecyclerView.Adapter<EditTextListAdapter.StringHolder> {

    private String[] textViewItems;
    private String[] contactNames;

    private List<Object> itemsAsStrings;

    public EditTextListAdapter(String[] tvItems){
        this.textViewItems = tvItems;
        contactNames = new String[tvItems.length];
        setHasStableIds(true);
    }

    @Override
    public StringHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listview_textview, null);
        return new StringHolder(view);
    }

    @Override
    public int getItemCount() {
        return textViewItems.length;
    }

    @Override
    public void onBindViewHolder(StringHolder holder, int position) {
        holder.bindStringAndIsChecked(position);
    }

    public String[] getContactNames(){
        return this.contactNames;
    }

    public class StringHolder extends RecyclerView.ViewHolder {
        private final TextView tvPhoneNumber;
        private final EditText etContactName;


        public StringHolder(View itemView){
            super(itemView);
            etContactName = (EditText) itemView.findViewById(R.id.et_contact_name);
            etContactName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = (Integer) etContactName.getTag();
                    contactNames[position] = etContactName.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_phone_number);
        }

        public void bindStringAndIsChecked(int position){
            tvPhoneNumber.setText(textViewItems[position]);
            etContactName.setTag(position);
        }

    }

}

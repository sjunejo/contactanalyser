package in.sadrudd.contactanalyser.ui.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import in.sadrudd.contactanalyser.R;

/**
 * Created by sjunjo on 25/08/15.
 */
public class EditTextListAdapter extends ArrayAdapter<String> {

    private String[] textViewItems;
    private String[] contactNames;

    private EditText etContact;
    private TextView tvPhoneNumber;

    public EditTextListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public EditTextListAdapter(Context context, String[] tvItems){
        super(context, 0, tvItems);
        this.textViewItems = tvItems;
        contactNames = new String[tvItems.length];
    }

    /**
    static class ViewHolder {
        TextView tvPhoneNumber;
        EditText etContact;

    }
     **/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // ViewHolder holder = null;
        View v = convertView;
        if (v == null){
            // holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            v = layoutInflater.inflate(R.layout.listview_textview, null);
            final EditText etContact = (EditText) v.findViewById(R.id.et_contact_name);
            etContact.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    contactNames[position] = etContact.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
            TextView tvPhoneNumber = (TextView) v.findViewById(R.id.tv_phone_number);
            tvPhoneNumber.setText(textViewItems[position]);
            // tv.setTag(holder);
        } else {
            //holder = (ViewHolder) v.getTag();
        }
        return v;
    }

    public String[] getContactNames(){
        return this.contactNames;
    }

}

package in.sadrudd.contactanalyser.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.Arrays;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.utils.Constants;

/**
 * Created by sjunjo on 25/08/15.
 */
public class EnterContactNamesFragment extends ListFragment implements View.OnClickListener,
        IContactFragment {

    private String[] phoneNumbers;

    private Button btnCreateContacts;

    private EnterContactNamesFragmentListener callback;

    public interface EnterContactNamesFragmentListener {
        public void onContactNamesEntered(String[] contactNames, String[] phoneNumbers);
    }

    public static final String ARGS_KEY = "ADD_CONTACTS_TEXTVIEW";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumbers = getArguments().getStringArray(ARGS_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(Constants.TAG, "OnCreateView() called");
        View v = inflater.inflate(R.layout.fragment_add_contact_textviews, container, false);
        setListAdapter(new EditTextListAdapter(getActivity(), phoneNumbers));
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCreateContacts = (Button) view.findViewById(R.id.btn_create_contacts);
        btnCreateContacts.setOnClickListener(this);
        Log.d(Constants.TAG, "Create Contacts fragment loaded and attached");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_contacts:
                createContactsButtonPressed();
                break;
        }
    }

    private void createContactsButtonPressed(){
        String[] contactNames = ((EditTextListAdapter) getListAdapter()).getContactNames();
        Log.d(Constants.TAG, "CONTACT NAMES:");
        Log.d(Constants.TAG, Arrays.toString(contactNames));

        // Lazy loading of callback?
        try {
            callback = (EnterContactNamesFragmentListener) getActivity();
            callback.onContactNamesEntered(contactNames, phoneNumbers);
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnMainFragmentLoadedListener");
        }
    }

    @Override
    public void setData(String[] data) {
        this.phoneNumbers = data;
        ((ArrayAdapter<String>) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public String[] getData() {
        return phoneNumbers;
    }

    @Override
    public CheckBoxListAdapter getAdapter() {
        return (CheckBoxListAdapter) getListAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callback = null;
        setListAdapter(null);
    }
}

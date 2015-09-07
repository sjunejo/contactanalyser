package in.sadrudd.contactanalyser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.ui.ContactAnalyserMainActivity;
import in.sadrudd.contactanalyser.ui.adapters.CheckBoxListAdapter;
import in.sadrudd.contactanalyser.ui.adapters.EditTextListAdapter;
import in.sadrudd.contactanalyser.utils.Constants;

/**
 * Created by sjunjo on 25/08/15.
 */
public class EnterContactNamesFragment extends Fragment implements View.OnClickListener,
        IContactFragment {

    private String[] phoneNumbers;


    private View view;

    private EnterContactNamesFragmentListener callback;

    public interface EnterContactNamesFragmentListener {
        public void onContactNamesEntered(String[] contactNames, String[] phoneNumbers);
    }

    public static final String ARGS_KEY = "ADD_CONTACTS_TEXTVIEW";

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args.containsKey(ARGS_KEY))
         phoneNumbers = args.getStringArray(ARGS_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(Constants.TAG, "OnCreateView() called");
        view = inflater.inflate(R.layout.fragment_add_contact_textviews, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.addcontacts_textviews_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new EditTextListAdapter(phoneNumbers));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCreateContacts = (Button) view.findViewById(R.id.btn_create_contacts);
        btnCreateContacts.setOnClickListener(this);
        Button btnCreateContactsSkip = (Button) view.findViewById(R.id.btn_create_contacts_skip);
        btnCreateContactsSkip.setOnClickListener(this);
        Log.d(Constants.TAG, "Create Contacts fragment loaded and attached");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_contacts:
                createContactsButtonPressed();
                break;
            case R.id.btn_create_contacts_skip:
                ((ContactAnalyserMainActivity) getActivity()).resetToMainFragment();
                break;
        }
    }

    private void createContactsButtonPressed(){
        String[] contactNames = ((EditTextListAdapter) recyclerView.getAdapter()).getContactNames();
        if (atLeastOneContactNameEntered(contactNames)){
            Log.d(Constants.TAG, "CONTACT NAMES:");
            Log.d(Constants.TAG, Arrays.toString(contactNames));

            // Lazy loading of callback?
            try {
                callback = (EnterContactNamesFragmentListener) getActivity();
                callback.onContactNamesEntered(contactNames, phoneNumbers);
            } catch (ClassCastException e){
                throw new ClassCastException(getActivity().toString() + "must implement OnMainFragmentLoadedListener");
            }
        } else {
            Toast.makeText(getActivity(), "At least one contact name must be entered",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean atLeastOneContactNameEntered(String[] contactNames){
        for (String contactName: contactNames){
            if (contactName != null && !contactName.trim().isEmpty())
                return true;
        }
        return false;
    }

    @Override
    public void setData(String[] data) {
        this.phoneNumbers = data;
        (recyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public String[] getData() {
        return phoneNumbers;
    }

    @Override
    public CheckBoxListAdapter getAdapter() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callback = null;
        recyclerView.setAdapter(null);
    }


}

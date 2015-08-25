package in.sadrudd.contactanalyser.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.utils.Constants;

/**
 * Created by sjunjo on 24/08/15.
 */
public class AddContactsFragment extends ListFragment {

    public static final String ARGS_KEY = "ADD_CONTACTS_FRAGMENT";

    private OnAddContactsFragmentLoadedListener callback;

    public interface OnAddContactsFragmentLoadedListener {
        public void onAddContactsFragmentLoaded();
    }

    private String[] phoneNumbers;

    public AddContactsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumbers = getArguments().getStringArray(ARGS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_contacts, container, false);
        setListAdapter(new CheckBoxListAdapter(getActivity(), phoneNumbers));
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG, "AddContacts fragment loaded and attached");
        try {
            callback = (OnAddContactsFragmentLoadedListener) getActivity();
            callback.onAddContactsFragmentLoaded();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnMainFragmentLoadedListener");
        }
    }

}

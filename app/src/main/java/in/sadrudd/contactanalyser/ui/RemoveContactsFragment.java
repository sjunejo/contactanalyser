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
 * Created by sjunjo on 20/08/15.
 */
public class RemoveContactsFragment extends ListFragment  {

    private OnRemoveContactsFragmentLoadedListener callback;

    public interface OnRemoveContactsFragmentLoadedListener {
        public void onRemoveContactsFragmentLoaded();
    }

    public static final String ARGS_KEY = "REMOVE_CONTACTS_FRAGMENT";

    private String[] contacts;


    public RemoveContactsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contacts = getArguments().getStringArray(ARGS_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_remove_contacts, container, false);

        setListAdapter(new CheckBoxListAdapter(getActivity(), contacts));
        return v;
    }

    public void setContacts(String[] contacts){
        this.contacts = contacts;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG, "Main fragment loaded and attached");
        try {
            callback = (OnRemoveContactsFragmentLoadedListener) getActivity();
            callback.onRemoveContactsFragmentLoaded();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnMainFragmentLoadedListener");
        }
    }

}

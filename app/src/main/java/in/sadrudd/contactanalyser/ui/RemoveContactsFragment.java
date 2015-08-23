package in.sadrudd.contactanalyser.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

import in.sadrudd.contactanalyser.R;

/**
 * Created by sjunjo on 20/08/15.
 */
public class RemoveContactsFragment extends ListFragment {

    public static final String ARGS_KEY = "REMOVE_CONTACTS_FRAGMENT";

    private String[] contacts;

    // TODO I don't think I actually need this
    public static RemoveContactsFragment newInstance(Set<String> setOfContacts){
        final RemoveContactsFragment removeContactsFragment  = new RemoveContactsFragment();
        final Bundle args = new Bundle();
        args.putStringArray(ARGS_KEY, (String[]) setOfContacts.toArray());
        return removeContactsFragment;
    }

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


}

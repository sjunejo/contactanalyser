package in.sadrudd.contactanalyser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.ui.adapters.CheckBoxListAdapter;
import in.sadrudd.contactanalyser.utils.Constants;

/**
 * Created by sjunjo on 24/08/15.
 */
public class AddContactsFragment extends Fragment implements IContactFragment {

    public static final String ARGS_KEY = "ADD_CONTACTS_FRAGMENT";

    private OnAddContactsFragmentLoadedListener callback;
    private View view;
    private RecyclerView recyclerView;

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
        Log.d(Constants.TAG, "OnCreateView() called");
        view = inflater.inflate(R.layout.fragment_add_contacts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.addcontacts_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new CheckBoxListAdapter(phoneNumbers));
        return view;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callback = null;
        view = null;
    }

    @Override
    public void setData(String[] data) {
        this.phoneNumbers = data;
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public String[] getData() {
        return phoneNumbers;
    }

    @Override
    public CheckBoxListAdapter getAdapter() {
        return (CheckBoxListAdapter) recyclerView.getAdapter();
    }
}

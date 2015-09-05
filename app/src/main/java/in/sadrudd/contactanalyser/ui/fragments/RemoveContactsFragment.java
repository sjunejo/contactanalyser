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
 * Created by sjunjo on 20/08/15.
 */
public class RemoveContactsFragment extends Fragment implements IContactFragment {

    private OnRemoveContactsFragmentLoadedListener callback;

    private RecyclerView recyclerView;

    public interface OnRemoveContactsFragmentLoadedListener {
        public void onRemoveContactsFragmentLoaded();
    }

    public static final String ARGS_KEY = "REMOVE_CONTACTS_FRAGMENT";

    private View view;

    private String[] contacts;

    public RemoveContactsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args.containsKey(ARGS_KEY))
            contacts = args.getStringArray(ARGS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(Constants.TAG, "OnCreateView() called");
        view = inflater.inflate(R.layout.fragment_remove_contacts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.removecontacts_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new CheckBoxListAdapter(contacts));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG, "RemoveContacts fragment loaded and attached");
        try {
            callback = (OnRemoveContactsFragmentLoadedListener) getActivity();
            callback.onRemoveContactsFragmentLoaded();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnMainFragmentLoadedListener");
        }
    }

    @Override
    public void setData(String[] data) {
        this.contacts = data;
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public String[] getData() {
        return contacts;
    }

    @Override
    public CheckBoxListAdapter getAdapter() {
        return (CheckBoxListAdapter) recyclerView.getAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callback = null;
        view = null;
    }
}

package in.sadrudd.contactanalyser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sadrudd.contactanalyser.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class ContactAnalyserMainActivityFragment extends Fragment  {

    public interface OnMainFragmentLoadedListener {
        public void onMainFragmentLoaded();
    }

    public ContactAnalyserMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Log.d(Constants.TAG, "OnCreateView() called");
        return inflater.inflate(R.layout.fragment_decluttr_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Log.d(Constants.TAG, "Main fragment loaded and attached");
        try {
            OnMainFragmentLoadedListener callback = (OnMainFragmentLoadedListener) getActivity();
            callback.onMainFragmentLoaded();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnMainFragmentLoadedListener");
        }
    }

}


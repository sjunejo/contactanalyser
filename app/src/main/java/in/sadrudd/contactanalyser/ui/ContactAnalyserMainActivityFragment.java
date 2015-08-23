package in.sadrudd.contactanalyser.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.utils.Constants;


/**
 * A placeholder fragment containing a simple view.
 */
public class ContactAnalyserMainActivityFragment extends Fragment  {

    private OnMainFragmentLoadedListener callback;

    public interface OnMainFragmentLoadedListener {
        public void onMainFragmentLoaded();
    }
    public ContactAnalyserMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_decluttr_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG, "Fragment loaded and attached");
        try {
            callback = (OnMainFragmentLoadedListener) getActivity();
            callback.onMainFragmentLoaded();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnMainFragmentLoadedListener");
        }
    }

}


package in.sadrudd.contactanalyser.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.data.CallLogDataAccessor;
import in.sadrudd.contactanalyser.data.PhoneNumberFrequencyObject;
import in.sadrudd.contactanalyser.utils.Constants;


public class ContactAnalyserMainActivity extends AppCompatActivity implements View.OnClickListener,
        ContactAnalyserMainActivityFragment.OnMainFragmentLoadedListener,
        RemoveContactsFragment.OnRemoveContactsFragmentLoadedListener {

    private Button btnAnalyseCallLog;
    private Button btnRemoveContacts;


    private TextView tvCallLog;

    private CallLogDataAccessor callLogDataAccessor;

    private List<Fragment> fragments;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private boolean removeContactsFragmentCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        initialiseFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decluttr_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialiseFragments(){
        fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, ContactAnalyserMainActivityFragment.class.getName()));
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_analyse_call_log:
                analyseCallLogData();
                tvCallLog.setText("");
                break;
            case R.id.btn_remove_contacts:
                removeContactsButtonPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0){
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onMainFragmentLoaded() {
        btnAnalyseCallLog = (Button) findViewById(R.id.btn_analyse_call_log);
        btnAnalyseCallLog.setOnClickListener(this);
        tvCallLog = (TextView) findViewById(R.id.tv_call_log);
    }

    @Override
    public void onRemoveContactsFragmentLoaded() {
        btnRemoveContacts = (Button) findViewById(R.id.btn_remove_contacts);
        btnRemoveContacts.setOnClickListener(this);
    }

    public Button getBtnAnalyseCallLog(){
        return btnAnalyseCallLog;
    }

    private void analyseCallLogData(){
        callLogDataAccessor = new CallLogDataAccessor();
        List<PhoneNumberFrequencyObject> uniquePhoneNumbers = callLogDataAccessor.getCallLogData(this)
                .getAllUniquePhoneNumbersSortedByAscendingFrequency();
        Log.d(Constants.TAG, uniquePhoneNumbers.toString());
        prepareRemoveContactsFragment(uniquePhoneNumbers);
    }

    private void prepareRemoveContactsFragment(List<PhoneNumberFrequencyObject> uniquePhoneNumbers){
        Set<String> setOfContactsToConsiderRemoving = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        setOfContactsToConsiderRemoving.addAll(getContactsWithFewRegisteredCalls(uniquePhoneNumbers));
        setOfContactsToConsiderRemoving.addAll(getContactsWithNoRegisteredCalls(uniquePhoneNumbers));
        Log.d(Constants.TAG, setOfContactsToConsiderRemoving.toString());

        Bundle args = new Bundle();

        String[] contacts = setOfContactsToConsiderRemoving.toArray(new String[setOfContactsToConsiderRemoving.size()]);
        args.putStringArray(RemoveContactsFragment.ARGS_KEY,
                contacts);
        if (!removeContactsFragmentCreated){
            fragments.add(Fragment.instantiate(this, RemoveContactsFragment.class.getName(), args));
            pagerAdapter.notifyDataSetChanged();
            removeContactsFragmentCreated = true;
        }
         else {
            ((RemoveContactsFragment) fragments.get(Constants.FRAGMENT_REMOVE_CONTACTS)).setContacts(contacts);
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
    }


    // For now, we're just focusing on contacts with one or zero registered calls
    private int removeContactsCallThreshold = 2;

    public Set<String> getContactsWithFewRegisteredCalls(List<PhoneNumberFrequencyObject> uniquePhoneNumbers){
        Set<String> contactsWithFewRegisteredCalls = new LinkedHashSet<String>();
        for (PhoneNumberFrequencyObject phoneNumberFrequencyObject: uniquePhoneNumbers){
            // Check contact
            if (phoneNumberFrequencyObject.getFrequency() >= removeContactsCallThreshold){
                break;
            } else {
                String contactName = callLogDataAccessor.getContactName(phoneNumberFrequencyObject
                        .getPhoneNumber());
                if (!contactName.equals("")){
                    contactsWithFewRegisteredCalls.add(contactName);
                    Log.d(Constants.TAG, contactName + ": " + phoneNumberFrequencyObject.getPhoneNumber());
                }
            }
        }
        return contactsWithFewRegisteredCalls;
    }

    public Set<String> getContactsWithNoRegisteredCalls(List<PhoneNumberFrequencyObject> uniquePhoneNumbers){
        Set<String> contactsWithNoPhoneCalls = callLogDataAccessor.getContactNames(this);
        //Log.d(Constants.TAG, "Contacts with no Calls: BEFORE");
        //Log.d(Constants.TAG, contactsWithNoPhoneCalls.toString());
        Set<String> contactsWithPhoneCalls = new LinkedHashSet<String>();
        // Get phone numbers associated with ALL names..)
        for (PhoneNumberFrequencyObject phoneNumberFrequencyObject: uniquePhoneNumbers){
            String contactName = callLogDataAccessor.getContactName(phoneNumberFrequencyObject.getPhoneNumber());
            if (!contactName.equals(""))
                contactsWithPhoneCalls.add(contactName);
        }
        contactsWithNoPhoneCalls.removeAll(contactsWithPhoneCalls);
        //Log.d(Constants.TAG, "Contacts with no Calls: AFTER");
        //Log.d(Constants.TAG, contactsWithNoPhoneCalls.toString());
        return contactsWithNoPhoneCalls;
    }

    private void removeContactsButtonPressed(){
        CheckBoxListAdapter checkBoxListAdapter = (CheckBoxListAdapter) ((RemoveContactsFragment)
                fragments.get(Constants.FRAGMENT_REMOVE_CONTACTS)).
                getListAdapter();
        String[] contactsToRemove = checkBoxListAdapter.getSetOfContactsCheckedForRemoval();
        createAreYouSureDialogue(contactsToRemove);
        // ARE YOU SURE??
    }

    private void createAreYouSureDialogue(String[] contactsToRemove){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Deleting: ");
        for (String contact: contactsToRemove){
            stringBuilder.append(contact + "\n");
        }
        stringBuilder.append("\nAre you sure?");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(stringBuilder.toString()).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}

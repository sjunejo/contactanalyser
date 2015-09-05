package in.sadrudd.contactanalyser.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import in.sadrudd.contactanalyser.R;
import in.sadrudd.contactanalyser.data.CallLogDataAccessor;
import in.sadrudd.contactanalyser.data.PhoneNumberFrequencyObject;
import in.sadrudd.contactanalyser.ui.adapters.CheckBoxListAdapter;
import in.sadrudd.contactanalyser.ui.fragments.AddContactsFragment;
import in.sadrudd.contactanalyser.ui.fragments.ContactAnalyserMainActivityFragment;
import in.sadrudd.contactanalyser.ui.fragments.EnterContactNamesFragment;
import in.sadrudd.contactanalyser.ui.fragments.IContactFragment;
import in.sadrudd.contactanalyser.ui.fragments.RemoveContactsFragment;
import in.sadrudd.contactanalyser.utils.Constants;

public class ContactAnalyserMainActivity extends AppCompatActivity implements View.OnClickListener,
        ContactAnalyserMainActivityFragment.OnMainFragmentLoadedListener,
        RemoveContactsFragment.OnRemoveContactsFragmentLoadedListener,
        AddContactsFragment.OnAddContactsFragmentLoadedListener,
        EnterContactNamesFragment.EnterContactNamesFragmentListener {

    private CallLogDataAccessor callLogDataAccessor;

    private Set<String> contactsWithFewRegisteredCalls;
    private Set<String> phoneNumbersWithSubstantialRegisteredCalls;

    private String[] arrayPhoneNumbersWithSubstantialRegisteredCalls;
    private String[] arrayContactsWithFewOrNoRegisteredCalls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decluttr_main);
        initialiseMainFragment();
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

    private void initialiseMainFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment mainActivityFragment = new ContactAnalyserMainActivityFragment();
        fragmentTransaction.replace(R.id.fragment_container, mainActivityFragment, Constants.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_analyse_call_log:
                analyseCallLogData();
                break;
            case R.id.btn_remove_contacts:
                removeContactsButtonPressed();
                break;
            case R.id.btn_add_contacts:
                addContactsButtonPressed();
                break;
            case R.id.btn_remove_contacts_skip:
                prepareAddContactsFragment();
                break;
            case R.id.btn_add_contacts_skip:
                resetToMainFragment();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMainFragmentLoaded() {
        Button btnAnalyseCallLog = (Button) findViewById(R.id.btn_analyse_call_log);
        btnAnalyseCallLog.setOnClickListener(this);
    }

    @Override
    public void onRemoveContactsFragmentLoaded() {
        Button btnRemoveContacts = (Button) findViewById(R.id.btn_remove_contacts);
        btnRemoveContacts.setOnClickListener(this);
        Button btnRemoveContactsSkip = (Button) findViewById(R.id.btn_remove_contacts_skip);
        btnRemoveContactsSkip.setOnClickListener(this);
    }

    @Override
    public void onAddContactsFragmentLoaded() {
        Button btnAddContacts = (Button) findViewById(R.id.btn_add_contacts);
        btnAddContacts.setOnClickListener(this);
        Button btnAddContactsSkip = (Button) findViewById(R.id.btn_add_contacts_skip);
        btnAddContactsSkip.setOnClickListener(this);
    }

    private void removeContactsButtonPressed(){
        String[] contactsToRemove = getCheckBoxListAdapter()
                .getCheckBoxItemsChecked();
        if (contactsToRemove.length > 0){
            createAreYouSureDialogue(contactsToRemove);
        } else {
            Toast.makeText(this, "You must select at least one contact to remove.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onContactNamesEntered(String[] contactNames, String[] phoneNumbers) {
        callLogDataAccessor.addContacts(this, contactNames, phoneNumbers);
        resetToMainFragment();
    }

    private void resetToMainFragment(){
        prepareFragment(null, "", ContactAnalyserMainActivityFragment.class.getName(), 0, true);
    }

    private void addContactsButtonPressed(){
        String[] phoneNumbersToAdd = getCheckBoxListAdapter()
                .getCheckBoxItemsChecked();
        if (phoneNumbersToAdd.length > 0){
            prepareFragment(phoneNumbersToAdd, EnterContactNamesFragment.ARGS_KEY,
                    EnterContactNamesFragment.class.getName(),
                    Constants.FRAGMENT_ENTER_CONTACTS, true);
            Log.d(Constants.TAG, Arrays.toString(phoneNumbersToAdd));
        } else {
            Toast.makeText(this, "You must select at least one phone number to add as a contact.",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void prepareDataSetsAndRemoveContactsFragment(List<PhoneNumberFrequencyObject> uniquePhoneNumbers){
            Set<String> setOfContactsToConsiderRemoving = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            setOfContactsToConsiderRemoving.addAll(contactsWithFewRegisteredCalls);
            setOfContactsToConsiderRemoving.addAll(getContactsWithNoRegisteredCalls(uniquePhoneNumbers));
            Log.d(Constants.TAG, setOfContactsToConsiderRemoving.toString());
            arrayPhoneNumbersWithSubstantialRegisteredCalls = setOfContactsToConsiderRemoving.toArray(
                    new String[setOfContactsToConsiderRemoving.size()]);

        prepareFragment(arrayPhoneNumbersWithSubstantialRegisteredCalls, RemoveContactsFragment.ARGS_KEY, RemoveContactsFragment.class.getName(),
                Constants.FRAGMENT_REMOVE_CONTACTS, true);
    }

    private void prepareAddContactsFragment(){
        arrayContactsWithFewOrNoRegisteredCalls = phoneNumbersWithSubstantialRegisteredCalls.toArray(
                    new String[phoneNumbersWithSubstantialRegisteredCalls.size()]);
        prepareFragment(arrayContactsWithFewOrNoRegisteredCalls, AddContactsFragment.ARGS_KEY, AddContactsFragment.class.getName(),
                Constants.FRAGMENT_ADD_CONTACTS, true);
    }

    private void prepareFragment(String[] initialArgs, String argumentKey, String nameOfFragment,
                                 int fragmentID, boolean animate){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (animate) fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        Fragment nextFragment;
        if (initialArgs != null) {
            Bundle bundle = new Bundle();
            bundle.putStringArray(argumentKey, initialArgs);
            nextFragment = Fragment.instantiate(this, nameOfFragment, bundle);
        }
        else {
            nextFragment = Fragment.instantiate(this, nameOfFragment);
        }
        nextFragment.setRetainInstance(true);
        fragmentTransaction.replace(R.id.fragment_container, nextFragment, Constants.TAG);
        fragmentTransaction.commit();
    }

    private void analyseCallLogData(){
        callLogDataAccessor = new CallLogDataAccessor();
        List<PhoneNumberFrequencyObject> uniquePhoneNumbers = callLogDataAccessor.getCallLogData(this)
                .getAllUniquePhoneNumbersSortedByAscendingFrequency();
        Log.d(Constants.TAG, uniquePhoneNumbers.toString());
        partitionCallLogData(uniquePhoneNumbers);
        prepareDataSetsAndRemoveContactsFragment(uniquePhoneNumbers);
    }

    // For now, we're just focusing on contacts with one or zero registered calls
    private int removeContactsCallThreshold = 2;

    private Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentByTag(Constants.TAG);
    }

    /**
     * One loop through PhoneNumber/Frequency pairs should be enough to determine
     * which numbers are candidates for removal from the Contacts list,
     * and which numbers are candidates for addition to the Contacts list.
     * @param uniquePhoneNumbers phoneNumber/Frequency pairs
     */
    private void partitionCallLogData(List<PhoneNumberFrequencyObject> uniquePhoneNumbers){
        contactsWithFewRegisteredCalls = new LinkedHashSet<String>();
        phoneNumbersWithSubstantialRegisteredCalls = new LinkedHashSet<String>();
        int count;
        for (count = 0; count < uniquePhoneNumbers.size(); count++){
            PhoneNumberFrequencyObject phoneNumberFrequencyObject = uniquePhoneNumbers.get(count);
            // Check contact
            if (phoneNumberFrequencyObject.getFrequency() >= removeContactsCallThreshold){
                break;
            } else {
                String contactName = callLogDataAccessor.getContactName(phoneNumberFrequencyObject
                        .getPhoneNumber());
                if (!contactName.equals("")){
                    contactsWithFewRegisteredCalls.add(contactName);
                    //Log.d(Constants.TAG, contactName + ": " + phoneNumberFrequencyObject.getPhoneNumber());
                }
            }
        }
        for (int i = count; i < uniquePhoneNumbers.size(); i++){
            PhoneNumberFrequencyObject phoneNumberFrequencyObject = uniquePhoneNumbers.get(i);
            String contactName = callLogDataAccessor.getContactName(phoneNumberFrequencyObject
                    .getPhoneNumber());
            if (contactName.equals("")){
                phoneNumbersWithSubstantialRegisteredCalls.add(phoneNumberFrequencyObject.getPhoneNumber());
                Log.d(Constants.TAG, "Phone Number: " + phoneNumberFrequencyObject.getPhoneNumber()
                + " | Frequency: " + phoneNumberFrequencyObject.getFrequency());
            }
        }
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

    public CheckBoxListAdapter getCheckBoxListAdapter(){
        return  ((IContactFragment)
                getCurrentFragment()).getAdapter();
    }

    private void createAreYouSureDialogue(final String[] contactsToRemove){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Deleting:\n\n");
        for (String contact: contactsToRemove){
            stringBuilder.append(contact + "\n");
        }
        stringBuilder.append("\n\nAre you sure?");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        callLogDataAccessor.deleteSelectedContacts(ContactAnalyserMainActivity.this,
                                contactsToRemove);
                        // Delete fragment and move on to next
                        prepareAddContactsFragment();
                        //getCheckBoxListAdapter(Constants.FRAGMENT_REMOVE_CONTACTS).removeFromListView(contactsToRemove);
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

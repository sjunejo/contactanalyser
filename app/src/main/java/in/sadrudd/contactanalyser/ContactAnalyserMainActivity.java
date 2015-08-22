package in.sadrudd.contactanalyser;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import in.sadrudd.contactanalyser.data.CallLogDataAccessor;
import in.sadrudd.contactanalyser.data.PhoneNumberFrequencyObject;
import in.sadrudd.contactanalyser.utils.Constants;


public class ContactAnalyserMainActivity extends AppCompatActivity implements View.OnClickListener{

    private Fragment deCluttrMainFragment;

    private Button btnAnalyseCallLog;
    private TextView tvCallLog;

    private CallLogDataAccessor callLogDataAccessor;

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
        deCluttrMainFragment = getFragmentManager().findFragmentById(R.id.fragment_decluttr_main);
        btnAnalyseCallLog = (Button) findViewById(R.id.btn_analyse_call_log);
        btnAnalyseCallLog.setOnClickListener(this);
        tvCallLog = (TextView) findViewById(R.id.tv_call_log);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_analyse_call_log:
                analyseCallLogData();
                tvCallLog.setText("");
                break;
        }
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

    // For now, we're just focusing on contacts with one or zero registered calls
    private int removeContactsCallThreshold = 1;

    private void prepareRemoveContactsFragment(List<PhoneNumberFrequencyObject> uniquePhoneNumbers){
        Set<String> setOfContacts = new LinkedHashSet<String>();
        setOfContacts.addAll(getContactsWithFewRegisteredCalls(uniquePhoneNumbers));
        setOfContacts.addAll(getContactsWithNoRegisteredCalls(uniquePhoneNumbers));

        // TODO display set as list
    }

    public Set<String> getContactsWithFewRegisteredCalls(List<PhoneNumberFrequencyObject> uniquePhoneNumbers){
        Set<String> contactsWithFewRegisteredCalls = new LinkedHashSet<String>();
        for (PhoneNumberFrequencyObject phoneNumberFrequencyObject: uniquePhoneNumbers){
            // Check contact
            if (phoneNumberFrequencyObject.getFrequency() >= 2){
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
        Log.d(Constants.TAG, "Contacts with no Calls: BEFORE");
        Log.d(Constants.TAG, contactsWithNoPhoneCalls.toString());
        Set<String> contactsWithPhoneCalls = new LinkedHashSet<String>();
        // Get phone numbers associated with ALL names..)
        for (PhoneNumberFrequencyObject phoneNumberFrequencyObject: uniquePhoneNumbers){
            String contactName = callLogDataAccessor.getContactName(phoneNumberFrequencyObject.getPhoneNumber());
            if (!contactName.equals(""))
                contactsWithPhoneCalls.add(contactName);
        }
        contactsWithNoPhoneCalls.removeAll(contactsWithPhoneCalls);
        Log.d(Constants.TAG, "Contacts with no Calls: AFTER");
        Log.d(Constants.TAG, contactsWithNoPhoneCalls.toString());
        return contactsWithNoPhoneCalls;
    }


}

package in.sadrudd.contactanalyser.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;

import in.sadrudd.contactanalyser.utils.Constants;

/**
 * Created by sjunjo on 30/07/15.
 */
public class CallLogDataAccessor {

    private CallLogDataContainer callLogDataContainer;

    private int indexPhoneNumber;
    private int indexCachedName;
    private int indexCallType;

    private static final String HEADING_NUMBER = CallLog.Calls.CACHED_NORMALIZED_NUMBER;
    private static final String HEADING_NAME = CallLog.Calls.CACHED_NAME;
    private static final String HEADING_TYPE = CallLog.Calls.TYPE;

    private String[] columnsFromCallLogToReturn = {
            HEADING_NUMBER,
            HEADING_NAME,
            HEADING_TYPE
    };

    private String[] contactDataProjection = {ContactsContract.Contacts.DISPLAY_NAME };

    public CallLogDataAccessor(){
        callLogDataContainer = new CallLogDataContainer();
    }

    public String[] getColumnsFromCallLogToReturn(){
        return columnsFromCallLogToReturn;
    }

    /**
     * Gets ALL call log data from appropriate content provider
     * and places it into the Call Log Data Container object.
     * @param context the context (of the activity, not application!)
     * @return
     */
    public CallLogDataContainer getCallLogData(Context context){
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
                null);
        cursor.moveToFirst();
        int[] columnIndexes = new int[columnsFromCallLogToReturn.length];

        // Get indices
        indexPhoneNumber = cursor.getColumnIndex(HEADING_NUMBER);
        indexCachedName = cursor.getColumnIndex(HEADING_NAME);
        indexCallType = cursor.getColumnIndex(HEADING_TYPE);

        while (cursor.moveToNext()){
            // If it's a missed call, don't add it to the call log.
            // Also, Unknown numbers shouldn't be analysed
            int callType = cursor.getInt(indexCallType);
            if (callType != CallLog.Calls.MISSED_TYPE && !cursor.isNull(indexPhoneNumber)){
                CallLogDataObject callLogDataObject = new CallLogDataObject();
                callLogDataObject.setPhoneNumber(cursor.getString(indexPhoneNumber));
                if (!cursor.isNull(indexCachedName))
                    callLogDataObject.setContactName(cursor.getString(indexCachedName));
                else
                    callLogDataObject.setContactName("");
                callLogDataObject.setCallType(callType);

                // Log.d(Constants.TAG, "PHONE NUMBER AND TYPE: " + callLogDataObject.getPhoneNumber() + " ---- " + callType);
                callLogDataContainer.put(callLogDataObject);
            }
        }
        cursor.close();
        return callLogDataContainer;
    }

    // TODO Add exception to throw
    public String getContactName(String phoneNumber){
        return callLogDataContainer.getContactForPhoneNumber(phoneNumber);
    }

    public Set<String> getContactNames(Context context){
        Set<String> setOfContactNames = new LinkedHashSet<String>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts.HAS_PHONE_NUMBER, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            setOfContactNames.add(cursor.getString(0));
        }
        cursor.close();
        return setOfContactNames;
    }

    public void deleteSelectedContacts(Context context, String[] contacts){
        String strQuery = ContactsContract.Contacts.DISPLAY_NAME + " IN ("
                + makePlaceholders(contacts.length) + ")";
        Log.d(Constants.TAG, "" + strQuery);
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.LOOKUP_KEY},
                strQuery, contacts, null);
        cursor.moveToFirst();
        Log.d(Constants.TAG, "Query carried out: " + cursor.getCount());
        do { // TODO get rid of magic numbers here
            String contact = cursor.getString(0);
            Log.d(Constants.TAG, "DP: " + contact);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, cursor.getString(1));
            context.getContentResolver().delete(uri, null, null);
        } while (cursor.moveToNext());
        cursor.close();
    }

    private String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    public void addContacts(Context context, String[] contactNames, String[] phoneNumbers){
        // Getting raw contact ID TODO find more efficient means of doing this!
        Cursor c = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,
                null, null ,null, null);
        long rawContactId = 0;
        if (c.moveToLast()) {
            rawContactId=c.getLong(c.getColumnIndex(ContactsContract.RawContacts._ID));
        }
        c.close();

        for (int i = 0; i < contactNames.length; i++){
            ContentValues values = new ContentValues();
            values.put(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, ++rawContactId);
            values.put(ContactsContract.CommonDataKinds.Phone.LABEL, contactNames[i]);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumbers[i]);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
            values.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            Uri dataUri = context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

        }
    }
}

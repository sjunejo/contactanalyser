package in.sadrudd.contactanalyser.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.util.LinkedHashSet;
import java.util.Set;

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
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null);
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            setOfContactNames.add(cursor.getString(0));
        }
        cursor.close();
        return setOfContactNames;
    }

}

package in.sadrudd.contactanalyser.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

/**
 * Created by sjunjo on 30/07/15.
 */
public class CallLogDataAccessor {

    private CallLogDataContainer callLogDataContainer;

    private int indexPhoneNumber;
    private int indexCachedName;
    private int indexCallType;

    private String[] columnsFromCallLogToReturn = {
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE
    };

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
        indexPhoneNumber = cursor.getColumnIndex(CallLog.Calls.CACHED_FORMATTED_NUMBER);
        indexCachedName = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        indexCallType = cursor.getColumnIndex(CallLog.Calls.TYPE);
        while (cursor.moveToNext()){
            // If it's a missed call, don't add it to the call log.
            // Also, Unknown numbers shouldn't be analysed
            int callType = cursor.getInt(indexCallType);
            if (indexCallType != CallLog.Calls.MISSED_TYPE && !cursor.isNull(indexPhoneNumber)){
                CallLogDataObject callLogDataObject = new CallLogDataObject();
                callLogDataObject.setPhoneNumber(cursor.getString(indexPhoneNumber));
                if (!cursor.isNull(indexCachedName))
                    callLogDataObject.setContactName(cursor.getString(indexCachedName));
                else
                    callLogDataObject.setContactName("");
                callLogDataObject.setCallType(callType);
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



}

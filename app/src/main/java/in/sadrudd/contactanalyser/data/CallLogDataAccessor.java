package in.sadrudd.contactanalyser.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

/**
 * Created by sjunjo on 30/07/15.
 */
public class CallLogDataAccessor {

    private CallLogData callLogData;

    private int indexPhoneNumber;
    private int indexCachedName;
    private int indexCallType;

    private String[] columnsFromCallLogToReturn = {
            CallLog.Calls.CACHED_FORMATTED_NUMBER,

            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE
    };

    public CallLogDataAccessor(){
        callLogData = new CallLogData();
    }

    public CallLogData getCallLogData(Context context){
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
            int callType = cursor.getInt(indexCallType);
            if (indexCallType != CallLog.Calls.MISSED_TYPE){
                CallLogDataObject callLogDataObject = new CallLogDataObject();
                callLogDataObject.setPhoneNumber(cursor.getString(indexPhoneNumber));
                callLogDataObject.setContactName(cursor.getString(indexCachedName));
                callLogDataObject.setCallType(callType);
                callLogData.put(callLogDataObject);
            }
        }
        cursor.close();
        return callLogData;
    }



}

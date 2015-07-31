package in.sadrudd.contactanalyser.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

/**
 * Created by sjunjo on 30/07/15.
 */
public class CallLogDataAccessor {

    private String[] columnsFromCallLogToReturn = {
            CallLog.Calls.CACHED_FORMATTED_NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE
    };


    public void getCallLog(Context context){
        Cursor cursor = context.getContentResolver().query(CallLog.CONTENT_URI, null, null, null,
                null);
        cursor.moveToFirst();
        int[] columnIndexes = new int[columnsFromCallLogToReturn.length];
        for (int i = 0; i < columnsFromCallLogToReturn.length; i++){
            columnIndexes[i] = cursor.getColumnIndexOrThrow(columnsFromCallLogToReturn[i]);
        }
        while (cursor.moveToNext()){

        }
        cursor.close();
    }


}

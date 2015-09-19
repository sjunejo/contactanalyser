package in.sadrudd.contactanalyser.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjunjo on 19/09/15.
 */
public class SMSDataAccessor {

    private static final String SMS_URI = "content://sms";
    private static final String SMS_INBOX = "1";
    private static final String SMS_PHONE_NUMBER = "address";
    private static final String SMS_TYPE = "type";

    public List<SMSDataObject> getSMSMessages(Context context){
        List<SMSDataObject> smsDataObjects = new ArrayList<SMSDataObject>();

        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI), null, null, null, null);
        if (cursor.moveToFirst()){
            int indexPhoneNumber = cursor.getColumnIndexOrThrow(SMS_PHONE_NUMBER);
            int indexType = cursor.getColumnIndexOrThrow(SMS_TYPE);

            while (cursor.moveToNext()){
                String phoneNumber = cursor.getString(indexPhoneNumber);
                boolean type;
                if (cursor.getString(indexType).equals(SMS_INBOX)){
                    type = SMSDataObject.INBOX;
                }
                else {
                    type = SMSDataObject.SENT;
                }
                smsDataObjects.add(new SMSDataObject(phoneNumber, type));
            }
        }
        return smsDataObjects;
    }
}

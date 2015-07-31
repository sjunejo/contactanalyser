package in.sadrudd.contactanalyser.data;

/**
 * Created by sjunjo on 31/07/15.
 */
public class CallLogDataObject {

    private String phoneNumber;
    private int callType;
    private String contactName;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}

package in.sadrudd.contactanalyser.data;

/**
 * Created by sjunjo on 19/09/15.
 */
public class SMSDataObject {


    public static boolean INBOX = false;
    public static boolean SENT = true;

    private String contactName;
    private String phoneNumber;

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    private boolean folder;

    public SMSDataObject(){

    }

    public SMSDataObject(String phoneNumber,  boolean folder){
        this.phoneNumber = phoneNumber;
        this.folder = folder;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

}

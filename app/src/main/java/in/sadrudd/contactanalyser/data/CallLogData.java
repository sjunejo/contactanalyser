package in.sadrudd.contactanalyser.data;

import java.util.HashMap;

/**
 * Created by sjunjo on 31/07/15.
 */
public class CallLogData {
    private HashMap<String, Integer> frequencyOfNumbers;


    public CallLogData(){
        frequencyOfNumbers = new HashMap<String, Integer>();
    }

    public void put(CallLogDataObject callLogDataObject){
        String phoneNumber = callLogDataObject.getPhoneNumber();
        frequencyOfNumbers.put(phoneNumber, frequencyOfNumbers.get(phoneNumber));
    }

}

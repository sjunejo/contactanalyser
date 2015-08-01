package in.sadrudd.contactanalyser.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sjunjo on 31/07/15.
 */
public class CallLogData {
    private HashMap<String, Integer> frequencyOfNumbers;
    private HashMap<String, CallLogDataObject> callLogData;


    public CallLogData(){
        callLogData = new HashMap<String, CallLogDataObject>();
        frequencyOfNumbers = new HashMap<String, Integer>();
    }

    public void put(CallLogDataObject callLogDataObject){
        String phoneNumber = callLogDataObject.getPhoneNumber();
        callLogData.put(phoneNumber, callLogDataObject);
        if (!frequencyOfNumbers.containsKey(phoneNumber))
            frequencyOfNumbers.put(phoneNumber, 1);
        else
            frequencyOfNumbers.put(phoneNumber, frequencyOfNumbers.get(phoneNumber)+1);
    }


    /**
     * Iterate through HashMap 'frequency of numbers' to produce a list of phone numbers
     * sorted in descending order, in terms of the sum of incoming/outgoing calls.
     */
    public List<PhoneNumberFrequencyObject> getAllUniquePhoneNumbersSortedByDescendingFrequency(){
        List<PhoneNumberFrequencyObject> frequenciesAndNumbers =
                new ArrayList<PhoneNumberFrequencyObject>();
        // Turn frequency of numbers into something sortable by the frequencies themselves!
        // (cannot be accomplished easily with a hashmap)
        Iterator it = frequencyOfNumbers.keySet().iterator();
        while (it.hasNext()){
            String phoneNumber = (String) it.next();
            frequenciesAndNumbers.add(new PhoneNumberFrequencyObject(phoneNumber,
                    frequencyOfNumbers.get(phoneNumber)));
        }
        Collections.sort(frequenciesAndNumbers);
        return frequenciesAndNumbers;
    }


}

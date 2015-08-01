package in.sadrudd.contactanalyser.data;

import java.util.HashMap;
import java.util.TreeMap;

import in.sadrudd.contactanalyser.utils.CallsComparator;

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
    public TreeMap<String, Integer> getAllUniquePhoneNumbersSortedByDescendingFrequency(){
        CallsComparator comparator = new CallsComparator(frequencyOfNumbers);
        TreeMap sortedMap = new TreeMap(comparator);
        sortedMap.putAll(frequencyOfNumbers);
        return sortedMap;
    }


}

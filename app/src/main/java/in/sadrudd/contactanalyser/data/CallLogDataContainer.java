package in.sadrudd.contactanalyser.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sjunjo on 31/07/15.
 */
public class CallLogDataContainer {

    /* Contains a list of unique phone numbers
     * and the number of incoming/outgoing calls
     * associated with them.
     */
    private HashMap<String, Integer> frequencyOfNumbers;

    // Contains the contacts (if they exist) associated with every phone number
    private HashMap<String, String> contactsHashMap;

    public CallLogDataContainer(){
        contactsHashMap = new HashMap<String, String>();
        frequencyOfNumbers = new HashMap<String, Integer>();
    }

    public void put(CallLogDataObject callLogDataObject){
        String phoneNumber = callLogDataObject.getPhoneNumber();
        contactsHashMap.put(phoneNumber, callLogDataObject.getContactName());
        if (!frequencyOfNumbers.containsKey(phoneNumber))
            frequencyOfNumbers.put(phoneNumber, 1);
        else
            frequencyOfNumbers.put(phoneNumber, frequencyOfNumbers.get(phoneNumber)+1);
    }


    /**
     * Iterate through HashMap 'frequency of numbers' to produce a list of phone numbers
     * sorted in descending order, in terms of the sum of incoming/outgoing calls.
     **/
    public List<PhoneNumberFrequencyObject> getAllUniquePhoneNumbersSortedByAscendingFrequency(){
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

    @Override
    public String toString() {
        // Can iterate through either of the two hashmaps...
        Iterator it = contactsHashMap.keySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();

        while (it.hasNext()){
            String phoneNumber = (String) it.next();
            stringBuilder.append(phoneNumber + "; " + contactsHashMap.get(phoneNumber) + "; " +
                    frequencyOfNumbers.get(phoneNumber) + "\n");
        }
        return stringBuilder.toString();
    }

    public String getContactForPhoneNumber(String phoneNumber){
        return contactsHashMap.get(phoneNumber);
    }
}

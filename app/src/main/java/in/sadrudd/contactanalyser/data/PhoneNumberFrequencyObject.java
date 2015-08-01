package in.sadrudd.contactanalyser.data;

/**
 * Created by sjunjo on 01/08/15.
 */
public class PhoneNumberFrequencyObject implements Comparable {

    private int frequency;
    private String phoneNumber;

    public PhoneNumberFrequencyObject(String phoneNumber, int frequency){
        setPhoneNumber(phoneNumber);
        setFrequency(frequency);

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getFrequency() {
        return frequency;
    }

    private void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(Object anotherObject) {
        int otherPhoneNumbersFrequency = ((PhoneNumberFrequencyObject) anotherObject).getFrequency();
        return otherPhoneNumbersFrequency - this.frequency; // Descending order!
    }

    @Override
    public String toString() {
        return this.phoneNumber + " | " + this.frequency;
    }
}

package in.sadrudd.contactanalyser.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by sjunjo on 01/08/15.
 */
public class CallsComparator implements Comparator<Object>{

    Map mapToSort;

    public CallsComparator(Map mapToSort){
        this.mapToSort = mapToSort;
    }

    public int compare(Object key1, Object key2) {
        Integer val1 = (Integer) mapToSort.get(key1);
        Integer val2 = (Integer) mapToSort.get(key2);
        if (val1 < val2) { // Remember...we're sorting in DESCENDING order.
            return 1;
        } else {
            return -1;
        }
    }
}

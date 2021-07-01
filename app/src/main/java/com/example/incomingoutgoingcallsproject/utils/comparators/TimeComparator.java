package com.example.incomingoutgoingcallsproject.utils.comparators;

import com.example.incomingoutgoingcallsproject.entity.PhoneRecord;

import java.time.Duration;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TimeComparator {
    public static Map<String, Duration> sort(Map<String, Duration> unsortedMap, final boolean order)
    {
        List<Map.Entry<String, Duration>> list = new LinkedList<Map.Entry<String, Duration>>(unsortedMap.entrySet());

        // Sorting the list based on values
        list.sort(new Comparator<Map.Entry<String, Duration>>() {
            public int compare(Map.Entry<String, Duration> o1,
                               Map.Entry<String, Duration> o2) {
                if (order) {
                    return Long.compare(o1.getValue().toMillis(), o2.getValue().toMillis());
                } else {
                    return Long.compare(o2.getValue().toMillis(), o1.getValue().toMillis());

                }
            }
        });
        // Maintaining insertion order with the help of LinkedList
        Map<String, Duration> sortedMap = new LinkedHashMap<String, Duration>();
        for (Map.Entry<String, Duration> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}

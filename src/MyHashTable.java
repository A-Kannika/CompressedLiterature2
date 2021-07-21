import java.util.*;
import java.util.Map;

/**
 * Create by Kannika Armstrong
 * TCSS342(Spring 2021): May 22, 2021
 * Assignment2 : Compressed Literature2 (MyHashTable class)
 * Professor. Christopher Paul Marriott
 */
public class MyHashTable <Key,Value> {
    private int capacity; // the number of buckets in the hash table
    private int size; //the number of entries stored in the hash table
    private ArrayList<Node> buckets; //the list of buckets for storing entries of the hash table
    private int myTotalProb; //the total probes for all entries.
    private int myMaxProb; //the maximum probe length for any entry.
    private Map<Integer, Integer> myHistogramMap = new TreeMap<>(); //A histogram of probes shows how many keys are found after a certain number of probes.


    public MyHashTable(int capacity) {
        this.capacity = capacity;
        size = 0;
        buckets = new ArrayList<>(capacity);

        //Create empty chains
        for (int i = 0; i < capacity; i++){
            buckets.add(null);
        }
    }


    /**
     * hash - returns the bucket ID associated with this key
     * use the built in hashcode() method.
     */
    private int hash(Key key)
    {
        return Math.abs(key.hashCode()) % capacity;
    }

    /**
     * get - returns the value associated with the key
     * Use linear probing to handle collisions.
     */
    public Value get(Key key) {
        int h = hash(key);
        while (buckets.get(h) != null) {
            if (buckets.get(h).key.equals(key)) {
                return buckets.get(h).value;
            }
            if (h == buckets.size() - 1) {
                h = 0;
            } else {
                h++;
            }
        }
        return null;
    }

    /**
     * put - overwrites the value associated with the key with a new value
     * Use linear probing to handle collisions.
     */
    public void put(Key key, Value value) {
        int h = hash(key);
        int probesCount = 1;
        while (buckets.get(h) != null) {
            if (buckets.get(h).key.equals(key)) {
                buckets.set(h, new Node(key, value));
                return;
            }
            // the last index in the array, move to 0.
            if (h == buckets.size() - 1) {
                h = 0;
            } else {
                h++;
            }
            probesCount++;
        }
        if (myHistogramMap.get(probesCount) == null) {
            myHistogramMap.put(probesCount, 1);
        } else {
            myHistogramMap.put(probesCount, myHistogramMap.get(probesCount) + 1);
        }
        buckets.set(h, new Node(key, value));
        size++;
        myTotalProb += probesCount;

        if (probesCount > myMaxProb) {
            myMaxProb = probesCount;
        }
    }

    /**
     * outputs the hash table stats
     */

    public void stats(){
        int max = 0;
        for (int i: myHistogramMap.values())
            if (i > max) {
                max = i;
            }
        System.out.println();
        System.out.println("====================== Hash Table Stats =====================\n");


        //output the size of the hash table.
        System.out.println("Number of Entries (size): " + size);

        //output the capacity of the hash table.
        System.out.println("Number of Buckets (capacity): " + capacity);

        //output the total probes for all entries.
        System.out.println("Total Probes: " + myTotalProb);

        //output the maximum probe length for any entry.
        System.out.println("Max Linear Prob: " + myMaxProb);

        //output a histogram of probes.  A histogram of probes shows how many keys are found after
        //a certain number of probes.
        System.out.println("Histogram of Probes: " + getHistogram());

    }

    /**
     * A histogram of probes shows how many keys are found after a certain number of probes.
     */
    public String getHistogram() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 1; i <= myMaxProb; i++) {
            String s;
            if (!myHistogramMap.containsKey(i)) {
                s = "0, ";
            } else {
                s = myHistogramMap.get(i) + ", ";
            }
            if (i == myMaxProb) {
                s = s.substring(0, s.length() - 2) + "]";
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public ArrayList<String> keySet() {
        ArrayList<String> keyList = new ArrayList<>();
        for (Node n: buckets)
            if (n != null) {
                keyList.add(n.key.toString());
            }
        return keyList;
    }

    /**
     * Node class of MyHashTable
     */
    private class Node{
        public Key key;
        public Value value;
        public Node(Key key, Value value){
            this.key = key;
            this.value = value;
        }
        public String toString() {
            return "The value at " + key + " key is " + value + ".";
        }
    }

    public void testHash(Key key){
        System.out.print(hash(key) + " ");
    }
}
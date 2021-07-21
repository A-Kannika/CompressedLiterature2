/**
 * Create by Kannika Armstrong
 * TCSS342(Spring 2021): May 29, 2021
 * Assignment2 : Compressed Literature2 (Main class)
 * Professor. Christopher Paul Marriott
 */
public class Main {
    public static void main(String[] args) {
        RunEncode();

        //uncomment to test MyHashTable()
        //TestMyHashTable();
    }

    /**
     * use to run the encoder class
     */
    public static void RunEncode(){
        System.out.println();
        System.out.println("===================== Encoder Processing ====================");
        Encoder e = new Encoder();
        e.encoderTest();
        System.out.println();
    }

    /**
     * use to test MyHashTable
     */
    public static void TestMyHashTable(){
        System.out.println("================= Test MyHashTable Class =================\n");
        MyHashTable<Integer, String> table = new MyHashTable<>(23);
        System.out.println(">>>>> Test hash(), put() and get() <<<<<\n");
        System.out.println("Test data set =   [3 27 51 30 77 147 11 196 221 176 40 18 66 251 148 8 80 100]\n");
        System.out.println("Expected key list: 3 4 5 7 8 9 11 12 14 15 17 18 20 21 10 8 11 8");
        System.out.print("Actual key list:   " );
        Integer[] list = new Integer[] {3, 27, 51, 30, 77, 147, 11, 196, 221, 176, 40, 18, 66, 251, 148, 8, 80, 100};
        for (int i = 0 ; i < list.length; i++){
            table.testHash(list[i]);
        }

        table.put(3, "3");
        table.put(27, "27");
        table.put(51, "51");
        table.put(30, "30");
        table.put(77, "77");
        table.put(147, "147");
        table.put(11, "11");
        table.put(196, "196");
        table.put(221, "221");
        table.put(176, "176");
        table.put(40, "40");
        table.put(18, "18");
        table.put(66, "66");
        table.put(251, "251");
        table.put(148, "148");
        table.put(8, "8");
        table.put(80, "80");
        table.put(100, "100");

        System.out.println();
        table.stats();
    }
}

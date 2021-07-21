import java.io.*;
import java.util.Arrays;

/**
 * Create by Kannika Armstrong
 * TCSS342(Spring 2021): May 29, 2021
 * Assignment2 : Compressed Literature2 (Encoder class)
 * Professor. Christopher Paul Marriott
 */

public class Encoder{

    private String inputFileName = "WarAndPeace.txt"; // final filename for the uncompressed input file
    private String outputFileName = "WarAndPeace_compression.txt"; // filename for the compressed output file
    private String codesFileName = "WarAndPeace_codes.txt"; // filename for the codes output file

//    private String inputFileName = "Text2Test.txt"; // final filename for the uncompressed input file
//    private String outputFileName = "test_compression.txt"; // filename for the compressed output file
//    private String codesFileName = "test_codes.txt"; // filename for the codes output file

    private int capacity = 32768; //number of buckets
    private String text; // the text loaded in from the input file
    private MyHashTable<String, Integer> frequencies = new MyHashTable<>(capacity); //the frequency of each word or separator in the input file
    private HuffmanNode huffmanTree; // root of HuffMan Tree
    private MyHashTable<String, String> codes = new MyHashTable<>(capacity); ///the codes assigned to each  word or separator by the Huffman algorithm
    private byte[] encodedText; //the encoded binary string

    /**
     * Using BufferedReader to read the input and loaded into text
     */
    private void readInputFile() {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(inputFileName))) {
            int singleCharInt;
            StringBuilder s = new StringBuilder();
            while ((singleCharInt = bufferedInputStream.read()) != -1) {
                s.append((char) singleCharInt);
            }
            bufferedInputStream.close();
            text = s.toString();
        } catch (Exception e) {
            System.out.println("could not find the file!!");
        }
    }

    /**
     * Count the frequency of each character in the input file, using HashMap
     */
    private void countFrequency() {
//        readInputFile();
        int x = 0;
        int y = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // if c is a separator
            if ((c < '0' || c > '9') && c != '\'' && (c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
                String separator = String.valueOf(c);
                if (frequencies.get(separator) == null) {
                    frequencies.put(separator, 1);
                } else {
                    frequencies.put(separator, frequencies.get(separator) + 1);
                }
                y = i;
                if (x != y) {
                    String word = text.substring(x, y);
                    if (frequencies.get(word) == null) {
                        frequencies.put(word, 1);
                    } else {
                        frequencies.put(word, frequencies.get(word) + 1);
                    }
                }
                x = i + 1;
            }
        }
    }

    private void buildTree() {
//        countFrequency();
        MyPriorityQueue<HuffmanNode> pq = new MyPriorityQueue<>();
        for (String s: frequencies.keySet()) {
            pq.offer(new HuffmanNode(frequencies.get(s), s));
        }
        // Merging two nodes with the least weights until one node remains.
        while (pq.size() >= 2) {
            pq.offer(new HuffmanNode(pq.poll(), pq.poll()));
        }
        huffmanTree = pq.poll();
 //       assignCodes(huffmanTree, "");
    }

    private void assignCodes(HuffmanNode root, String code) {
        // assignsHuffman codes to each character in codes
        if (root.left != null)
            assignCodes(root.left, code + "0");
        if (root.right != null)
            assignCodes(root.right, code + "1");

        // When a leaf is reached the code is stored in the codes map.
        if (root.left == null && root.right == null)
            codes.put(root.symbol, code);
    }

    private void encode() {
 //       buildTree();
        StringBuilder sb = new StringBuilder();

        int x = 0;
        int y = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // if c is a separator
            if ((c < '0' || c > '9') && c != '\'' && (c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
                String separator = String.valueOf(c);
                sb.append(codes.get(separator));
                y = i;
                if (x != y) {
                    String word = text.substring(x, y);
                    sb.append(codes.get(word));
                }
                x = i + 1;
            }
        }
        String s = sb.toString();
        // Splitting the string every 8 bits.
        String[] array = new String[s.length() / 8 + 1];
        int last = s.length() % 8;
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = s.substring(8 * i, 8 * i + 8);
        }
        array[array.length - 1] = s.substring(8 * (array.length - 1), 8 * (array.length - 1) + last);
        encodedText = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            encodedText[i] = (byte) Integer.parseInt(array[i], 2);
        }
    }

    private void writeOutputFile() {
//        encode();
        // writes the contents of encodedText to the outputFileName.
        File outputCompressedFile = new File(outputFileName);
        try {
            FileOutputStream compressedFile = new FileOutputStream(outputCompressedFile);
            compressedFile.write(encodedText);
            compressedFile.close();
        } catch (Exception e) {
            System.out.println("could not find the file to compressed!!");

        }
        // print out the original file size
        String fileName1 = "WarAndPeace.txt";
        File f = new File(fileName1);
        long fileSize1 = f.length();
        System.out.println("\nUncompressed file size: "
                + String.format("%.2f", (double) fileSize1 / 1024) + " KB (" + fileSize1 + " bytes)");
        // print out the compression file size
        String fileName2 = "WarAndPeace_compression.txt";
        f = new File(fileName2);
        long fileSize2 = f.length();
        System.out.println("Compressed file size: "
                + String.format("%.2f", (double) fileSize2 / 1024) + " KB (" + fileSize2 + " bytes)");
        // the ratio after compressed file
        System.out.println("Compressed ratio: "
                + (double) (fileSize2 * 100 / fileSize1) + "%");

    }

    private void writeCodesFile() {
//        encode();
        // writes the contents of codes to codeFileName.
        try {
            FileWriter codeOutput = new FileWriter(codesFileName);
            codeOutput.write(codes.toString());
            codeOutput.close();
        } catch (Exception e) {
            System.out.println("could not find the file to write!!");
        }
    }

    @Override
    public String toString() {
        return "Encoder{" +
                "inputFileName='" + inputFileName + '\'' +
                ", outputFileName='" + outputFileName + '\'' +
                ", codesFileName='" + codesFileName + '\'' +
                ", text='" + text + '\'' +
                ", frequencies=" + frequencies +
                ", encodedText=" + Arrays.toString(encodedText) +
                '}';
    }


    /**
     * HuffmanNode should be a private class of Encoder.
     * The additional constructor should be used to mergeHuffmanNodes during the compression algorithm.
     */

    private class HuffmanNode implements Comparable<HuffmanNode> {
        public int weight;
        public String symbol;
        public HuffmanNode left;
        public HuffmanNode right;

        public HuffmanNode(int weight, String symbol) {
            this.weight = weight;
            this.symbol = symbol;
            left = null;
            right = null;
        }

        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.left = left;
            this.right = right;
            this.weight = left.weight + right.weight;
            this.symbol = "";
        }

        @Override
        public int compareTo(HuffmanNode o) {
            return Integer.compare(this.weight, o.weight);
        }

        @Override
        public String toString() {
            return "String: " + symbol +
                    " weight:" + weight;
        }
    }

    public void encoderTest() {
        long start = System.currentTimeMillis();
        readInputFile();
        countFrequency();
        buildTree();
        assignCodes(huffmanTree, "");
        encode();
        writeOutputFile();
        writeCodesFile();
        long end = System.currentTimeMillis();
        long compressedTime = end - start;
        System.out.println("Compressed time: " + compressedTime + " millisecond");
        frequencies.stats();
    }

}
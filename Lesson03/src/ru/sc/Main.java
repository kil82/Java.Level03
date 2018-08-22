package ru.sc;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        task1();
        task2();
        task3();
    }

    public static void task1() throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream("text/task1.txt"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (in.read() != -1) {
            out.write(in.read());
        }
        byte[] byteArray = out.toByteArray();
        System.out.println(Arrays.toString(byteArray));
        in.close();
        out.close();
    }

    public static void task2() throws IOException {
        ArrayList<InputStream> inputArrayList = new ArrayList<>();
        inputArrayList.add(new FileInputStream("text/task2_1.txt"));
        inputArrayList.add(new FileInputStream("text/task2_2.txt"));
        inputArrayList.add(new FileInputStream("text/task2_3.txt"));
        inputArrayList.add(new FileInputStream("text/task2_4.txt"));
        inputArrayList.add(new FileInputStream("text/task2_5.txt"));

        BufferedInputStream in = new BufferedInputStream(new SequenceInputStream(Collections.enumeration(inputArrayList)));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("text/task2.txt"));
        while (in.read() != -1) {
            out.write(in.read());
        }
        in.close();
        out.close();
    }

    public static void task3() throws IOException {
        final int PAGE_SIZE = 1800;
        byte[] pageArray = new byte[PAGE_SIZE];
        RandomAccessFile fileForRead = new RandomAccessFile("text/task3.txt", "r");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter page number: ");
        int pageNumber = scanner.nextInt();
        long timeStart = System.currentTimeMillis();
        fileForRead.seek(PAGE_SIZE * (pageNumber - 1));
        fileForRead.read(pageArray);
        System.out.println(Arrays.toString(pageArray));
        fileForRead.close();
        System.out.println(System.currentTimeMillis() - timeStart);

    }
}

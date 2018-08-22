package ru.sc;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        //task1();
        //task2();
        task3();
    }

    static Object monitor = new Object();
    static volatile char printedChar = 'A';

    public static void task1() throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        synchronized (monitor) {
                            while (printedChar != 'A') {
                                monitor.wait();
                            }
                            System.out.print(printedChar);
                            printedChar = 'B';
                            monitor.notifyAll();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        synchronized (monitor) {
                            while (printedChar != 'B') {
                                monitor.wait();
                            }
                            System.out.print(printedChar);
                            printedChar = 'C';
                            monitor.notifyAll();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        synchronized (monitor) {
                            while (printedChar != 'C') {
                                monitor.wait();
                            }
                            System.out.print(printedChar);
                            printedChar = 'A';
                            monitor.notifyAll();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    volatile static boolean[] suspendFlag = {true, false, false};

    public static void task2() throws IOException {

        FileOutputStream outputStream = new FileOutputStream("task2.txt");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        synchronized (monitor) {
                            while (!suspendFlag[0]) {
                                monitor.wait();
                            }
                            try {
                                outputStream.write(121213);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Thread.sleep(200);
                            suspendFlag[0] = false;
                            suspendFlag[1] = true;
                            monitor.notifyAll();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        synchronized (monitor) {
                            while (!suspendFlag[1]) {
                                monitor.wait();
                            }
                            try {
                                outputStream.write(131312);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Thread.sleep(200);
                            suspendFlag[1] = false;
                            suspendFlag[2] = true;
                            monitor.notifyAll();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        synchronized (monitor) {
                            while (!suspendFlag[2]) {
                                monitor.wait();
                            }
                            try {
                                outputStream.write(141412);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Thread.sleep(200);
                            suspendFlag[2] = false;
                            suspendFlag[0] = true;
                            monitor.notifyAll();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }


    public static void task3() {
        MFU mfu1 = new MFU();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mfu1.print(10);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mfu1.scan(15);
            }
        }).start();
    }
}
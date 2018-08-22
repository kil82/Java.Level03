package ru.sc;


public class MFU {
    static private final Object mfuMonitor = new Object();
    private boolean canPrint = true;
    private boolean canScan = true;
    public void print(int n) {
        synchronized (mfuMonitor) {
            while (!canPrint) {
                try {
                    mfuMonitor.wait();
                } catch (InterruptedException e) {
                    System.out.println("Exception");
                }
            }
            canScan = false;
            canPrint = false;
            for (int i = 0; i < n; i++) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Печатаем страницу : " + i);
            }

            canScan = true;
            canPrint = true;
            mfuMonitor.notifyAll();
        }
    }

    public void scan(int n) {
        synchronized (mfuMonitor) {
            while (!canScan) {
                try {
                    mfuMonitor.wait();
                } catch (InterruptedException e) {
                    System.out.println("Exception");
                }
            }
            canScan = false;
            canPrint = false;
            for (int i = 0; i < n; i++) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Сканируем страницу : " + i);
            }

            canScan = true;
            canPrint = true;
            mfuMonitor.notifyAll();
        }
    }
}

package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @param N The number of threads 
     */
    public static ArrayList<Byte> getDigits(int start, int count, int N) {
        
        int totalCalculate = count / N;
        LinkedList<DigitThread> threads = new LinkedList<>();
        
        //byte[] result = new byte[count];
        ArrayList<Byte> result = new ArrayList<>();

        Object lock = new Object();
        boolean anyThreadRunning = true;
        Scanner sc = new Scanner(System.in);
        int currentNumberDigits = 0;
        ArrayList<Byte> digitT;
        
        for (int i = 0; i < N; i++) {
            int starts = start + (totalCalculate*i);
            totalCalculate = i == (N - 1) ? count - (totalCalculate*i): totalCalculate;
            DigitThread threadI = new DigitThread(starts, totalCalculate, lock);
            threads.add(threadI);
            threadI.start();
        }

        while(anyThreadRunning) {
            try {
                anyThreadRunning = false;
                for (DigitThread thread : threads) {
                    if (thread.isAlive()) {
                        anyThreadRunning = true;
                        break;
                    }
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            digitT = getBytes(threads);
            currentNumberDigits = 0;
            for(DigitThread thread: threads) {
                currentNumberDigits += thread.getDigits().size();
            }
            System.out.println("Cantidad calculada: " + currentNumberDigits);
            // Se espera que el usuario haga Enter para reanudar los hilos
            if(anyThreadRunning) {
                System.out.println("Press ENTER to continue...");
                sc.nextLine();
                synchronized(lock) {
                    lock.notifyAll();
                }
            }
        }
        digitT = getBytes(threads);
        return digitT;
    }

    public static ArrayList<Byte> getBytes(LinkedList<DigitThread> threads) {
        ArrayList<Byte> digitT = new ArrayList<Byte>();
        for (DigitThread thread: threads) {
            ArrayList<Byte> byteList = new ArrayList<Byte>();
            for (byte b: thread.getDigits()) {
                byteList.add(b);
            }
            digitT.addAll(byteList);
        }
        return digitT;
    }
 
}

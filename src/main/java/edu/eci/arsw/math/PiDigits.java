package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.LinkedList;

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
    public static byte[] getDigits(int start, int count, int N) {
        
        int totalCalculate = count / N;
        LinkedList<DigitThread> threads = new LinkedList<>();
        ArrayList<Byte> digitT = new ArrayList<Byte>();
        byte[] result = new byte[count];

        for (int i = 0; i < N; i++) {
            int starts = start + (totalCalculate*i);
            totalCalculate = i == (N - 1) ? count - (totalCalculate*i): totalCalculate;
            DigitThread threadI = new DigitThread(starts, totalCalculate);
            threads.add(threadI);
            threadI.start();
        }

        for (DigitThread thread : threads) {

            try {
                thread.join();
                ArrayList<Byte> byteList = new ArrayList<Byte>();
                for (byte b: thread.getDigits()) {
                    byteList.add(b);
                }
                digitT.addAll(byteList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < digitT.size(); i++) {
        result[i] = digitT.get(i);
    }
        return result;
        
    }
 
}

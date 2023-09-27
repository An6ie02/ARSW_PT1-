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
        
        int totalCalculate = count / N; // Cantidad de numeros que calcula cada hilo
        LinkedList<DigitThread> threads = new LinkedList<>();
        Object lock = new Object();
        boolean anyThreadRunning = true; //Al menos un hilo esta en ejecucion
        Scanner sc = new Scanner(System.in);
        int currentNumberDigits = 0;
        ArrayList<Byte> digitT;
        
        for (int i = 0; i < N; i++) { // Por el numero de hilos solicitados
            /**El valor de inicio del hilo va cambiando en cada iteración: se va sumando el total de
            numeros que han sido calculos por otros hilos(i)
            **/
            int starts = start + (totalCalculate*i); 
            // Ajusta la cantidad de numeros que calculara el ultimo hilo, asegura que calcule los numeros 
            //restantes para llegar al valor total count.
            totalCalculate = i == (N - 1) ? count - (totalCalculate*i): totalCalculate;
            //Se crea y agrega el hilo a la lista de hilos
            DigitThread threadI = new DigitThread(starts, totalCalculate, lock);
            threads.add(threadI);
            threadI.start();
        }
        //Mientras que hayan hilos en ejecucion
        while(anyThreadRunning) {
            try {
                anyThreadRunning = false; //Se asume que no hay hilos en ejecucion para luego verificar si los hay
                for (DigitThread thread : threads) {
                    if (thread.isAlive()) { // determina si un hilo esta vivo
                        anyThreadRunning = true;
                        break;
                    }
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentNumberDigits = 0;
            //Se muestran la cantidad de digitos calculados hasta el momento de todos los threads
            for(DigitThread thread: threads) {
                currentNumberDigits += thread.getDigits().size();
            }
            System.out.println("Cantidad calculada: " + currentNumberDigits);
            // Se espera que el usuario haga Enter para reanudar los hilos una vez verificado que hayan hilos en ejecucion
            if(anyThreadRunning) {
                System.out.println("Press ENTER to continue...");
                sc.nextLine();
                synchronized(lock) {
                    lock.notifyAll();
                }
            }
        }
        //Se obtienen los resultados finales
        digitT = getBytes(threads);
        return digitT;
    }

    /**
     * Obtiene la lista de digitos pi calculados
     * @param threads
     * @return lista de los bytes calculados
     */
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

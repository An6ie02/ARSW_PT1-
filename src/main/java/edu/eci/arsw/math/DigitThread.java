package edu.eci.arsw.math;

import java.util.ArrayList;

public class DigitThread extends Thread {
    
    private int start;
    private int count;
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private ArrayList<Byte> digits;
    private Object lock;
    private boolean running;
    
    public DigitThread(int start, int countEnd, Object lock) {
        this.start = start;
        this.count = countEnd;
        this.digits = new ArrayList<>();
        this.lock = lock;
        this.running = true;
    }

    @Override
    public void run() {
        //devuelve la hora actual del sistema en milisegundos, hora de inicio del hilo
        long inicialTime = System.currentTimeMillis(); 
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        double sum = 0;

        for (int i = 0; i < count; i++) {
            //Se verifica que no han pasado los 5 segundos reglamentarios de ejecucio y se procede a hacer el calculo de los digitos
            if (System.currentTimeMillis() - inicialTime <= 5000) {
                if (i % DigitsPerSum == 0) {
                    sum = 4 * sum(1, start)
                            - 2 * sum(4, start)
                            - sum(5, start)
                            - sum(6, start);

                    start += DigitsPerSum;
                }

                sum = 16 * (sum - Math.floor(sum));
                digits.add((byte) sum);
            } else { //Si han pasado mas de 5 segundos
                running = false; //Se le indica al hilo que debe detenerse
                synchronized(lock) { //Se controla el acceso 
                    while(!running) {
                        try {
                            lock.wait(); //Esto suspende el hilo y lo coloca en un estado de espera hasta que sea notificado
                            inicialTime = System.currentTimeMillis(); //Se reinicia el tiempo de inicio de ejecucion del hilo
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        
    }
    
    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

    public ArrayList<Byte> getDigits() {
        return digits;
    }

}


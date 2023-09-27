/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

import java.util.ArrayList;

/**
 *
 * @author hcadavid
 */
public class Main {

    public static void main(String a[]) {
        
        System.out.println(bytesToHex(PiDigits.getDigits(0, 10, 2)));
        System.out.println("====================================================");
        System.out.println(bytesToHex(PiDigits.getDigits(1, 10000, 5)));

    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(ArrayList<Byte> arrayList) {
        char[] hexChars = new char[arrayList.size() * 2];
        for (int j = 0; j < arrayList.size(); j++) {
            int v = arrayList.get(j) & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }

}

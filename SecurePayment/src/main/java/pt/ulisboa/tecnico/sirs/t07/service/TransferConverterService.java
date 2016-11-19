package pt.ulisboa.tecnico.sirs.t07.service;

import javax.xml.bind.ValidationException;

import java.util.Vector;

import static java.lang.Math.pow;

/**
 * Created by tiago on 12/11/2016.
 */
public class TransferConverterService {
    private long[] converted;
    private int converted2;
    private String originIban;
    private String destinationIban;

    public TransferConverterService(String originIban, String destinationIban){
        this.originIban = originIban;
        this.destinationIban = destinationIban;
        this.converted = new long[3];
        String last = destinationIban.toUpperCase().substring(destinationIban.length()-10,destinationIban.length());
        long lastInt = Long.parseLong(last);
        converted2 = (int) joinNumbers(countryCodeToInt(destinationIban),getCheckDigit(destinationIban),2);

        converted[2] = joinNumbers(countryCodeToInt(originIban),getCheckDigit(originIban),2);
        converted[2] = joinNumbers(lastInt,converted[2],6);

        String restDest = destinationIban.toUpperCase().substring(4,destinationIban.length()-10);
        long rest = Long.parseLong(restDest);
        converted[1] = rest;

        String lastOrig = originIban.toUpperCase().substring(originIban.length()-5,originIban.length());
        rest = Long.parseLong(lastOrig);
        converted[1] = joinNumbers(rest,converted[1],11);

        lastOrig = originIban.toUpperCase().substring(4,originIban.length()-5);
        rest = Long.parseLong(lastOrig);
        converted[0] = rest;
        System.out.println(converted[0]);
        System.out.println(converted[1]);
        System.out.println(converted[2]);
        System.out.println(converted2);

    }

    public TransferConverterService(long first, long secound, long third, int dest){
        this.converted = new long[3];
        converted[0] = first;
        converted[1] = secound;
        converted[2] = third;
        converted2 = dest;

        destinationIban = intToCountryCode(dest/100)+(dest%100)+(secound+"").substring(5)+(third+"").substring(0,10);
        originIban = intToCountryCode(new Long((third/100)%10000).intValue()) + (third%100)+first+(secound+"").substring(0,5);
    }


    private int countryCodeToInt(String iban){
        char[] charCountry = iban.toUpperCase().substring(0,2).toCharArray();
        int code = 0;
        for(char c : charCountry){
            int num = c - 55;
            code = code*100+num;
        }
        return code;
    }

    private String intToCountryCode(int val){
            char secound = (char)(val%100+55);
            char first = (char) ((val/100)+55);
        return ""+first+secound;
    }


    private int getCheckDigit(String iban){
        return Integer.parseInt(iban.toUpperCase().substring(2,4));
    }


    private long joinNumbers(long left,long right,long lengthRight){
        return (long) (left*pow(10,lengthRight)+right);
    }

    public String getOriginIban() {
        return originIban;
    }

    public String getDestinationIban() {
        return destinationIban;
    }

    public long getConverted0() {
        return converted[0];
    }

    public long getConverted1() {
        return converted[1];
    }

    public long getConverted2() {
        return converted[2];
    }

    public int getConverted3() {
        return converted2;
    }
}

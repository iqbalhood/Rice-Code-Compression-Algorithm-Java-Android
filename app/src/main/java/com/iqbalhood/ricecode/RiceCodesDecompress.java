package com.iqbalhood.ricecode;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * Created by IQBAL on 5/23/2016.
 */
public class RiceCodesDecompress {

    private static String dec2bin(int dec) {
        StringBuffer bin = new StringBuffer();
        int r = dec % 2;
        bin.insert(0, r);
        dec = dec / 2;
        while (dec != 0){
            r = dec % 2;
            bin.insert(0, r);
            dec = dec / 2;
        }
        return bin.toString();
    }

    private static int bin2dec(String bin) {
        StringBuffer b = new StringBuffer(bin);
        b.reverse();
        int n = 1;
        int dec = 0;
        for (int i = 0; i < b.length(); i++) {
            dec = dec + Character.getNumericValue(b.charAt(i)) * n;
            n = n * 2;
        }
        return dec;
    }


    private static String decode(String code) {
        StringBuffer sb = new StringBuffer();
        int t = code.length();
        for (int i = 0; i < t; i++) {
            char x = code.charAt(i);
            StringBuffer y = new StringBuffer(dec2bin((int)x));
            if (y.length() < 8) {
                int z = 8 - y.length() % 8;
                for (int j = 0; j < z; j++)
                    y.insert(0, "0");
            }
            sb.append(y);
        }
        return sb.toString();
    }

    public static String BacaPlain(String namafile){
        String baca = "";
        File root = new File(Environment.getExternalStorageDirectory(), "kompresi");
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(namafile);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            System.out.println("Total file size to read (in bytes) : "
                    + fis.available());

            int content;
            while ((content = fis.read()) != -1) {
                // convert to char and display it
                baca +=  String.valueOf((char) content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return baca;
    }



    public static String bacaFH(){

        String baca = "";
        String namafile = "header.txt";
        File root = new File(Environment.getExternalStorageDirectory(), "header");
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, namafile);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            int content;
            while ((content = fis.read()) != -1) {
                // convert to char and display it
                baca +=  String.valueOf((char) content);


            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return baca;

    }

    private	static String decompress(String	stb, String	c, String[]	s) {
        StringBuffer st = new StringBuffer();
        StringBuffer bit = new StringBuffer();
        StringBuffer stb2;
        int t = stb.length();
        System.out.println("==== PANJANG STB ===== "+stb.length());
        int pad = bin2dec(stb.substring(t - 8, t));
        stb2 = new StringBuffer(stb.substring(0, t - pad - 8));
        System.out.println("==== PANJANG STB2 ===== "+stb2.length());
        for (int i = 0; i < stb2.length(); i++) {
            bit.append(stb2.charAt(i));
            if (Arrays.asList(s).contains(bit.toString())) {
                int x = Arrays.asList(s).indexOf(bit.toString());
                System.out.println(st.append(c.charAt(x)));
                bit = new StringBuffer();
            }
        }
        return st.toString();

    }


    public static String proses_dekompresi (String AlamatFileKompresi, String NamaFileDekompresi){

        String h = readFromFile(AlamatFileKompresi);
        String dekom = dekompresi(h);
        tulis(dekom, NamaFileDekompresi);

        return "proses dekompresi selesai";
    }




    public static String dekompresi(String stb){
        String sc = bacaFH();

        String namafile = "header2.txt";
        File root = new File(Environment.getExternalStorageDirectory(), "header");
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, namafile);
        String[] ge ={};
        String ak = "";
        try {

            FileInputStream fos = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fos);

            ge = (String[]) oos.readObject();
            ak += ge;

            oos.close();

        }
        catch(Exception e){}

        String dc =	decode(stb);
        System.out.println("========= DECODE STRING==============");
        System.out.println(dc);
        System.out.println("=======================\n");

        String ds = decompress(dc, sc, ge);
        System.out.println("=======================");
        System.out.println(sc);
        System.out.println("=======================\n");

        System.out.println("=======================");
        System.out.println(Arrays.toString(ge));
        System.out.println("=======================\n");


        System.out.println("=======================");
        System.out.println(ds);
        System.out.println("=======================\n");


        return ds;
    }




    public static void tulis(String tulis, String nama_file){
        try {

            String content = tulis;

            File root = new File(Environment.getExternalStorageDirectory(), "dekompresi");
            if (!root.exists()) {
                root.mkdirs();
            }

            File file = new File(root, nama_file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private  static  String readFromFile(String fileName) {


        String ret = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                ret += sCurrentLine;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


















}

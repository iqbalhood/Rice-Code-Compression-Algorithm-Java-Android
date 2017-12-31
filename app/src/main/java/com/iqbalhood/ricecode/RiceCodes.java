package com.iqbalhood.ricecode;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;


/**
 * Created by Dita Ananda Yulaz on 5/17/2016.
 */
public class RiceCodes {

    private static int[] fr;
    private static String cs;
    private static String[] rc;


    private static String getCharSet(String st) {
        StringBuffer s = new StringBuffer();
        int n = st.length();
        for (int i = 0; i < n; i++) {
            String c = Character.toString(st.charAt(i));
            if (s.indexOf(c) == -1)
                s.append(c);
        }
        return s.toString();
    }

    private static int countChar(String s, char ch) {
        String c = Character.toString(ch);
        return (s.length() - s.replace(c, "").length()) / c.length();
    }

    private static int[] countFreq(String st) {
        String charset = getCharSet(st);
        int n = charset.length();
        int[] freq = new int[n];
        for (int i = 0; i < n; i++)
            freq[i] = countChar(st, charset.charAt(i));
        return freq;
    }

    private static void InsertionSort(int[] freq, String charset) {
        fr = freq;
        cs = charset;
        int n = charset.length();
        StringBuffer sb = new StringBuffer(charset);
        for (int i = 1; i < n; i++)
            for (int j = 0; j < i; j++)
                if (fr[i] > fr[j]){
                    int temp = fr[i];
                    fr[i] = fr[j];
                    fr[j] = temp;
                    char ctemp = sb.charAt(i);
                    sb.setCharAt(i, sb.charAt(j));
                    sb.setCharAt(j, ctemp);
                }
        cs = sb.toString();
    }

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

    private static String string2stb(String st, String c, String[] s) {
        StringBuffer stb = new StringBuffer();
        for (int i = 0; i < st.length(); i++) {
            String z = Character.toString(st.charAt(i));
            int k = c.indexOf(z);
            stb.append(s[k]);
        }
        int x = stb.length() % 8;
        int pad = 0;
        if (x != 0) {
            pad = 8 - x;
            for (int i = 0; i < pad; i++)
                stb.append("0");
        }
        String d = dec2bin(pad);
        int y = 8 - d.length();
        for (int i = 0; i < y; i++)
            stb.append("0");
        stb.append(d);
        return stb.toString();
    }

    private static String encode(String stb) {
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < stb.length(); i += 8) {
            String x = stb.substring(i, i + 8);
            char y = (char)(bin2dec(x));
            code.append(y);
        }
        return code.toString();
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

    private static String decompress(String stb, String c, String[] s) {
        StringBuffer st = new StringBuffer();
        StringBuffer bit = new StringBuffer();
        StringBuffer stb2;
        int t = stb.length();
        int pad = bin2dec(stb.substring(t - 8, t));
        stb2 = new StringBuffer(stb.substring(0, t - pad - 8));
        for (int i = 0; i < stb2.length(); i++) {
            bit.append(stb2.charAt(i));
            if (Arrays.asList(s).contains(bit.toString())) {
                int x = Arrays.asList(s).indexOf(bit.toString());
                st.append(c.charAt(x));
                bit = new StringBuffer();
            }
        }
        return st.toString();
    }

    private static int power(int x, int y){
        int result = 1;
        for (int i = 0; i < y; i++)
            result = result * x;
        return result;
    }




    private static void Rice(String st, int k) {
        int[] freq = countFreq(st);
        String charset = getCharSet(st);
        int t = charset.length();
        InsertionSort(freq, charset);
        freq = fr;
        charset = cs;

        StringBuffer[] prefix = new StringBuffer[t / power(2, k) + 1];
        prefix[0] = new StringBuffer("0");
        for (int i = 1; i < t / power(2, k) + 1; i++)
            prefix[i] = new StringBuffer("1" + prefix[i - 1].toString());
        int stop = power(2, k) - 1;

        StringBuffer[] suffix = new StringBuffer[stop + 1];
        int s = 0;

        for (int i = 0; i < stop + 1; i++) {
            String b = dec2bin(i);
            StringBuffer pad = new StringBuffer();
            for (int j = 0; j < k - b.length(); j++)
                pad.append("0");
            suffix[s++] = pad.append(b);
        }

        if (k == 0)
            suffix[0] = new StringBuffer();

        String[] rc2 = new String[prefix.length * suffix.length];

        s = 0;

        for (int i = 0; i < prefix.length; i++) {
            for (int j = 0; j < suffix.length; j++) {
                rc2[s++] = prefix[i].toString() + suffix[j].toString();
            }
        }

        rc = new String[t];
        for (int i = 0; i < t; i++)
            rc[i] = rc2[i];
    }




    public static String kompresi(String st){
        int k = 2;
        String kompresi = "";
        Rice(st, k);

        String stb = string2stb(st, cs, rc);
        System.out.print("======"+stb);
        kompresi = stb;
        return kompresi;

    }

    public String   processRiceCodes(String namafile,  String nama_file_kompresi, int k) {
        String bits = "";

        String str = BacaPlain(namafile);
      //  String nama_file_kompresi ="FILE_KOMPRESI.rc";
        String hasil = kompresi(str);
        String code	= encode(hasil);
        buatFK(nama_file_kompresi,  code);
        buatFH( "header.txt", "" );
        buatFH2( "header2.txt", "" );
        String stb = string2stb(str, cs, rc);


        int uncompressed_bits = str.length() * 8;
        int compressed_bits = stb.length();

        double CR = (double)(((compressed_bits * 1.0) / uncompressed_bits) * 100 )/100;
        double SS = (double)((1.0 - 1.0/CR) * 100)*(-1);
        double RC = (double) uncompressed_bits  / compressed_bits;

        bits =  " =================  \n"+
                "Uncompressed_bits = " + uncompressed_bits + "\n"
                +" =================  \n"+
                "Compressed_bits  = " + compressed_bits + "\n"
                +" =================  \n"
                +"Ratio of Compression = " + RC +"\n"
                +" =================  \n"
                +"Compression Ratio = " + CR + "%"+"\n"
                +" =================  \n"
                +"Space Savings = " + SS + " %"+"\n"
                +" =================  \n";



       return bits ;
    }






    public static void buatFH( String sFileName, String sBody){


        Environment.getExternalStorageState();


        File root = new File(Environment.getExternalStorageDirectory(), "header");
        if (!root.exists()) {
            root.mkdirs();
        }

        String content = cs;
        File file = new File(root,sFileName);
        // get the content in bytes
        byte[] contentInBytes = content.getBytes();
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            fOut.write(contentInBytes);
            fOut.close();

        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }







    public static void buatFH2( String sFileName, String sBody){


        Environment.getExternalStorageState();


        File root = new File(Environment.getExternalStorageDirectory(), "header");
        if (!root.exists()) {
            root.mkdirs();
        }

        String content = cs;
        File file = new File(root,sFileName);
        // get the content in bytes
        byte[] contentInBytes = content.getBytes();
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(fOut);
            oos.writeObject(rc);
            oos.close();

        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }



    public static void buatFK(String nama_file_kompresi, String hasil){
         try {

            File root = new File(Environment.getExternalStorageDirectory(), "kompresi");
            if (!root.exists()) {
                root.mkdirs();
            }



            File gpxfile = new File(root,nama_file_kompresi);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(hasil);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String BacaPlain(String namafile){
        String baca = "";
        File root = new File(Environment.getExternalStorageDirectory(), "fileasal");
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




























}

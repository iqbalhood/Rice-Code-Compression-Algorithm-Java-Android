package com.dita.ricecode;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dita Ananda Yulaz on 5/17/2016.
 */
public class Compress extends AppCompatActivity {

    Button proses, cancel;
    TextView hasil ;

    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder;

    //EditTeks Untuk Save File
    EditText ed_simpan;


    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    File root;
    File curFolder;

    String file_asal = "";


    private List<String> fileList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compress_dialog);

        buttonOpenDialog = (Button) findViewById(R.id.opendialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;

        proses = (Button)findViewById(R.id.proses);
        cancel = (Button)findViewById(R.id.btnCancel);
        hasil = (TextView)findViewById(R.id.hasil);
        ed_simpan = (EditText)findViewById(R.id.save);

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String filesimpan = ed_simpan.getText().toString();

                if (filesimpan.matches("")) {
                    Toast.makeText(getApplicationContext(), "File name cannot be blank !!!", Toast.LENGTH_SHORT).show();
                }else {
                    int k = 2;
                    RiceCodes rice = new RiceCodes();

                    //hitung waktu mulai
                    long startTime = System.currentTimeMillis();
                    //proses kompresi
                    String hasil_teks = rice.processRiceCodes(file_asal,filesimpan, k);
                    //hitung waktu selesai
                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;


                    String Waktu_Kompresi = "Compression Time = "+totalTime + " ms";


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            Compress.this);

                    // set title
                    alertDialogBuilder.setTitle("Result");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("" + hasil_teks + "\n" + Waktu_Kompresi)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    Compress.this.finish();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent k = new Intent(Compress.this, Dashboard.class);
                k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(k);




            }
        });
    }



    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(Compress.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("File Browser");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File selected = new File(fileList.get(position));
                        if(selected.isDirectory()) {
                            ListDir(selected);
                        } else {
                            Toast.makeText(Compress.this, selected.toString() + " selected",
                                    Toast.LENGTH_LONG).show();
                            dismissDialog(CUSTOM_DIALOG_ID);

                            file_asal = selected.toString();
                            buttonOpenDialog.setText(file_asal);

                        }
                    }
                });

                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }

    void ListDir(File f) {
        if(f.equals(root)) {
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();

        for(File file : files) {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }

















}
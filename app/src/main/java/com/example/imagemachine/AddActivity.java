package com.example.imagemachine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSave, btnImg, btnNext;

    EditText machineIDET, machineNameET, machineTypeET, machineQRET, machineDateET;

    DatePickerDialog machineDatePD;

    SimpleDateFormat dateFormatter;

    private int PICK_IMAGE_REQUEST = 1;

    ImageView imageView;

    private List<Uri> selectedImage = null;

    private int currentDisplayedImg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        machineIDET = (EditText) findViewById(R.id.etAddMID);
        machineNameET = (EditText) findViewById(R.id.etAddMName);
        machineTypeET = (EditText) findViewById(R.id.etAddMType);
        machineQRET = (EditText) findViewById(R.id.etAddMQR);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        machineDateET = (EditText) findViewById(R.id.etAddMDate);
        machineDateET.setShowSoftInputOnFocus(false);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnImg = (Button) findViewById(R.id.btnImg);
        btnNext = (Button) findViewById(R.id.btnNextImg);

        imageView = (ImageView) findViewById(R.id.imageView);

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose Image"), PICK_IMAGE_REQUEST);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImage != null){
                    Uri currentImg = selectedImage.get(currentDisplayedImg);

                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), currentImg);

                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int size = selectedImage.size();

                    if(currentDisplayedImg >= (size - 1)){
                        currentDisplayedImg = 0;
                    }
                    else currentDisplayedImg++;
                }
            }
        });

        setDateField();

        if(checkData()){
            saveNewMachine();
        }
    }

    //Checks if all input has been filled
    private boolean checkData() {
        if(machineIDET.getText().toString() != "" && machineNameET.getText().toString() != "" && machineTypeET.getText().toString() != "" && machineQRET.getText().toString() != "" && machineDateET.getText().toString() != ""){
            return true;
        }
        else return false;
    }

    //Function to save new machine
    private void saveNewMachine() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineDB db = MachineDB.getInstance(AddActivity.this);

                Machine newMachine = new Machine();

                //sets the value to the machine class
                newMachine.ID = Integer.parseInt(machineIDET.getText().toString());
                newMachine.name = machineNameET.getText().toString();
                newMachine.type = machineTypeET.getText().toString();
                newMachine.qr = Integer.parseInt(machineQRET.getText().toString());
                newMachine.date = machineDateET.getText().toString();

                db.addMachine(newMachine);

                //Alert dialog when SQL is executed and close the activity, back to main activity
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Info")
                        .setMessage("Added 1 new data")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .show();
            }
        });


    }

    //Set date picker dialog
    private void setDateField() {
        machineDateET.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        machineDatePD = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                machineDateET.setText(dateFormatter.format(newDate.getTime())); //sets the value to date field
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    //Click event for the date picker on date edittext
    @Override
    public void onClick(View view) {
        if(view == machineDateET){
            machineDatePD.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();

            if(selectedImage == null){
                selectedImage = new ArrayList<Uri>();
            }
            selectedImage.add(uri);

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

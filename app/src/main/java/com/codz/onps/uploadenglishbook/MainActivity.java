package com.codz.onps.uploadenglishbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 99;
    private File MIN_DIR;
    private static final String TAG = "TAG_ERR";

    String file_name = "file_test3234";

    private static final int SPLASH_TIME_OUT = 3000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MIN_DIR = new File(getExternalFilesDir(null), ".MyBooksFile");
        if(checkDirFiles()){
            showDone();
        }else hideDone();

        findViewById(R.id.button).setOnClickListener(v->{
            showProgress();
            copyFile();
        });
    }

    private void showDone() {
        findViewById(R.id.done_layout).setVisibility(View.VISIBLE);
    }

    private void hideDone() {
        findViewById(R.id.done_layout).setVisibility(View.GONE);
    }



    private void copyFile() {
        InputStream inputStream = getResources().openRawResource(R.raw.file);

        // Destination directory path
        File destinationDirectory = new File(getExternalFilesDir(null), ".MyBooksFile");

        // Create the destination directory if it doesn't exist
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        // Destination file path
        File destinationFile = new File(destinationDirectory, file_name+".pdf");

        // Copy the file
        try {
            OutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
//            Toast.makeText(this, "File copied successfully", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                    showDone();
                }
            }, SPLASH_TIME_OUT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: "+ e.getMessage());
            Toast.makeText(this, "Failed, try again", Toast.LENGTH_SHORT).show();
            hideProgress();
        }
    }

    private void showProgress(){
        findViewById(R.id.button).setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        findViewById(R.id.button).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }





    private boolean checkDirFiles() {
        if (MIN_DIR.exists() && MIN_DIR.isDirectory()) {
            // Get all files in the directory
            File[] files = MIN_DIR.listFiles();

            // Check if any files exist
            if (files != null && files.length > 0) {
                // Iterate over the files and get their names
                for (File file : files) {
                    String fileName = file.getName();
                    // Do something with the file name
                    if(fileName.equals(file_name+".pdf"))return true;
                    Log.d(TAG, "- "+fileName);
                }
            } else {
                // No files found in the directory
            }
        }

        return false;
    }
}
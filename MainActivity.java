package com.example.qrcodegenerator;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV;
    private EditText dataEdt;
    private Button generateQrBtn, saveQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing all variables.
        qrCodeIV = (ImageView) findViewById(R.id.idIVQrcode);
        dataEdt = (EditText) findViewById(R.id.idEdt);
        generateQrBtn = (Button) findViewById(R.id.idBtnGenerateQR);
        saveQrBtn = (Button) findViewById(R.id.idsaveImage);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


        // initializing onclick listener for button.
        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dataEdt.getText().toString())) {

                    // if the edittext inputs are empty then execute
                    // this method showing a toast message.
                    Toast.makeText(MainActivity.this, "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
                } else {
                    // below line is for getting
                    // the windowmanager service.
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

                    // initializing a variable for default display.
                    Display display = manager.getDefaultDisplay();

                    // creating a variable for point which
                    // is to be displayed in QR Code.
                    Point point = new Point();
                    display.getSize(point);

                    // getting width and
                    // height of a point
                    int width = point.x;
                    int height = point.y;

                    // generating dimension from width and height.
                    int dimen = width < height ? width : height;
                    dimen = dimen * 3 / 4;

                    // setting this dimensions inside our qr code
                    // encoder to generate our qr code.
                    qrgEncoder = new QRGEncoder(dataEdt.getText().toString(), null, QRGContents.Type.TEXT, dimen);
                    try {
                        // getting our qrcode in the form of bitmap.
                        bitmap = qrgEncoder.encodeAsBitmap();
                        // the bitmap is set inside our image
                        // view using .setimagebitmap method.
                        qrCodeIV.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        // this method is called for
                        // exception handling.
                        Log.e("Tag", e.toString());
                    }
                }
            }
        });
        saveQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            saveGallery();
            }

        });


    }
private void saveGallery(){
    BitmapDrawable bitmapDrawable=(BitmapDrawable) qrCodeIV.getDrawable();
    Bitmap bitmap=bitmapDrawable.getBitmap();


    FileOutputStream fileOutputStream=null;
    File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    File dir=new File(file.getAbsolutePath()+"/MyPics");
    dir.mkdirs();
    String filename=String.format("%d.png",System.currentTimeMillis());
    File outFile= new File(dir,filename);
    try {
        fileOutputStream=new FileOutputStream(outFile);

    }catch (Exception e){
        e.printStackTrace();
    }
    bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
    try {
    fileOutputStream.flush();
    }catch(Exception e){
    e.printStackTrace();
    }
    try {
        fileOutputStream.close();
    }catch(Exception e){
        e.printStackTrace();
    }
}


}
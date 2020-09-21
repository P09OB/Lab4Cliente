package com.example.lab4cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4cliente.model.Confirmacion;
import com.example.lab4cliente.model.Usuarios;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name;
    private EditText password;

    private Button login;
    private InputStream is;
    private OutputStream os;
    private Socket socket;
    private OutputStreamWriter osw;
    private BufferedWriter writer;
    private BufferedReader reader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.buttonlogin);

        login.setOnClickListener(this);

        initCliente();




    }

    public void initCliente() {

        new Thread(

                ()->{

                    try {
                        socket = new Socket("192.168.0.42",5000);
                        is = socket.getInputStream();
                        int mensaje = is.read();

                        InputStreamReader isr= new InputStreamReader(is);
                        reader = new BufferedReader(isr);


                        Log.e("recibido",mensaje + "");
                        os = socket.getOutputStream();
                        osw  = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                        while(true){

                            String line = reader.readLine();
                            Gson gson = new Gson();
                            Confirmacion conf = gson.fromJson(line,Confirmacion.class);
                            boolean respuesta = conf.getRespuesta();

                            Log.e("recibido",respuesta + "");

                            if(respuesta == true){

                                Intent intent = new Intent(this,WelcomeActivity.class);
                                startActivity(intent);

                            } if(respuesta == false){

                                runOnUiThread(

                                        ()->{
                                            Toast.makeText(this,"No es corresto el usuario o la contraseña",Toast.LENGTH_LONG).show();

                                        }
                                );

                            }

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

        ).start();


    }

    public void enviar (String mensaje) {
        new Thread(

                ()->{
                    try {

                        writer.write(mensaje+"\n");
                        writer.flush();



                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }




        ).start();




    }


    @Override
    public void onClick(View view) {

        Gson gson = new Gson();

        String ID = UUID.randomUUID().toString();
        String username = name.getText().toString();
        String contrasenna = password.getText().toString();

        Usuarios user = new Usuarios(username,contrasenna,ID);

        String json = gson.toJson(user);
        Log.e(">>>>>",""+json);
        enviar(json);

        SharedPreferences preferences = getSharedPreferences("datos",MODE_PRIVATE);
        preferences.edit().putString("user",username).apply();
    }
}
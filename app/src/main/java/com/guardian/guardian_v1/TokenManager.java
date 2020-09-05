package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Scanner;

public class TokenManager extends AppCompatActivity {
    private static TokenManager tokenManager;
    public static TokenManager getInstance(){
        if (tokenManager == null){
            tokenManager = new TokenManager();
        }
        return tokenManager;
    }

    /*public void saveToken(String token){
        String name = "guardian_token.json";
        File file = new File( name);
        try{
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(token);
            writer.close(); }
        catch(IOException exception){
            exception.printStackTrace();
        }
    }*/

    public void saveToken(String textToSave) {
        File dir = new File(this.getFilesDir(), "guardian_token.txt");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            FileOutputStream fileOutputStream = openFileOutput("guardian_token.txt", MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    /*public String getToken(){
        File file = new File("guardian_token.json");
        if (!file.exists()) {
            return null;
        }
        try {
            Scanner s = new Scanner(file);
            return s.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }*/

    public String getToken() {
        StringBuilder stringBuffer = new StringBuilder("");
        try {
            FileInputStream fileInputStream = openFileInput("guardian_token.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuilder();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public boolean hasValidToken(){
        return false;
    }
}

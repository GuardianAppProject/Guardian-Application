package com.guardian.guardian_v1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class TokenManager {
    private static TokenManager tokenManager;
    public static TokenManager getInstance(){
        if (tokenManager == null){
            tokenManager = new TokenManager();
        }
        return tokenManager;
    }

    public void saveToken(String token){
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
    }

    public String getToken(){
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
    }

    public boolean hasValidToken(){
        return false;
    }
}

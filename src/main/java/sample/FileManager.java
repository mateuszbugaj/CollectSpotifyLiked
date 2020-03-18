package sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private int numberOfEntries = 0;

    private BufferedWriter writer;


    public FileManager() throws IOException {
        writer = new BufferedWriter(new FileWriter("Spotify liked",true));
    }

    public void save(String data) {
        try {
            numberOfEntries++;
            writer.append("\n"+numberOfEntries+"\n").append(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWriter(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

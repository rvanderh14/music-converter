package be.rony.musicconverter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MusicConverter {


    /**
     * Main routine.
     * <p>args[0] is the input file name
     * <p>args[1] is the output file name
     * @param args  Command line arguments
     */
    public static void main(String[] args) {
        try {

            String inFileName = "/home/rony/IdeaProjects/musicconverter/src/main/resources/02 Thunder Road.flac";
            String outFileName = "/home/rony/IdeaProjects/musicconverter/src/main/resources/02 Thunder Road.wav";
            Decoder decoder = new Decoder();
            decoder.decode(inFileName, outFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

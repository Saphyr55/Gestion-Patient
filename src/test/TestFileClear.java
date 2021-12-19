package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import hopital.Hopital;

public class TestFileClear {

    public static void main(String[] args) {
        BufferedReader readerRememberme = new BufferedReader(Hopital.getRemembermeReaderFile());
        OutputStreamWriter writerRememberme = new OutputStreamWriter(Hopital.getRemembermeFileWriter(),
                StandardCharsets.UTF_8);
        PrintWriter printWriterRememberme = new PrintWriter(Hopital.getRemembermeFileWriter());
        String line;
        String string;
        String result = "";
        try {
            while ((line = readerRememberme.readLine()) != null) {
                string = line;
                result = string;
                System.out.println(string);
            }
            result = result.replaceAll("dadadada", "");
            writerRememberme.append("");
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                readerRememberme.close();
                writerRememberme.close();
                printWriterRememberme.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package hopital.personnels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import hopital.Hopital;

public class Technician extends Personnel {

    private int nLine = 1;

    public Technician(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        try {
            BufferedReader reader = new BufferedReader(Hopital.getTechnicianReaderFile());
            while ((reader.readLine()) != null) {
                nLine++;
            }
            reader.close();
            setIdentifiant(300000 + nLine);
            OutputStreamWriter writer = new OutputStreamWriter(Hopital.getTechnicianWriterFile(),
                    StandardCharsets.UTF_8);
            writer.write(nLine + "&" + getIdentifiant() + "&" + getFirstName() + "&" + getLastName() + "&" + getEmail()
                    + "&" + getPassword() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public Technician(int id, String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        super.setIdentifiant(id);
        Hopital.getTechnicians().add(this);
    }

}

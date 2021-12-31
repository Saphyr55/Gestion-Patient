package hopital.loading;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Classe qui permet lire le fichier rememberme.json
 * 
 * @author Andy
 */
public class LoadingRememberMe {

    private JSONParser parser;
    private JSONObject jsonObject;
    private static final String encoding = "UTF-8";

    public LoadingRememberMe() {
        parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                "./src/log/rememberme/rememberme.json"), encoding))) {
            jsonObject = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the jsonObject
     */
    public JSONObject getJsonObject() {
        return jsonObject;
    }
}

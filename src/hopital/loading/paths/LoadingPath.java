/**
 * 
 */
package hopital.loading.paths;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import hopital.loading.language.Language;

/**
 * @author Utilisateur
 *
 */
public class LoadingPath {
	
	private JSONParser parser;
	private JSONObject jsonObject;
	private static final String encoding = "UTF-8";
	
	public LoadingPath() {
        parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
        		"./src/ressources/paths/paths.json"), encoding))) 
        {
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

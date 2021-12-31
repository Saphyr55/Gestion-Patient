package hopital.loading.language;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Permet de lire le fichier langue strings.json
 * 
 * @author Andy
 */
public class LoadingLanguage {

	private JSONParser parser;
	private JSONObject jsonObject;
	public static final String encoding = "UTF-8";

	/**
	 * Consustruteur permettant de lire fichier strings avec le language
	 * correspondant
	 * 
	 * @param language
	 */
	public LoadingLanguage(Language language) {
		if (language == null)
			language = Language.FR;
		parser = new JSONParser();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				"./src/ressources/languages/" + language.toString().toLowerCase() + "/strings.json"), encoding))) {
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

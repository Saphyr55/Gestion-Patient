package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * 
 * 
 */
public class TestFileClear {

    public static void main(String[] args) {

        JSONObject object = new JSONObject();
        object.put("appareillage", true);
        Map<String, Boolean> list = new HashMap<>();
        list.put("String", false);
        list.put("dzda", false);
        list.put("dadazda", false);
        list.put("qasaq", false);
        list.put("saswwqq", false);
        object.putAll(list);

        try {
            File file = new File("./src/test.json");
            if (file.createNewFile())
                System.out.println("Creation réussi");
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true),
                    StandardCharsets.UTF_8);
            writer.write(object.toJSONString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

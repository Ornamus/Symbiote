package symbiote.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigFile {
    
    private final List<String> lines = new ArrayList<>();
    public String name;
    
    public ConfigFile(String path) {
        init(path, getClass());
    }
    
    public ConfigFile(String path, Class c) {
        init(path, c);
    }
    
    private void init(String path, Class c) {
        name = path;
        BufferedReader b = new BufferedReader(new InputStreamReader(c.getResourceAsStream(path)));
        while (true) {
            try {
                String s = b.readLine();
                if (s != null) {
                    lines.add(s);
                } else {
                    b.close();
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean exists(String value) {
        for (String s : lines) {
            if (s.contains("=") && s.contains(value)) {
                return true;
            }
        }
        return false;
    }
    
    public String getString(String value) {
        for (String s : lines) {
            if (s.contains("=")) {
                String[] split = s.split("=");
                if (split[0].equalsIgnoreCase(value)) {
                    return split[1];
                }
            }
        }
        return null;
    }
    
    public Integer getInt(String value) {
        String result = getString(value);
        if (result != null) {
            return Integer.parseInt(result);
        } else {
            return null;
        }
    }
    
    public List<String[]> getValues() {
        List<String[]> values = new ArrayList<>();
        for (String s : lines) {
            if (s.contains("=")) {
                values.add(s.split("="));
            }
        }
        return values;
    }
}

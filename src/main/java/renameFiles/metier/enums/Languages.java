package renameFiles.metier.enums;

import java.util.ArrayList;
import java.util.Collections;

public enum Languages
{
    FRENCH  ("vostfr", "fre", "french"  , "fra", "francais", "français",  "vf" ),
    ENGLISH ("vosten", "eng", "english" ),
    JAPANESE("vostjp", "jpn", "japanese", "jp", "jap" ),
    MULTI   ("multi");


    private final String[] values;

    Languages(String... values)
    {
        this.values = values;
    }

    public String[] getValues()
    {
        return this.values;
    }

    public static String[] getAllValues()
    {
        ArrayList<String> listValues = new ArrayList<>();

        for (Languages l : Languages.values())
            Collections.addAll(listValues, l.getValues());

        return listValues.toArray(new String[0]);
    }
}

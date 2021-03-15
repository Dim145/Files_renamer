package renameFiles.metier.resources;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager
{
    private static ResourceManager instance;

    public static ResourceManager getInstance()
    {
        if( instance == null ) instance = new ResourceManager();

        return instance;
    }

    private Locale locale;
    private ResourceBundle ihmStringBundle;

    private final ArrayList<Traduisible> listToTranlate;

    public ResourceManager()
    {
        this.listToTranlate = new ArrayList<>();

        this.setLocale(Locale.getDefault());
    }

    public void setLocale( Locale locale )
    {
        if( locale == null ) return;

        this.locale          = locale;
        this.ihmStringBundle = ResourceBundle.getBundle("ihmStrings", locale);

        this.tranlate();
    }

    public Locale getLocale()
    {
        return this.locale;
    }

    public String getString(String key)
    {
        return this.ihmStringBundle.getString(key);
    }

    public String getString(Resources res)
    {
        return this.getString(res.getKey());
    }

    public boolean addObjectToTranslate( Traduisible l )
    {
        return this.listToTranlate.add(l);
    }

    public boolean removeObjectToTranlate( Traduisible l )
    {
        return this.listToTranlate.remove(l);
    }
    
    private void tranlate()
    {
        for (Traduisible l : this.listToTranlate)
            l.setNewText();
    }
}

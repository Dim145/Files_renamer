package renameFiles.metier.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager
{
    private static PropertiesManager instance;

    public static PropertiesManager getInstance() throws IOException
    {
        if( instance == null ) instance = new PropertiesManager();

        return instance;
    }

    private final Properties properties;
    private final File       propertiesFile;

    private PropertiesManager() throws IOException
    {
        File baseRep        = new File(System.getProperty("user.home") + "/.FileRenamer");
        this.propertiesFile = new File(baseRep.getPath() + "/configs.xml");

        this.properties = new Properties();

        boolean isExist = baseRep.exists();

        if( !isExist )
            isExist = baseRep.mkdir();

        if( !isExist )
            throw new IOException("file .FileRenamer not created");

        isExist = propertiesFile.exists();

        if( isExist ) properties.loadFromXML(new FileInputStream(propertiesFile));
        else          isExist = propertiesFile.createNewFile();

        if( !isExist )
        {
            throw new IOException("fichier inexistant et impossible a creer: " + propertiesFile.getPath());
        }
    }

    public String getPropertie( String propertieName )
    {
        return this.properties.getProperty(propertieName, null);
    }

    public void setPropertie( String propertieName, String value )
    {
        this.properties.setProperty(propertieName, value);
    }

    public void savePropertiesToXML()
    {
        try
        {
            this.properties.storeToXML(new FileOutputStream(this.propertiesFile), "user configurations");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

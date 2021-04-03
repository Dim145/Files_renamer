package renameFiles;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.Metier;
import renameFiles.metier.enums.FileType;

import javax.swing.*;
import java.io.File;

/**
 * The type Controleur.
 */
public class Controleur
{
    private final Metier metier;
    private final IHMGUI ihm;

    /**
     * Instantiates a new Controleur.
     */
    public Controleur()
    {
        this.metier = new Metier(this);
        this.metier.setLanguesByPrefFile();

        this.ihm    = new IHMGUI(this);

        this.metier.setupPreferenceFile();

        this.ihm.setNbSDL(this.metier.getLevelMax()+1);

        this.ihm.setVisible(true);
    }

    public Controleur( FileType type )
    {
        this();

        if( type != null )
            this.ihm.setTypeCourant(type);
    }

    public void savePreferences(String key, String value)
    {
        this.metier.savePreferences(key, value);
    }

    /**
     * Rename file.
     *
     * @param path                  the path
     * @param patern                the patern
     * @param replaceAllPointInName the replace all point in name
     */
    public void renameFile( String path, String patern, boolean replaceAllPointInName, boolean qualiterTextuel )
    {
        this.metier.renameWithPaterneInPath(path, patern, replaceAllPointInName, qualiterTextuel);
    }

    /**
     * Sets current path.
     *
     * @param path the path
     */
    public void setCurrentPath(String path)
    {
        File file = new File(path);

        if( file.isDirectory() )
            this.ihm.setCurrentPath(file.getAbsolutePath());
        else if( file.isFile() )
            this.ihm.setCurrentPath(file.getParent());
        else if( !file.exists() )
            System.out.println("Error file inconnue: " + path);
    }

    /**
     * Sets extensions.
     *
     * @param text the text
     */
    public void setExtensions(String text)
    {
        this.metier.setAcceptedExtensions(text);
    }

    /**
     * Print console.
     *
     * @param s the s
     */
    public void printConsole(String s)
    {
        this.ihm.printInConsole(s);
    }

    /**
     * Change block param.
     *
     * @param b the b
     */
    public void changeBlockParam(boolean b)
    {
        this.metier.setBlockIfNotMathPatern(b);
    }

    public void setActiveWeb( boolean b )
    {
        this.metier.setActiveWeb(b);
    }

    /**
     * Sets type courant.
     *
     * @param type the type
     */
    public void setTypeCourant(FileType type )
    {
        this.metier.setTypeCourant(type);
    }

    /**
     * Sets save nb if exist.
     *
     * @param selected the selected
     */
    public void setSaveNbIfExist(boolean selected)
    {
        this.metier.setSaveNbIfExistInAlea(selected);
    }

    /**
     * Sets sdl.
     *
     * @param parseInt the parse int
     */
    public void setSDL(int parseInt)
    {
        this.metier.setMaxLevel(parseInt);
    }

    /**
     * Gets level max.
     *
     * @return the level max
     */
    public int getLevelMax()
    {
        return this.metier.getLevelMax();
    }

    public void setIHMValueFirstTime(String name, String valueToParse)
    {
        if( this.ihm.isVisible() ) return;

        try
        {
            if( Metier.tabPreferences[2].equals(name) )
            {
                this.setSDL(Integer.parseInt(valueToParse));
            }
            else
            {
                this.ihm.setIHMValueFirstTime(name, Boolean.parseBoolean(valueToParse));
            }
        }
        catch (Exception ignored){}
    }

    public void addTextToTampon(String text, boolean pushToConsole)
    {
        this.ihm.addTextToTampon(text, pushToConsole);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        System.setProperty("file.encoding", "UTF-8");

        if ( !UIManager.getSystemLookAndFeelClassName().equals( "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" ) )
            try   { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
            catch ( Exception ignored) {} // Theme de l'app, ici, celle du system sur lequel est lancer l'app

        FileType t = null;
        if ( args != null && args.length > 1 )
            for (FileType type : FileType.values())
                if( type.name().equalsIgnoreCase(args[1]))
                    t = type;

        Controleur ctrl = new Controleur(t);

        if( args != null && args.length > 0)
            ctrl.setCurrentPath(args[0]);
    }
}

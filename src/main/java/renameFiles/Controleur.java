package renameFiles;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.FileType;
import renameFiles.metier.Metier;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

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
        this.ihm    = new IHMGUI(this);

        this.metier.setupPreferenceFile();

        this.ihm.setNbSDL(this.metier.getLevelMax()+1);

        this.ihm.setVisible(true);
    }

    /**
     * Save preferences.
     *
     * @param prefs     the prefs
     * @param clearFile the clear file
     */
    public void savePreferences(HashMap<String, Object> prefs, boolean clearFile )
    {
        this.metier.savePreferences(prefs, clearFile);
    }

    /**
     * Rename file.
     *
     * @param path                  the path
     * @param patern                the patern
     * @param replaceAllPointInName the replace all point in name
     */
    public void renameFile( String path, String patern, boolean replaceAllPointInName )
    {
        this.metier.renameWithPaterneInPath(path, patern, replaceAllPointInName);
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

    /**
     * Sets block if not math patern.
     *
     * @param b the b
     */
    public void setBlockIfNotMathPatern(boolean b)
    {
        this.ihm.changeBlockParam(b);
    }

    /**
     * Sets dark mode.
     *
     * @param aBoolean the a boolean
     */
    public void setDarkMode(boolean aBoolean)
    {
        this.ihm.changeTheme(aBoolean);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        if ( !UIManager.getSystemLookAndFeelClassName().equals( "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" ) )
            try   { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
            catch ( Exception ignored) {} // Theme de l'app, ici, celle du system sur lequel est lancer l'app

        Controleur ctrl = new Controleur();

        if( args != null && args.length > 0)
            ctrl.setCurrentPath(args[0]);
    }
}
package renameFiles;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.FileType;
import renameFiles.metier.Metier;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class Controleur
{
    private final Metier metier;
    private final IHMGUI ihm;

    public Controleur()
    {
        this.metier = new Metier(this);
        this.ihm    = new IHMGUI(this);

        this.metier.setupPreferenceFile();

        this.ihm.setNbSDL(this.metier.getLevelMax()+1);

        this.ihm.setVisible(true);
    }

    public void savePreferences(HashMap<String, Object> prefs, boolean clearFile )
    {
        this.metier.savePreferences(prefs, clearFile);
    }

    public void renameFile( String path, String patern, boolean replaceAllPointInName )
    {
        this.metier.renameWithPaterneInPath(path, patern, replaceAllPointInName);
    }

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

    public void setExtensions(String text)
    {
        this.metier.setAcceptedExtensions(text);
    }

    public void printConsole(String s)
    {
        this.ihm.printInConsole(s);
    }

    public void changeBlockParam(boolean b)
    {
        this.metier.setBlockIfNotMathPatern(b);
    }

    public void setTypeCourant(FileType type )
    {
        this.metier.setTypeCourant(type);
    }

    public void setSaveNbIfExist(boolean selected)
    {
        this.metier.setSaveNbIfExistInAlea(selected);
    }

    public void setSDL(int parseInt)
    {
        this.metier.setMaxLevel(parseInt);
    }

    public int getLevelMax()
    {
        return this.metier.getLevelMax();
    }

    public void setBlockIfNotMathPatern(boolean b)
    {
        this.ihm.changeBlockParam(b);
    }

    public void setDarkMode(boolean aBoolean)
    {
        this.ihm.changeTheme(aBoolean);
    }

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

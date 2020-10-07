package renameFiles;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.Metier;

import javax.swing.*;
import java.io.File;

public class Controleur
{
    private final Metier metier;
    private final IHMGUI ihm;

    public Controleur()
    {
        this.metier = new Metier(this);
        this.ihm    = new IHMGUI(this);
    }

    public void renameFile( String path, String patern )
    {
        this.metier.renameWithPaterneInPath(path, patern);
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

    public void setBlockIfNotMathPatern(boolean b)
    {
        this.metier.setBlockIfNotMathPatern(b);
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

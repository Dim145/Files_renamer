package renameFiles;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.Metier;

import java.io.File;

public class Controleur
{
    private final Metier metier;
    private final IHMGUI ihm;

    public Controleur()
    {
        this.metier = new Metier(this);
        this.ihm     = new IHMGUI(this);
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

    public static void main(String[] args)
    {
        Controleur ctrl = new Controleur();

        if( args != null && args.length > 0)
            ctrl.setCurrentPath(args[0]);
    }

    public void setExtensions(String text)
    {
        this.metier.setAcceptedExtensions(text);
    }

    public void printConsole(String s)
    {
        this.ihm.printInConsole(s);
    }
}

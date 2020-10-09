package renameFiles;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.Metier;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Controleur
{
    private final Metier metier;
    private final IHMGUI ihm;

    private File prefFile;

    public Controleur()
    {
        this.metier = new Metier(this);
        this.ihm    = new IHMGUI(this);

        File appDirectorie = new File(System.getProperty("user.home") + "/.FileRenamer");

        if( !appDirectorie.exists() )
            if( appDirectorie.mkdir() ) this.ihm.printInConsole("File \".FileRenamer\" created.");
            else                        this.ihm.printInConsole("<font color=\"red\">Error, cannot create the app file in user directory</font>");

        this.prefFile = new File(appDirectorie.getAbsolutePath() + "/.preferences.conf");

        if( this.prefFile.exists())
        {
            try(Scanner scanner = new Scanner(this.prefFile))
            {
                int cpt = 0;
                while( scanner.hasNext() )
                {
                    String line = scanner.nextLine();
                    String[] tab = line.split("=");

                    if( tab.length < 2 ) continue;

                    switch (cpt)
                    {
                        case 0 : try
                        {
                            if(Boolean.parseBoolean(tab[1]))
                                this.ihm.changeBlockParam();
                        }
                        catch (Exception ignored) { }
                        break;

                        case 1 : try
                        {
                            boolean darkTheme = Boolean.parseBoolean(tab[1]);

                            if( darkTheme ) this.ihm.changeTheme();
                        }
                        catch ( Exception ignored) { }
                    }

                    cpt++;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                if( this.prefFile.createNewFile() )
                {
                    this.ihm.printInConsole("File preferences created");
                }
                else
                {
                    this.ihm.printInConsole("<font color=\"red\">Error, cannot create the preferences file</font>");
                }

                if( this.prefFile.exists() )
                {
                    this.saveBooleanPreferences("ignoreRenameProtection", false, true);
                    this.saveBooleanPreferences("darkMode", false, false);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        this.ihm.setVisible(true);
    }

    private String readAllFile( File file )
    {
        StringBuilder res = new StringBuilder();

        try(Scanner scanner = new Scanner(file))
        {
            scanner.reset();

            while( scanner.hasNext() )
                res.append(scanner.nextLine()).append("\n");
        }
        catch (Exception e) { e.printStackTrace(); }

        return res.toString();
    }

    public void saveBooleanPreferences( String name, boolean value, boolean clearFile )
    {
        String fileValue = "";
        if( !clearFile ) fileValue = this.readAllFile(this.prefFile);

        try(FileWriter writer = new FileWriter(this.prefFile))
        {
            writer.append(fileValue).append(name).append("=").append(String.valueOf(value)).append("\n");
        }
        catch (Exception e)
        {
            this.ihm.printInConsole("<font color=\"red\">Error in file writting");
            e.printStackTrace();
        }
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

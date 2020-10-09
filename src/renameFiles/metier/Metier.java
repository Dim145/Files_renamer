package renameFiles.metier;

import renameFiles.Controleur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Metier
{
    private final ArrayList<File> files;
    private final Controleur  ctrl;
    private final List<String> acceptedExtension;
    private boolean blockIfNotMathPatern;

    public Metier( Controleur ctrl )
    {
        this.files             = new ArrayList<>();
        this.acceptedExtension = new ArrayList<>();
        this.ctrl              = ctrl;

        this.blockIfNotMathPatern = true;
    }

    public void setAcceptedExtensions( String extensions )
    {
        if( extensions == null || extensions.length() < 1) return;

        this.acceptedExtension.clear();

        for( String s : extensions.split(",") )
        {
            if( s.isEmpty() || " ".equals(s) ) continue;

            if( s.startsWith(".") ) s = s.substring(1);

            this.acceptedExtension.add(s.trim());
        }
    }

    public void renameWithPaterneInPath( String path, String patern )
    {
        if( patern == null || patern.length() < 1 || !patern.contains("%%") )
        {
            this.ctrl.printConsole("<font color=\"red\">Saisissez un paterne avec au moins 1 fois \"%%\"</font>");

            return;
        }

        File repRacine = new File(path);

        this.files.clear();
        this.readRepertory(repRacine, 0);

        if( this.files.size() == 0 )
        {
            this.ctrl.printConsole("<font color=\"red\">Aucun fichier avec les extensions saisi n'ont été trouvés</font>");
            return;
        }

        for (File file : this.files)
        {
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            String extension = file.getName().substring(file.getName().lastIndexOf("."));
            ArrayList<Integer> listNombre = new ArrayList<>();

            for (int cpt = 0; cpt < fileName.length(); cpt++)
            {
                char lettre = fileName.charAt(cpt);

                if( Character.isDigit(lettre) )
                {
                    StringBuilder tmp = new StringBuilder("" + lettre);

                    lettre = ++cpt < fileName.length() ? fileName.charAt(cpt) : 'a';

                    while( Character.isDigit(lettre) )
                    {
                        tmp.append(lettre);

                        if( cpt < fileName.length()-1)
                            lettre = fileName.charAt(++cpt);
                        else
                            break;
                    }

                    try
                    {
                        listNombre.add(Integer.parseInt(tmp.toString()));
                    }
                    catch (Exception e)
                    {
                        this.ctrl.printConsole("<font color=\"red\">Error in conversion of string: " + tmp + "</font>");
                    }
                }
            }

            String newName = patern + extension;

            int cptPatern = 0;

            for (int i = 0; i < patern.length()-1; i++)
                if( "%%".equals( ("" + patern.charAt(i)) + patern.charAt(i+1) ) ) cptPatern++;

            if( this.blockIfNotMathPatern && cptPatern != listNombre.size() )
            {
                this.ctrl.printConsole( fileName + ": <font color=\"red\">Il n'y as pas le même nombre de chiffres dans le nom que de \"%%\" dans le patern</font>");
                continue;
            }

            for ( int i : listNombre )
                newName = newName.replaceFirst("%%", String.format("%02d", i));

            if( file.renameTo(new File(file.getParent() + "/" + newName)) )
                this.ctrl.printConsole("file: " + fileName + extension + " -> <font color=\"rgb(0, 255, 255)\">" + newName + "</font>");
            else
                this.ctrl.printConsole("<font color=\"red\">file: " + fileName + " not renamed</font>");
        }
    }

    private void readRepertory( final File rep, int level )
    {
        if( rep.isFile() || rep.getName().equals("renamed") ) return;

        if( level < 0) level = 0;

        File[] temp = rep.listFiles();

        if( temp == null ) return;

        for (File file : temp)
            if (file.isFile())
            {
                if (this.accept(file.getName())) this.files.add(file);
            }
            else if (file.isDirectory() && level <= 1 )
            {
                readRepertory(file, ++level);
            }
    }

    public boolean accept(String name)
    {
        if( this.acceptedExtension.size() > 0 )
            for( String s : this.acceptedExtension )
                if( name.endsWith("." + s))
                    return true;

        return false;
    }

    public void setBlockIfNotMathPatern(boolean b)
    {
        this.blockIfNotMathPatern = b;
    }
}

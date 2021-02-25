package renameFiles.metier;

import renameFiles.Controleur;
import renameFiles.metier.types.BaseFile;
import renameFiles.metier.types.VideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

    public boolean setAcceptedExtensions( String extensions )
    {
        if( extensions == null || extensions.length() < 1) return false;

        this.acceptedExtension.clear();

        for( String s : extensions.split(",") )
        {
            if( s.isEmpty() || " ".equals(s) ) continue;

            if( s.startsWith(".") ) s = s.substring(1);

            this.acceptedExtension.add(s.trim());
        }

        return this.acceptedExtension.size() > 0;
    }

    public void renameWithPaterneInPath( String path, String patern )
    {
        if( patern == null || patern.length() < 1 )
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

        int nbRound  = String.valueOf((int) this.detectMaxEp()).length();

        for (File file : this.files)
        {
            BaseFile baseFile = null;
            String extension = file.getName().substring(file.getName().lastIndexOf("."));
            String fileName  = file.getName().substring(0, file.getName().lastIndexOf("."));

            if (Arrays.asList(VideoFile.extensions).contains(extension.startsWith(".") ? extension.substring(1) : extension))
                baseFile = VideoFile.getVideoFileFromFile(file);

            if(baseFile != null)
            {
                VideoFile video = (VideoFile) baseFile;
                video.setFullFormatedName(patern);
                video.setName();
            }
            else
            {
                baseFile = new BaseFile(fileName, extension);

                baseFile.remplirListeNombre();
                baseFile.setFullFormatedName(patern);
                baseFile.replaceFullFormatedName();
            }

            if( file.renameTo(new File(file.getParent() + "/" + baseFile.toString())) )
                this.ctrl.printConsole("file: " + fileName + extension + " -> <font color=\"rgb(0, 255, 255)\">" + baseFile.toString() + "</font>");
            else
                this.ctrl.printConsole("<font color=\"red\">file: " + fileName + " not renamed</font>");
        }
    }

    private double detectMaxEp()
    {
        double max = 0;

        for (File file : this.files)
        {
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            ArrayList<Double> listNombre = new ArrayList<>();

            int emplacementEp = -1;



            for (int i = 0; i < listNombre.size(); i++)
                if( emplacementEp == i )
                    if( listNombre.get(i) > max )
                        max = listNombre.get(i);
        }

        return max;
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

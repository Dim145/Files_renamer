package renameFiles.metier;

import renameFiles.Controleur;
import renameFiles.metier.types.BaseFile;
import renameFiles.metier.types.videos.Saison;
import renameFiles.metier.types.videos.VideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Metier
{
    private final ArrayList<File> files;
    private final Controleur  ctrl;
    private final List<String> acceptedExtension;
    private boolean blockIfNotMathPatern;

    private FileType typeCourant;

    public Metier(Controleur ctrl )
    {
        this.files             = new ArrayList<>();
        this.acceptedExtension = new ArrayList<>();
        this.ctrl              = ctrl;

        this.typeCourant          = FileType.AUTRES;
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

    public void renameWithPaterneInPath( String path, String patern, boolean replaceAllPointInName )
    {
        if( this.typeCourant != FileType.VIDEOS && (patern == null || patern.length() < 1 || !patern.contains("%%")) )
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

        ArrayList<Saison> listSaison = new ArrayList<>();

        for (File file : this.files)
        {
            String fileName  = file.getName().substring(0, file.getName().lastIndexOf("."));

            if( replaceAllPointInName )
                fileName = fileName.replaceAll("\\.", " ");

            if( this.typeCourant == FileType.VIDEOS)
            {
                VideoFile video = VideoFile.getVideoFileFromFile(file);

                if( video == null )
                {
                    this.ctrl.printConsole("<font color=\"red\">file: " + fileName + " is not a video</font>");
                    return;
                }

                video.setFullFormatedName(patern);
                video.setName();

                Saison s = new Saison(video.getName(), video.getNumeroSaison());

                int index = listSaison.indexOf(s);

                if ( index == -1 )
                {
                    listSaison.add(s);
                    index = listSaison.size()-1;
                }

                listSaison.get(index).ajouterEpisode(video);
            }
            else
            {

                String extension  = file.getName().substring(file.getName().lastIndexOf("."));
                BaseFile baseFile = new BaseFile(fileName, extension, file);

                baseFile.remplirListeNombre();
                baseFile.setFullFormatedName(patern);

                if( !baseFile.replaceFullFormatedName(blockIfNotMathPatern) )
                {
                    this.ctrl.printConsole("<font color=\"red\">file: " + fileName + " ne contient pas le meme nombres de %% que le patern.</font>");
                    continue;
                }

                if( file.renameTo(new File(file.getParent() + "/" + baseFile.toString())) )
                    this.ctrl.printConsole("file: " + fileName + extension + " -> <font color=\"rgb(0, 255, 255)\">" + baseFile.toString() + "</font>");
                else
                    this.ctrl.printConsole("<font color=\"red\">file: " + fileName + " not renamed</font>");
            }
        }

        if ( this.typeCourant == FileType.VIDEOS)
        {
            for (Saison s : listSaison)
            {
                s.setRoundAllApisode();

                for (VideoFile video : s.getAllEpisodes())
                {
                    File file = video.getFile();

                    if( file.renameTo(new File(file.getParent() + "/" + video.toString())) )
                        this.ctrl.printConsole("file: " + file.getName() + " -> <font color=\"rgb(0, 255, 255)\">" + video.toString() + "</font>");
                    else
                        this.ctrl.printConsole("<font color=\"red\">file: " + video.getName() + " not renamed</font>");
                }
            }
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

    public FileType getTypeCourant()
    {
        return typeCourant;
    }

    public void setTypeCourant(FileType typeCourant)
    {
        this.typeCourant = typeCourant;
    }
}

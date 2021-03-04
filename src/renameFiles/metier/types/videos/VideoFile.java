package renameFiles.metier.types.videos;

import org.jetbrains.annotations.NotNull;
import renameFiles.metier.types.BaseFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoFile extends BaseFile
{
    public static final String[] extensions = {"mp4", "mkv", "avi"};

    private double numeroEpisode;
    private int    numeroSaison;

    private int qualiter;
    private int compression;

    private int nbMaxEpisode;

    public VideoFile(String name, String extension)
    {
        this(name, extension, -1, -1, null);
    }

    public VideoFile(String name, String extension, File file)
    {
        this(name, extension, -1, -1, file);
    }

    public VideoFile(String name)
    {
        this(name, null);
    }

    public VideoFile(String name, String extension, int numeroEp, int numeroSaison, File file)
    {
        this(name, extension, numeroEp, numeroSaison, -1, file);
    }

    public VideoFile(String name, String extension, int numeroEp, int numeroSaison, int qualiter, File file)
    {
        super(name, extension, file);

        this.numeroEpisode = numeroEp;
        this.numeroSaison  = numeroSaison;
        this.qualiter      = qualiter;

        this.nbMaxEpisode  = -1;
        this.compression   = -1;
    }

    public double getNumeroEpisode()
    {
        return numeroEpisode;
    }

    public void setNumeroEpisode(double numeroEpisode)
    {
        this.numeroEpisode = numeroEpisode;
    }

    public int getNumeroSaison()
    {
        return numeroSaison;
    }

    public void setNumeroSaison(int numeroSaison)
    {
        this.numeroSaison = numeroSaison;
    }

    public int getQualiter()
    {
        return qualiter;
    }

    public void setQualiter(int qualiter)
    {
        this.qualiter = qualiter;
    }

    public int getCompression()
    {
        return compression;
    }

    public void setCompression(int compression)
    {
        this.compression = compression;
    }

    public static VideoFile getVideoFileFromFile(@NotNull File file )
    {
        int indexOfPoint = file.getName().lastIndexOf(".");

        String fileName  = file.getName().substring(0, indexOfPoint);
        String extension = file.getName().substring(indexOfPoint);

        if (!Arrays.asList(VideoFile.extensions).contains(extension.startsWith(".") ? extension.substring(1) : extension))
            return null;

        VideoFile video = new VideoFile(fileName, extension, file);

        for (int cpt = 0; cpt < fileName.length(); cpt++)
        {
            char lettre = fileName.charAt(cpt);

            if( Character.isDigit(lettre) )
            {
                StringBuilder tmp = new StringBuilder("" + lettre);

                lettre = ++cpt < fileName.length() ? fileName.charAt(cpt) : 'a';

                while( Character.isDigit(lettre) || lettre == '.' || lettre == ',' )
                {
                    tmp.append(lettre);

                    if( cpt < fileName.length()-1)
                        lettre = fileName.charAt(++cpt);
                    else
                        break;
                }

                if( tmp.toString().endsWith(".") || tmp.toString().endsWith(",") )
                    tmp.delete(tmp.length()-1, tmp.length());

                try
                {
                    video.addNombre(Double.parseDouble(tmp.toString().replaceAll(",", ".")));

                    if( cpt < fileName.length() && fileName.toLowerCase().charAt(cpt) == 'p' || cpt+1 < fileName.length() && fileName.toLowerCase().charAt(cpt+1) == 'p' )
                    {
                        video.setQualiter((int) video.getNombre(video.getNbNombres()-1));
                        continue;
                    }

                    int cpt2 = cpt - (tmp.length());
                    tmp.delete(0, tmp.length());

                    boolean oneWhiteSpace = true;
                    do
                    {
                        lettre = fileName.charAt(cpt2--);

                        if( lettre == ' ' )
                        {
                            if( tmp.length() > 0 )
                                oneWhiteSpace = false;

                            continue;
                        }

                        if( !Character.isDigit(lettre) )
                            tmp.append(lettre);
                    }
                    while( Character.isDigit(lettre) || oneWhiteSpace );

                    String texteAvNb = tmp.reverse().toString().trim().toLowerCase();

                    if( texteAvNb.equals("e") || texteAvNb.equals("ep") || texteAvNb.equals("episode") || texteAvNb.equals("épisode") )
                        video.setNumeroEpisode(video.getNombre(video.getNbNombres()-1));

                    if( texteAvNb.equals("s") || texteAvNb.equals("saison") ) // Todo améliorer la reconnaissance
                        if ( video.getNumeroSaison() != -1 ) video.setNumeroEpisode((int) video.getNombre(video.getNbNombres()-1));
                        else                                 video.setNumeroSaison ((int) video.getNombre(video.getNbNombres()-1));

                    if( texteAvNb.equals("x") )
                        video.setCompression((int) video.getNombre(video.getNbNombres()-1));
                }
                catch (Exception ignored)
                { }
            }
        }

        return video;
    }

    @Override
    public String toString()
    {
        if( this.name != null && !this.name.isEmpty())
        {
            String name = this.name + " ";

            int nbRound = Math.max(this.nbMaxEpisode, 2);

            if( this.numeroSaison  > -1 ) name += "S"  + this.numeroSaison  + " ";
            if( this.numeroEpisode > -1 ) name += "Ep" + (this.numeroEpisode % 1 == 0 ? String.format("%0"+String.valueOf(nbRound).length()+"d", (int)this.numeroEpisode) : String.format(
                    "%0"+String.valueOf(nbRound).length()+".2f", this.numeroEpisode)) + " ";
            if( this.qualiter      > -1 ) name += this.qualiter + "p ";
            if( this.compression   > -1 ) name += "x"  + this.compression   + " ";

            return name.trim() + this.extension;
        }

        return this.fullname + this.extension;
    }

    public void setName()
    {
        String nameToUse = this.fullname;

        if( this.fullFormatedName != null && !this.fullFormatedName.isEmpty() )
        {
            this.remplirListeNombre();
            this.replaceFullFormatedName(false);

            nameToUse = this.fullFormatedName;
        }

        if(nameToUse.chars().noneMatch(Character::isDigit))
        {
            this.name = nameToUse.trim();
            return;
        }

        ArrayList<Integer> listAllIndex = new ArrayList<>();

        for (int cpt = 0; cpt < nameToUse.length(); cpt++)
        {
            char lettre = nameToUse.charAt(cpt);

            if( Character.isDigit(lettre) )
            {
                StringBuilder tmp = new StringBuilder("" + lettre);

                lettre = ++cpt < nameToUse.length() ? nameToUse.charAt(cpt) : 'a';

                while( Character.isDigit(lettre) || lettre == '.' || lettre == ',' )
                {
                    tmp.append(lettre);

                    if( cpt < nameToUse.length()-1)
                        lettre = nameToUse.charAt(++cpt);
                    else
                        break;
                }

                if( tmp.toString().endsWith(".") || tmp.toString().endsWith(",") )
                    tmp.delete(tmp.length()-1, tmp.length());

                try
                {
                    int cpt2 = cpt - (tmp.length());
                    tmp.delete(0, tmp.length());

                    if( cpt < nameToUse.length() && nameToUse.toLowerCase().charAt(cpt) == 'p' || cpt+1 < nameToUse.length() && nameToUse.toLowerCase().charAt(cpt+1) == 'p' )
                    {
                        listAllIndex.add(cpt2-1);
                        continue;
                    }

                    boolean oneWhiteSpace = true;
                    do
                    {
                        lettre = nameToUse.charAt(cpt2--);

                        if( lettre == ' ' )
                        {
                            if( tmp.length() > 0 )
                                oneWhiteSpace = false;

                            continue;
                        }

                        if( !Character.isDigit(lettre) )
                            tmp.append(lettre);
                    }
                    while( Character.isDigit(lettre) || oneWhiteSpace );

                    String texteAvNb = tmp.reverse().toString().trim().toLowerCase();

                    if( texteAvNb.equals("e") || texteAvNb.equals("ep") || texteAvNb.equals("episode") || texteAvNb.equals("épisode") )
                        listAllIndex.add(cpt2);

                    if( texteAvNb.equals("s") || texteAvNb.equals("saison") )
                        listAllIndex.add(cpt2);

                    if( texteAvNb.equals("x") )
                        listAllIndex.add(cpt2);
                }
                catch (Exception ignored)
                { }
            }
        }

        this.name = nameToUse.substring(0, listAllIndex.stream().mapToInt(i -> i).min().getAsInt()+1).trim();
    }

    public int getNbMaxEpisode()
    {
        return nbMaxEpisode;
    }

    public void setNbMaxEpisode(int nbMaxEpisode)
    {
        this.nbMaxEpisode = nbMaxEpisode;
    }
}

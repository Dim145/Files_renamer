package renameFiles.metier.types;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

public class VideoFile extends BaseFile
{
    public static final String[] extensions = {"mp4", "mkv", "avi"};

    private double numeroEpisode;
    private int    numeroSaison;

    private int qualiter;
    private int compression;

    public VideoFile(String name, String extension)
    {
        this(name, extension, -1, -1);
    }

    public VideoFile(String name)
    {
        this(name, null);
    }

    public VideoFile(String name, String extension, int numeroEp, int numeroSaison)
    {
        this(name, extension, numeroEp, numeroSaison, -1);
    }

    public VideoFile(String name, String extension, int numeroEp, int numeroSaison, int qualiter)
    {
        super(name, extension);

        this.numeroEpisode = numeroEp;
        this.numeroSaison  = numeroSaison;
        this.qualiter      = qualiter;
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

        VideoFile video = new VideoFile(fileName, extension);

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

                    int cpt2 = cpt - (tmp.length());
                    tmp.delete(0, tmp.length());

                    boolean oneWhiteSpace = true;
                    do
                    {
                        lettre = fileName.charAt(cpt2--);

                        if( lettre == ' ' )
                        {
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

                    if( texteAvNb.equals("s") || texteAvNb.equals("saison") )
                        video.setNumeroSaison((int) video.getNombre(video.getNbNombres()-1));

                    if( texteAvNb.equals("x") )
                        video.setCompression((int) video.getNombre(video.getNbNombres()-1));

                    if( fileName.toLowerCase().charAt(cpt) == 'p' || fileName.toLowerCase().charAt(cpt+1) == 'p' )
                        video.setQualiter((int) video.getNombre(video.getNbNombres()-1));
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

            if( this.numeroSaison  > -1 ) name += "S"  + this.numeroSaison  + " ";
            if( this.numeroEpisode > -1 ) name += "Ep" + (this.numeroEpisode % 1 == 0 ? String.format("%02d", (int)this.numeroEpisode) : String.format(
                    "%02.2f", this.numeroEpisode)) + " ";
            if( this.qualiter      > -1 ) name += this.qualiter + "p ";
            if( this.compression   > -1 ) name += "x"  + this.compression   + " ";

            return name.trim() + this.extension;
        }

        return this.fullname + this.extension;
    }

    public void setName()
    {
        String nameToUse = this.fullname;

        if( this.fullFormatedName != null )
        {
            this.remplirListeNombre();
            this.replaceFullFormatedName();

            nameToUse = this.fullFormatedName;
        }

        if(nameToUse.chars().noneMatch(Character::isDigit))
        {
            this.name = nameToUse.trim();
            return;
        }

        int indexEndName = 0;

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

                    boolean oneWhiteSpace = true;
                    do
                    {
                        lettre = nameToUse.charAt(cpt2--);

                        if( lettre == ' ' )
                        {
                            oneWhiteSpace = false;
                            continue;
                        }

                        if( !Character.isDigit(lettre))
                            tmp.append(lettre);
                    }
                    while( Character.isDigit(lettre) || oneWhiteSpace );

                    String texteAvNb = tmp.toString().trim().toLowerCase();

                    if( texteAvNb.equals("e") || texteAvNb.equals("ep") || texteAvNb.equals("episode") || texteAvNb.equals("épisode") )
                        indexEndName = cpt2;

                    if( texteAvNb.equals("s") || texteAvNb.equals("saison") )
                    {
                        indexEndName = cpt2;
                        break;
                    }
                }
                catch (Exception ignored)
                { }
            }
        }

        this.name = nameToUse.substring(0, indexEndName+1);
    }
}

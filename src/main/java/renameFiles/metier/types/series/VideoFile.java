package renameFiles.metier.types.series;

import org.jetbrains.annotations.NotNull;
import renameFiles.metier.enums.Definitions;
import renameFiles.metier.types.BaseFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Video file.
 */
public class VideoFile extends BaseFile
{
    /**
     * The constant extensions.
     */
    public static final String[] extensions    = {"mp4", "mkv", "avi"   , "mov"   , "mpeg", "mpg"   , "wmv"    };
    public static final String[] listLanguages = {"vf" , "fr" , "vostfr", "french", "en"  , "vosten", "english"};

    private double numeroEpisode;
    private int    numeroSaison;

    private int qualiter;
    private int compression;

    private int nbMaxEpisode;

    private boolean isEpisodeSpecial;
    private boolean isOAV;

    private String language;

    /**
     * Instantiates a new Video file.
     *
     * @param name      the name
     * @param extension the extension
     */
    public VideoFile(String name, String extension)
    {
        this(name, extension, -1, -1, null);
    }

    /**
     * Instantiates a new Video file.
     *
     * @param name      the name
     * @param extension the extension
     * @param file      the file
     */
    public VideoFile(String name, String extension, File file)
    {
        this(name, extension, -1, -1, file);
    }

    /**
     * Instantiates a new Video file.
     *
     * @param name the name
     */
    public VideoFile(String name)
    {
        this(name, null);
    }

    /**
     * Instantiates a new Video file.
     *
     * @param name         the name
     * @param extension    the extension
     * @param numeroEp     the numero ep
     * @param numeroSaison the numero saison
     * @param file         the file
     */
    public VideoFile(String name, String extension, int numeroEp, int numeroSaison, File file)
    {
        this(name, extension, numeroEp, numeroSaison, -1, file);
    }

    /**
     * Instantiates a new Video file.
     *
     * @param name         the name
     * @param extension    the extension
     * @param numeroEp     the numero ep
     * @param numeroSaison the numero saison
     * @param qualiter     the qualiter
     * @param file         the file
     */
    public VideoFile(String name, String extension, int numeroEp, int numeroSaison, int qualiter, File file)
    {
        super(name, extension, file);

        this.numeroEpisode = numeroEp;
        this.numeroSaison  = numeroSaison;
        this.qualiter      = qualiter;

        this.nbMaxEpisode  = -1;
        this.compression   = -1;

        this.language = null;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * Gets numero episode.
     *
     * @return the numero episode
     */
    public double getNumeroEpisode()
    {
        return numeroEpisode;
    }

    /**
     * Sets numero episode.
     *
     * @param numeroEpisode the numero episode
     */
    public void setNumeroEpisode(double numeroEpisode)
    {
        this.numeroEpisode = numeroEpisode;
    }

    /**
     * Gets numero saison.
     *
     * @return the numero saison
     */
    public int getNumeroSaison()
    {
        return numeroSaison;
    }

    /**
     * Sets numero saison.
     *
     * @param numeroSaison the numero saison
     */
    public void setNumeroSaison(int numeroSaison)
    {
        this.numeroSaison = numeroSaison;
    }

    /**
     * Gets qualiter.
     *
     * @return the qualiter
     */
    public int getQualiter()
    {
        return qualiter;
    }

    /**
     * Sets qualiter.
     *
     * @param qualiter the qualiter
     */
    public void setQualiter(int qualiter)
    {
        this.qualiter = qualiter;
    }

    /**
     * Gets compression.
     *
     * @return the compression
     */
    public int getCompression()
    {
        return compression;
    }

    /**
     * Sets compression.
     *
     * @param compression the compression
     */
    public void setCompression(int compression)
    {
        this.compression = compression;
    }

    /**
     * Gets video file from file.
     *
     * @param file           the file
     * @param replaceAllPbyS the replace all pby s
     * @return the video file from file
     */
    public static VideoFile getVideoFileFromFile(@NotNull File file, boolean replaceAllPbyS )
    {
        int indexOfPoint = file.getName().lastIndexOf(".");

        String fileName  = file.getName().substring(0, indexOfPoint);
        String extension = file.getName().substring(indexOfPoint);

        if (!Arrays.asList(VideoFile.extensions).contains(extension.startsWith(".") ? extension.substring(1) : extension))
            return null;

        if( replaceAllPbyS )
            fileName = fileName.replaceAll("\\.", " ");

        VideoFile video = new VideoFile(fileName, extension, file);

        for (Definitions def : Definitions.values())
        {
            if( fileName.contains(" " + def.name()) || fileName.contains(def.name() + " ") )
            {
                video.setQualiter(def.getQualiter());
                break;
            }
        }

        String tmpFileName = fileName.toLowerCase();

        for (String s : VideoFile.listLanguages)
        {
            if( tmpFileName.contains(s) )
                video.setLanguage(s);
        }

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

                    if( texteAvNb.equals("sp") || texteAvNb.equals("spécial") || texteAvNb.equals("special") )
                    {
                        video.setNumeroEpisode(video.getNombre(video.getNbNombres()-1)); // c'est juste un changement de texte.
                        video.setEpisodeSpecial(true);
                    }

                    if( texteAvNb.equals("oav") )
                    {
                        video.setNumeroEpisode(video.getNombre(video.getNbNombres()-1)); // juste un changement de texte
                        video.setOAV(true);
                    }

                    if( texteAvNb.equals("") || texteAvNb.equals("-") ) // par default, on considere que c'est un episode
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

            int nbRound = Math.max(String.valueOf(this.nbMaxEpisode).length(), 2);

            if( this.numeroSaison  > -1 ) name += "S"  + this.numeroSaison  + " ";
            if( this.numeroEpisode > -1 ) name += (this.isEpisodeSpecial ? "Sp" : this.isOAV ? "OAV" : "Ep") + (this.numeroEpisode % 1 == 0 ? String.format("%0"+nbRound+"d", (int)this.numeroEpisode) : String.format(
                    "%0"+nbRound+".2f", this.numeroEpisode)) + " ";
            if( this.language != null   ) name += this.language + " ";
            if( this.qualiter      > -1 ) name += this.qualiter + "p ";
            if( this.compression   > -1 ) name += "x"  + this.compression   + " ";

            return name.trim() + this.extension;
        }

        return this.fullname + this.extension;
    }

    /**
     * Sets name.
     *
     * @param replaceAllPointInName the replace all point in name
     */
    public void setName(boolean replaceAllPointInName)
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

        if( nameToUse.startsWith("[") )
            nameToUse = nameToUse.substring(nameToUse.indexOf("]")+1).trim();

        if( replaceAllPointInName )
            nameToUse = nameToUse.replaceAll("\\.", " ");

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

                    if( Double.parseDouble(tmp.toString()) == this.getNbMaxEpisode() )
                        listAllIndex.add(cpt2);

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

        int min = listAllIndex.stream().mapToInt(i -> i).min().orElse(nameToUse.length()-1)+1;
        int indexSeparateurMoins = nameToUse.indexOf("-");

        if( indexSeparateurMoins > 0 && indexSeparateurMoins < min )
            min = indexSeparateurMoins;

        if( min == nameToUse.length() ) // dans le cas ou ni saison ni rien d'autre n'est données
        {
            for (int i = 0; i < nameToUse.length(); i++)
            {
                if( Character.isDigit(nameToUse.charAt(i)) )
                {
                    int tmp = min = i;

                    StringBuilder nb = new StringBuilder();
                    do
                    {
                        nb.append(nameToUse.charAt(tmp++));
                    }
                    while (Character.isDigit(nameToUse.charAt(tmp)) || nameToUse.charAt(tmp) == '.' || nameToUse.charAt(tmp) == ',' );

                    this.setNumeroEpisode(Double.parseDouble(nb.toString()));

                    break;
                }
            }
        }

        this.name = nameToUse.substring(0, min).trim();

        if( this.name.endsWith("(TV)") )
            this.name = this.name.substring(0, this.name.indexOf("(TV)")).trim();
    }

    /**
     * Gets nb max episode.
     *
     * @return the nb max episode
     */
    public int getNbMaxEpisode()
    {
        return nbMaxEpisode;
    }

    /**
     * Sets nb max episode.
     *
     * @param nbMaxEpisode the nb max episode
     */
    public void setNbMaxEpisode(int nbMaxEpisode)
    {
        this.nbMaxEpisode = nbMaxEpisode;
    }

    /**
     * Is episode special boolean.
     *
     * @return the boolean
     */
    public boolean isEpisodeSpecial()
    {
        return isEpisodeSpecial;
    }

    /**
     * Sets episode special.
     *
     * @param episodeSpecial the episode special
     */
    public void setEpisodeSpecial(boolean episodeSpecial)
    {
        isEpisodeSpecial = episodeSpecial;

        if( isOAV() && episodeSpecial )
            this.setOAV(false);
    }

    /**
     * Is oav boolean.
     *
     * @return the boolean
     */
    public boolean isOAV()
    {
        return isOAV;
    }

    /**
     * Sets oav.
     *
     * @param OAV the oav
     */
    public void setOAV(boolean OAV)
    {
        isOAV = OAV;

        if( this.isEpisodeSpecial() && OAV )
            this.setEpisodeSpecial(false);
    }
}

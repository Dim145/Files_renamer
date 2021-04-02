package renameFiles.metier.types.series;

import renameFiles.metier.enums.Definitions;
import renameFiles.metier.enums.Languages;
import renameFiles.metier.types.BaseFile;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Video file.
 */
public class Episode extends BaseFile
{
    /**
     * constant accepted extensions.
     */
    public static final String[] extensions    = {"mp4", "mkv"   , "avi"   , "mov"   , "mpeg"   , "mpg"   , "wmv"    };

    private double numeroEpisode;
    private int    numeroSaison;

    private int qualiter;
    private int compression;
    private int anneeSortie;
    private int nbBits;

    private int nbMaxEpisode;
    private int nbMaxSaison;

    private boolean isEpisodeSpecial;
    private boolean isOAV;
    private boolean isNC;

    private boolean prefDefLetter;

    private String language;
    private String title;

    /**
     * Instantiates a new Video file.
     *
     * @param name      the name
     * @param extension the extension
     */
    public Episode(String name, String extension)
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
    public Episode(String name, String extension, File file)
    {
        this(name, extension, -1, -1, file);
    }

    /**
     * Instantiates a new Video file.
     *
     * @param name the name
     */
    public Episode(String name)
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
    public Episode(String name, String extension, int numeroEp, int numeroSaison, File file)
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
    public Episode(String name, String extension, int numeroEp, int numeroSaison, int qualiter, File file)
    {
        super(name, extension, file);

        this.numeroEpisode = numeroEp;
        this.numeroSaison  = numeroSaison;
        this.qualiter      = qualiter;

        this.nbMaxSaison   = 1;
        this.nbMaxEpisode  = 1;
        this.compression   = -1;
        this.nbBits        = -1;
        this.anneeSortie   = -1;

        this.language = "";
    }

    public String getLanguage()
    {
        return language;
    }

    public void addLanguages(String language)
    {
        this.language += language + " ";
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

    @Override
    public String toString()
    {
        this.language = this.language.trim();

        if( this.name != null && !this.name.isEmpty())
        {
            Definitions def = Definitions.getByPixels(this.getQualiter());

            String name = this.name + " ";

            if( this.getAnneeSortie() > -1 )
                name += "(" + this.getAnneeSortie() + ") ";

            if( this.language.contains(" ") ) // "vostfr vf" -> "[vostfr,vf]"
                this.language = "[" + this.language.replaceAll(" ", "," ) + "]";

            int nbRoundEpisode = Math.max(String.valueOf( this.getNbMaxEpisode()).length(), 2);
            int nbRoundSaison  = Math.max(String.valueOf( this.nbMaxSaison      ).length(), 1);

            if( this.numeroSaison  > -1 ) name += "S"  + String.format("%0" + nbRoundSaison + "d", this.getNumeroSaison())  + " ";
            if( this.numeroEpisode > -1 ) name += (this.isEpisodeSpecial() ? "Sp" : this.isOAV() ? "OAV" : "Ep") + (this.getNumeroEpisode() % 1 == 0 ? String.format("%0"+nbRoundEpisode+"d", (int)this.getNumeroEpisode()) : String.format(
                    "%0"+nbRoundEpisode+".2f", this.getNumeroEpisode())) + " ";
            if( !this.language.isEmpty()) name += this.getLanguage() + " ";
            if( this.isNC()             ) name += "NC ";
            if( this.qualiter      > -1 ) name += ( def == null ? this.getQualiter() + "p" : (this.isPrefDefLetter() ? def : def.getQualiter() + "p") ) + " "; // def = null si pas dans les normes
            if( this.nbBits        > -1 ) name += this.getNbBits() + "bits ";
            if( this.compression   > -1 ) name += "x"  + this.getCompression()   + " ";

            return name.trim() + this.getExtension();
        }

        return this.getFullname() + this.getExtension();
    }

    /**
     * Sets name.
     *
     * @param replaceAllPointInName the replace all point in name
     */
    @Override
    public void setName(boolean replaceAllPointInName)
    {
        String nameToUse = this.fullname;

        if( this.getFullFormatedName() != null && !this.getFullFormatedName().isEmpty() )
        {
            this.remplirListeNombre();
            this.replaceFullFormatedName(false);

            nameToUse = this.getFullFormatedName();
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
                int[] index = {cpt, cpt};
                double nb   = VideoHelper.getNextNumber(index, nameToUse);

                try
                {
                    int cpt2 = index[1];

                    if( nb == this.getNumeroEpisode() )
                        listAllIndex.add(cpt2);

                    StringBuilder tmp = new StringBuilder();

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

                    if( VideoHelper.isEpisodeString(texteAvNb) || VideoHelper.isSaisonString(texteAvNb) ||
                            VideoHelper.isSpecialEpString(texteAvNb) || VideoHelper.isOAVString(texteAvNb) )
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

        for (String lang : Languages.getAllValues())
        {
            Matcher match = Pattern.compile("[|, \\[(]"+lang.toLowerCase()+"[, \\])]|" + lang.toLowerCase() + "$").matcher(nameToUse.toLowerCase());

            if( match.find() )
            {
                int index = match.start();

                if( index < min ) min = index;
            }
        }

        if( min == nameToUse.length() ) // dans le cas ou ni saison ni rien d'autre n'est données
        {
            for (int i = 0; i < nameToUse.length(); i++)
            {
                if( Character.isDigit(nameToUse.charAt(i)) )
                {
                    this.setNumeroEpisode(VideoHelper.getNextNumber(new int[]{i}, nameToUse));

                    min = i;
                    break;
                }
            }

            min = min != nameToUse.length() ? min - 1 : min;
        }

        this.name = nameToUse.substring(0, min).trim();

        if( this.name.endsWith("(TV)") )
            this.name = this.name.substring(0, this.name.indexOf("(TV)")).trim();

        if( this.name.startsWith("NC") )
            this.name = this.name.substring(3).trim();
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

    public void setNbMaxSaison( int nbMaxSaison )
    {
        this.nbMaxSaison = nbMaxSaison;
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

    public boolean isNC()
    {
        return isNC;
    }

    public void setNC(boolean NC)
    {
        isNC = NC;
    }

    public int getAnneeSortie()
    {
        return anneeSortie;
    }

    public void setAnneeSortie(int anneeSortie)
    {
        this.anneeSortie = anneeSortie;
    }

    public int getNbBits()
    {
        return nbBits;
    }

    public void setNbBits(int nbBits)
    {
        this.nbBits = nbBits;
    }

    public boolean isPrefDefLetter()
    {
        return prefDefLetter;
    }

    public void setPrefDefLetter(boolean prefDefLetter)
    {
        this.prefDefLetter = prefDefLetter;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}

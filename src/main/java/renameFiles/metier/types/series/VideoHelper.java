package renameFiles.metier.types.series;

import org.jetbrains.annotations.NotNull;
import renameFiles.metier.enums.Definitions;
import renameFiles.metier.enums.Languages;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoHelper
{
    /**
     * Gets Episode from file.
     *
     * @param file           the video file
     * @param replaceAllPbyS the replace all points by spaces
     * @return Episode or null if extensions not in {@link Episode}.extensions
     */
    public static Episode getEpisodeFromFile(@NotNull File file, boolean replaceAllPbyS )
    {
        int indexOfPoint = file.getName().lastIndexOf(".");

        String fileName  = file.getName().substring(0, indexOfPoint);
        String extension = file.getName().substring(indexOfPoint);

        // ********* Si n'est pas dans les extensions acceptees, FIN ***********************
        if (!Arrays.asList(Episode.extensions).contains(extension.startsWith(".") ? extension.substring(1) : extension))
            return null;

        // certains noms de videos sont comme cela: Un.Episode.S1.Episode.4.vostfr.mp4
        if( replaceAllPbyS )
            fileName = fileName.replaceAll("\\.", " ");

        Episode video = new Episode(fileName, extension, file);

        //*************************** Récupération des valeurs non suivie d'un nombre ********************************
        for (Definitions def : Definitions.values()) // HD - FHD ...
        {
            if( fileName.contains(" " + def.name()) || fileName.contains(def.name() + " ") )
            {
                video.setQualiter(def.getQualiter());
                break;
            }
        }

        String tmpFileName = fileName.toLowerCase();

        fileName = fileName.replaceAll("]\\[", "] [");

        for (String lang : Languages.getAllValues()) // VOSTFR - JAP ...
        {
            Matcher match = Pattern.compile("[|, \\[(]"+lang.toLowerCase()+"[, \\])]|" + lang.toLowerCase() + "$").matcher(tmpFileName);

            if( match.find() )
                video.addLanguages(lang);
        }

        Matcher match = Pattern.compile("^nc|[|, \\[(]nc[, \\])]|nc$").matcher(tmpFileName);

        if( match.find() )
            video.setNC(true);

        if( tmpFileName.contains("hevc") )
            video.setCompression(265);

        // ************************ Parcours de toute la chaine **************************
        for (int cpt = 0; cpt < fileName.length(); cpt++)
        {
            char lettre = fileName.charAt(cpt);

            //********************* Récupération du nombre complet ***********************
            if( Character.isDigit(lettre) )
            {
                int[] index = {cpt, cpt}; // permet un equivalent au passage par reference (=> pointeurs)
                // index[0] = indexFinal | index[1] = save index base

                double nb = VideoHelper.getNextNumber(index, fileName);
                cpt = index[0];

                try
                {
                    video.addNombre(nb);

                    //--------------- si c'est une qualité, on ignore le reste (= pas de texte avant) -----------------
                    if( VideoHelper.numberIsQuality(cpt, fileName.toLowerCase()) )
                    {
                        video.setQualiter((int) video.getNombre(video.getNbNombres()-1));
                        continue;
                    }

                    if (VideoHelper.isAnnee(index[1], fileName.toLowerCase()))
                    {
                        video.setAnneeSortie((int) video.getNombre(video.getNbNombres()-1));
                        continue;
                    }

                    if( VideoHelper.isBits(cpt, fileName.toLowerCase()) )
                    {
                        video.setNbBits((int) video.getNombre(video.getNbNombres()-1));
                        continue;
                    }

                    //*********************** récupération du texte avant le nombre **********************************

                    String texteAvNb = VideoHelper.getTextBeforeNumber(index[1], fileName);

                    //************************ Texte avant un nombre **************************

                    if( texteAvNb.startsWith("[") ) texteAvNb = texteAvNb.substring(1);

                    if( VideoHelper.isEpisodeString(texteAvNb) )
                        video.setNumeroEpisode(video.getNombre(video.getNbNombres()-1));

                    if( VideoHelper.isSpecialEpString(texteAvNb) )
                    {
                        video.setNumeroEpisode(video.getNombre(video.getNbNombres()-1)); // c'est juste un changement de texte.
                        video.setEpisodeSpecial(true);
                    }

                    if( VideoHelper.isOAVString(texteAvNb) )
                    {
                        video.setNumeroEpisode(video.getNombre(video.getNbNombres()-1)); // juste un changement de texte
                        video.setOAV(true);
                    }

                    if( texteAvNb.equals("") || texteAvNb.equals("-") ) // par default, on considere que c'est un episode si il n'est pas deja present
                        if( video.getNumeroEpisode() == -1 )
                            video.setNumeroEpisode(video.getNombre(video.getNbNombres()-1));

                    if( VideoHelper.isSaisonString(texteAvNb) )
                        if ( video.getNumeroSaison() != -1 ) video.setNumeroEpisode((int) video.getNombre(video.getNbNombres()-1));
                        else                                 video.setNumeroSaison ((int) video.getNombre(video.getNbNombres()-1));

                    if( texteAvNb.equals("x") )
                        if(video.getCompression() == -1 )
                            video.setCompression((int) video.getNombre(video.getNbNombres()-1));
                }
                catch (Exception ignored)
                { }
            }
        }

        return video;
    }

    public static boolean isEpisodeString( String str )
    {
        return str.equals("e") || str.equals("ep") || str.equals("episode") || str.equals(
                "épisode") || str.equals("se") || str.equals("sep");
    }

    public static boolean isSpecialEpString( String str )
    {
        return str.equals("sp") || str.equals("spécial") || str.equals("special");
    }

    public static boolean isSaisonString( String str )
    {
        return str.equals("s") || str.equals("saison");
    }

    public static boolean isOAVString( String str )
    {
        return str.equals("oav");
    }

    public static String getTextBeforeNumber( int startNumberIndex, String str )
    {
        StringBuilder tmp = new StringBuilder();

        char lettre;
        boolean oneWhiteSpace = true;
        do
        {
            lettre = str.charAt(startNumberIndex--);

            if( lettre == ' ' )
            {
                if( tmp.length() > 0 )
                    oneWhiteSpace = false;

                if( tmp.length() > 1 ) break;

                continue;
            }

            if( !Character.isDigit(lettre) )
                tmp.append(lettre);
        }
        while( (Character.isDigit(lettre) || oneWhiteSpace) && startNumberIndex > -1);

        return  tmp.reverse().toString().trim().toLowerCase();
    }

    public static double getNextNumber( int[] firstIndex, String str)
    {
        StringBuilder tmp = new StringBuilder("" + str.charAt(firstIndex[0]));

        char lettre = ++firstIndex[0] < str.length() ? str.charAt(firstIndex[0]) : 'a';

        while( Character.isDigit(lettre) || lettre == '.' || lettre == ',' )
        {
            tmp.append(lettre);

            if( firstIndex[0] < str.length()-1)
                lettre = str.charAt(++firstIndex[0]);
            else
                break;
        }

        if( tmp.toString().endsWith(".") || tmp.toString().endsWith(",") )
            tmp.delete(tmp.length()-1, tmp.length());

        return Double.parseDouble(tmp.toString().replaceAll(",", "."));
    }

    public static boolean numberIsQuality( int indexEndNumber, String str)
    {
        return indexEndNumber   < str.length() && str.charAt(indexEndNumber) == 'p' ||
               indexEndNumber+1 < str.length() && str.charAt(indexEndNumber) == ' ' &&
               str.charAt(indexEndNumber+1) == 'p';
    }

    public static boolean isBits( int indexEndNumber, String str )
    {
        if( indexEndNumber >= str.length() || indexEndNumber+1 >= str.length() )
            return false;

        String txt = VideoHelper.getTextBeforeNumber(str.length()-1-indexEndNumber,
                VideoHelper.reverse(str.toLowerCase()));

        return VideoHelper.reverse(txt).equals("bits");
    }

    public static  boolean isAnnee( int indexBeginNumber, String str )
    {
        if( str.charAt(indexBeginNumber-1) != '(' ) return false;

        double d = VideoHelper.getNextNumber(new int[]{indexBeginNumber}, str);

        int length = String.valueOf(d % 1 == 0 ? (int) d : d).length();// (2017) l

        return str.charAt(indexBeginNumber + length) == ')';
    }

    public static String reverse(String str)
    {
        return new StringBuilder(str).reverse().toString();
    }
}

package renameFiles.metier.web.series;

import renameFiles.ihm.MenuBar;
import renameFiles.metier.properties.PropertiesManager;
import renameFiles.metier.web.WebElement;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Serie implements WebElement
{
    private int id;
    private URI url;
    private String name;
    private String type;
    private Date premiered;

    private final HashMap<String, ArrayList<String>> othersNAmes;
    private final ArrayList<Episode>      listEpisodes;

    public Serie()
    {
        this.listEpisodes = new ArrayList<>();
        this.othersNAmes  = new HashMap<>();
    }

    public int getId()
    {
        return id;
    }

    public URI getUrl()
    {
        return url;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public Date getReleaseDate()
    {
        return premiered;
    }

    public boolean addEpisode( Episode ep )
    {
        if( this.getEpisode(ep.getSeason(), ep.getNumber()) != null )
            return false;

        return this.listEpisodes.add(ep);
    }
    
    public boolean addEpisodes(Collection<Episode> collection )
    {
        for (Episode ep : collection)
            if( !this.addEpisode(ep) )
                return false;

        return true;
    }

    public Episode getEpisode( int saison, float episode )
    {
        for (Episode ep : this.listEpisodes)
            if( ep.getSeason() == saison && ep.getNumber() == episode )
                return ep;

        return null;
    }

    public Episode getEpisode( int episode )
    {
        return this.getEpisode(1, episode);
    }

    public boolean addOtherName( Locale locale, String otherName )
    {
        if( locale == null || otherName == null || otherName.isEmpty() )
            return false;

        ArrayList<String> listNames = this.othersNAmes.getOrDefault(locale.getLanguage(), null);

        if( listNames == null )
        {
            listNames = new ArrayList<>();

            this.othersNAmes.put(locale.getLanguage(), listNames);
        }

        return listNames.add(otherName);
    }

    public String[] getOtherNames( Locale locale )
    {
        if( locale == null ) return null;

        return this.othersNAmes.get(locale.getLanguage()).toArray(new String[0]);
    }

    public int getNbEpisodes()
    {
        return this.listEpisodes.size();
    }

    @Override
    public String toString()
    {
        StringBuilder sRet = new StringBuilder("\t" +this.name + " \n\t");

        sRet.append("Nb episodes: ").append(this.listEpisodes.size()).append("\n\t");
        
        sRet.append("other names: ");

        for (String key : this.othersNAmes.keySet())
            for (String l : this.othersNAmes.get(key))
                sRet.append(l).append(", ");

        return sRet.toString();
    }

    public void clearEpisodes()
    {
        this.listEpisodes.clear();
    }

    public String getName(Locale aDefault)
    {
        ArrayList<String> listDefault = this.othersNAmes.get(""); // certaine serie ont une valeur lang a vide

        if( listDefault == null )
            listDefault = new ArrayList<>();

        if( !listDefault.contains(this.getName()))
            listDefault.add(0, this.getName()); // on recupere celle-ci et ajoute le nom par default en 1

        ArrayList<String> listEN = this.othersNAmes.get("en"); // on recupere la liste anglaise

        if( listEN == null )
            listEN = this.othersNAmes.get("us"); // americaine si anglaise n'existe pas

        if( listEN != null && !listEN.containsAll(listDefault) ) // on ajoute la liste par default. en = langue du site.
            listEN.addAll(listDefault);
        else
            this.othersNAmes.put("en", listDefault);

        try
        {
            String   pref = PropertiesManager.getInstance().getPropertie("WebLanguages");
            Locale[] listLocales;

            if( pref == null )
            {
                listLocales = MenuBar.availablesLanguages;
            }
            else
            {
                String[] listLanguage = pref.split(",");
                listLocales  = new Locale[listLanguage.length];

                for (int i = 0; i < listLocales.length; i++)
                    listLocales[i] = Locale.forLanguageTag(listLanguage[i]);
            }

            for (Locale l : listLocales)
            {
                String lang = l.getLanguage();

                if( lang.equalsIgnoreCase("ja") )
                    lang = "jp";

                ArrayList<String> names = this.othersNAmes.get(lang);

                if( names == null & lang.equals("en") )
                    names = this.othersNAmes.get("us"); // en == us

                int index = 0;

                /*
                    Permet de recuperer le nom japonnais ecrit en ecriture occidental
                    si la langue actuelle n'est pas japonaise
                 */
                if( names != null && lang.equals("jp") && !aDefault.getLanguage().equals("ja") )
                {
                    boolean findNameINROMANJI = false;

                    for (String s : names)
                    {
                        Matcher matc = Pattern.compile("[一-龠]+|[ぁ-ゔ]+|[ァ-ヴー]+").matcher(s);

                        if( !matc.find() )
                        {
                            index = names.indexOf(s);
                            findNameINROMANJI = true;

                            break;
                        }
                    }

                    if( !findNameINROMANJI )
                        continue;
                }

                String name = names == null ? null : names.get(index);

                if( name != null )
                    return name;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return this.getName(); // nom par default = anglais
    }
}

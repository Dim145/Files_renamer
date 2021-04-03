package renameFiles.metier.web;

import java.net.URI;
import java.util.*;

public class Serie
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

    public Date getPremiered()
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

    public Episode getEpisode( int saison, int episode )
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

    // Todo améliorer recupération du titre selon la langue
    public String getName(Locale aDefault)
    {
        ArrayList<String> names = this.othersNAmes.get(aDefault.getLanguage());

        String name = names == null ? null : names.get(0);

        if( name == null || name.isEmpty() ) return this.getName();

        return name;
    }
}

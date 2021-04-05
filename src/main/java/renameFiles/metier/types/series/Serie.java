package renameFiles.metier.types.series;

import renameFiles.ihm.dialogs.DialogAvancement;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.types.ListeInterface;
import renameFiles.metier.web.WebElement;
import renameFiles.metier.web.WebInfoHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Serie implements ListeInterface
{
    private final ArrayList<Saison> listSaison;

    private final String    serieName;
    private final boolean[] webValues;

    private final boolean qualiterTextuel;

    public Serie(String serieName)
    {
        this(serieName,false);
    }

    public Serie( String serieName, Collection<Saison> saisons)
    {
        this(serieName, false);

        this.addSaisons(saisons);
    }

    public Serie(String name, boolean qualiterTextuel)
    {
        this(name, qualiterTextuel, new boolean[3]);
    }

    public Serie(String name, boolean qualiterTextuel, boolean[] useInternetData )
    {
        this.serieName  = name;
        this.listSaison = new ArrayList<>();

        this.qualiterTextuel = qualiterTextuel;
        this.webValues = useInternetData;
    }

    @Override
    public boolean add( Object s )
    {
        if( !(s instanceof Saison) && !( s instanceof Episode) ) return false;

        if( s instanceof Saison )
        {
            if( ((Saison)s).getNomSerie().equals(this.serieName) )
                return this.listSaison.add((Saison) s);

            return false;
        }

        Episode video = (Episode) s;

        if( !video.getName().equals(this.serieName) ) return false;

        Saison saison = new Saison(video.getName(), video.getNumeroSaison());

        int index = listSaison.indexOf(saison);

        if ( index == -1 )
        {
            listSaison.add(saison);
            index = listSaison.size()-1;
        }

        return listSaison.get(index).ajouterEpisode(video);
    }

    public Saison getSaison( int numeroSaison )
    {
        if( numeroSaison > this.listSaison.size() )
            return null;

        return this.listSaison.get(numeroSaison-1);
    }

    public boolean addSaisons(Collection<Saison> saisons )
    {
        for (Saison s : saisons)
            if( !this.add(s) )
                return false;

        return true;
    }

    public void setNbMaxSaisonAllSaison()
    {
        int max = 0;

        for (Saison s : this.listSaison)
            if( max < s.getNumeroSaison() )
                max = s.getNumeroSaison();

        for (Saison s : this.listSaison)
            s.setRoundSaisonAllEpisode(max);
    }

    @Override
    public String traitement(DialogAvancement dialog)
    {
        StringBuilder sRet = new StringBuilder();

        if( dialog != null && !dialog.isVisible() ) dialog.setVisible(true);

        this.setNbMaxSaisonAllSaison();

        WebElement webElement = null;

        if(webValues[0])
        {
            if( dialog != null ) dialog.setFichierCourant("recherche " + this.getSerieName() + " depuis Internet...");

            if(VideoHelper.isFilm(this))
            {
                webElement = WebInfoHelper.getWebFilm(this);
            }
            else
            {
                webElement = WebInfoHelper.getWebSerie(this);

                if( webValues[1] && webElement != null )
                {
                    if( dialog != null )
                        dialog.setFichierCourant("demande des episodes de " + this.getSerieName());

                    boolean success = WebInfoHelper.setEpisodesList((renameFiles.metier.web.series.Serie) webElement);

                    if( dialog != null )
                        dialog.setFichierCourant( success ? "réussite" : "échec" );
                }
            }
        }

        for (Saison s : listSaison)
        {
            s.setRoundEpisodeAllEpisode();

            for (Episode video : s.getAllEpisodes())
            {
                video.setPrefDefLetter(this.qualiterTextuel);

                if(webValues[0])
                {
                    if ( webElement != null )
                    {
                        if (webValues[2])
                        {
                            video.setFullFormatedName(webElement.getName(ResourceManager.getInstance().getLocale()));
                            video.setName(false); // ne declenche pas le long script puisque setFullFormatedName est appeler
                        }

                        if( webValues[1] && webElement instanceof renameFiles.metier.web.series.Serie)
                        {
                            renameFiles.metier.web.series.Episode ep = ((renameFiles.metier.web.series.Serie) webElement)
                                    .getEpisode(s.getNumeroSaison(), (float) video.getNumeroEpisode());

                            if (ep != null) video.setTitle(ep.getName());
                        }
                    }
                }

                File file = video.getFile();
                if(dialog != null) dialog.setFichierCourant(file.getName());

                if( file.renameTo(new File(file.getParent() + "/" + video.toString())) )
                    sRet.append("file: ").append(file.getName()).append(" -> <font color=\"rgb(0, 255, 255)\">").append(
                            video.toString()).append("</font>\n");
                else
                    sRet.append("<font color=\"red\">file: ").append(video.getName()).append(" not renamed</font>\n");

                if( dialog != null ) dialog.avancerUneFois();
            }
        }

        return sRet.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass() && o.getClass() != String.class) return false;

        if( o.getClass() == String.class )
            return this.serieName.equals(o);

        Serie serie = (Serie) o;
        return Objects.equals(serieName, serie.serieName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(serieName);
    }

    public String getSerieName()
    {
        return serieName;
    }
}

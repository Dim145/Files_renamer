package renameFiles.metier.types.series;

import renameFiles.ihm.dialogs.DialogAvancement;
import renameFiles.metier.types.ListeInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Serie implements ListeInterface
{
    private final ArrayList<Saison> listSaison;

    private final String serieName;

    private boolean qualiterTextuel;

    public Serie(String serieName)
    {
        this.serieName = serieName;

        this.listSaison = new ArrayList<>();
    }

    public Serie( String serieName, Collection<Saison> saisons)
    {
        this(serieName);

        this.addSaisons(saisons);
    }

    public Serie(String name, boolean qualiterTextuel)
    {
        this(name);

        this.qualiterTextuel = qualiterTextuel;
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

        for (Saison s : listSaison)
        {
            s.setRoundEpisodeAllEpisode();

            for (Episode video : s.getAllEpisodes())
            {
                video.setPrefDefLetter(this.qualiterTextuel);

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
}

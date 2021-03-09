package renameFiles.metier.types.series;

import renameFiles.ihm.dialogs.DialogAvancement;
import renameFiles.metier.types.AbstractListe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Serie extends AbstractListe
{
    private final ArrayList<Saison> listSaison;

    private final String serieName;

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

    @Override
    public boolean add( Object s )
    {
        if( !(s instanceof Saison) && !( s instanceof VideoFile) ) return false;

        if( s instanceof Saison )
        {
            if( ((Saison)s).getNomSerie().equals(this.serieName) )
                return this.listSaison.add((Saison) s);

            return false;
        }

        VideoFile video = (VideoFile) s;

        Saison saison = new Saison(video.getName(), video.getNumeroSaison());

        int index = listSaison.indexOf(saison);

        if ( index == -1 )
        {
            listSaison.add(saison);
            index = listSaison.size()-1;
        }

        listSaison.get(index).ajouterEpisode(video);

        return false;
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

    @Override
    public String traitement(DialogAvancement dialog)
    {
        StringBuilder sRet = new StringBuilder();

        dialog.reset();

        if( !dialog.isVisible() ) dialog.setVisible(true);

        for (Saison s : listSaison)
        {
            dialog.setTitle("Renommage de la saison " + s.getNumeroSaison());

            s.setRoundAllApisode();

            for (VideoFile video : s.getAllEpisodes())
            {
                File file = video.getFile();

                if( file.renameTo(new File(file.getParent() + "/" + video.toString())) )
                    sRet.append("file: ").append(file.getName()).append(" -> <font color=\"rgb(0, 255, 255)\">").append(
                            video.toString()).append("</font>");
                else
                    sRet.append("<font color=\"red\">file: ").append(video.getName()).append(" not renamed</font>\n");

                dialog.avancerUneFois();
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

package renameFiles.metier.types.series;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Saison
{
    private final ArrayList<VideoFile> listeEpisode;
    private final String               nomSerie;

    private int numeroSaison;
    private int nbEpisodes;

    public Saison(@NotNull String nomSerie)
    {
        this(nomSerie, 1);
    }

    public Saison(@NotNull String nomSerie, int numeroSaison)
    {
        this.listeEpisode = new ArrayList<>();
        this.nbEpisodes   = 0;

        this.numeroSaison = numeroSaison;
        this.nomSerie     = nomSerie;
    }

    public int getNumeroSaison()
    {
        return numeroSaison;
    }

    public void setNumeroSaison(int numeroSaison)
    {
        this.numeroSaison = numeroSaison;
    }

    public int getNbEpisodes()
    {
        return nbEpisodes;
    }

    public void setNbEpisodes(int nbEpisodes)
    {
        this.nbEpisodes = nbEpisodes;
    }

    public boolean ajouterEpisode(@NotNull VideoFile video)
    {
        if (video.getName() == null) video.setName(false);

        if (!this.nomSerie.isEmpty() && this.nomSerie.contains(video.getName()))
        {
            boolean isAdd = this.listeEpisode.add(video);

            if (!isAdd) return false;

            if (this.nbEpisodes < video.getNumeroEpisode())
                this.nbEpisodes = (int) Math.round(video.getNumeroEpisode());

            return true;
        }

        return false;
    }

    public boolean ajouterEpisodes(@NotNull Collection<VideoFile> collections)
    {
        for (VideoFile video : collections)
            if (!this.ajouterEpisode(video)) return false;

        return true;
    }

    public VideoFile[] getAllEpisodes()
    {
        return this.listeEpisode.toArray(new VideoFile[0]);
    }
    
    public void setRoundAllApisode()
    {
        if( this.nbEpisodes > 0 )
            for (VideoFile video : this.listeEpisode)
                video.setNbMaxEpisode(this.nbEpisodes);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Saison saison = (Saison) o;

        return numeroSaison == saison.numeroSaison && nomSerie.equals(saison.nomSerie);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(nomSerie, numeroSaison);
    }
}

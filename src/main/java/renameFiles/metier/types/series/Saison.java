package renameFiles.metier.types.series;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * The type Saison.
 */
public class Saison
{
    private final ArrayList<Episode> listeEpisode;
    private final String               nomSerie;

    private int numeroSaison;
    private int nbEpisodes;

    /**
     * Instantiates a new Saison.
     *
     * @param nomSerie the nom serie
     */
    public Saison(@NotNull String nomSerie)
    {
        this(nomSerie, 1);
    }

    /**
     * Instantiates a new Saison.
     *
     * @param nomSerie     the nom serie
     * @param numeroSaison the numero saison
     */
    public Saison(@NotNull String nomSerie, int numeroSaison)
    {
        this.listeEpisode = new ArrayList<>();
        this.nbEpisodes   = 0;

        this.numeroSaison = Math.abs(numeroSaison);
        this.nomSerie     = nomSerie;
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
     * Gets nb episodes.
     *
     * @return the nb episodes
     */
    public int getNbEpisodes()
    {
        return nbEpisodes;
    }

    /**
     * Sets nb episodes.
     *
     * @param nbEpisodes the nb episodes
     */
    public void setNbEpisodes(int nbEpisodes)
    {
        this.nbEpisodes = nbEpisodes;
    }

    /**
     * Ajouter episode boolean.
     *
     * @param video the video
     * @return the boolean
     */
    public boolean ajouterEpisode(@NotNull Episode video)
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

    public String getNomSerie()
    {
        return this.nomSerie;
    }

    /**
     * Ajouter episodes boolean.
     *
     * @param collections the collections
     * @return the boolean
     */
    public boolean ajouterEpisodes(@NotNull Collection<Episode> collections)
    {
        for (Episode video : collections)
            if (!this.ajouterEpisode(video)) return false;

        return true;
    }

    /**
     * Get all episodes video file [ ].
     *
     * @return the video file [ ]
     */
    public Episode[] getAllEpisodes()
    {
        return this.listeEpisode.toArray(new Episode[0]);
    }

    /**
     * Sets round all apisode.
     */
    public void setRoundEpisodeAllEpisode()
    {
        if( this.nbEpisodes > 0 )
            for (Episode video : this.listeEpisode)
                video.setNbMaxEpisode(this.nbEpisodes);
    }

    public void setRoundSaisonAllEpisode( int nbMaxSaison )
    {
        if( nbMaxSaison > 0 )
            for (Episode video : this.listeEpisode)
                video.setNbMaxSaison(nbMaxSaison);
    }

    public Episode getEpisode( int numeroEp )
    {
        if( numeroEp > this.listeEpisode.size() )
            return null;

        return this.listeEpisode.get(numeroEp-1);
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

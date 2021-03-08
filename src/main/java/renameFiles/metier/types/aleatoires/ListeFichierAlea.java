package renameFiles.metier.types.aleatoires;

import org.jetbrains.annotations.NotNull;
import renameFiles.ihm.dialogs.DialogAvancement;

import java.io.File;
import java.util.*;

/**
 * The type Liste fichier alea.
 */
public class ListeFichierAlea implements Iterable<AleaNameFile>
{
    private final ArrayList<Integer>      listeChiffreExistant;
    private final ArrayList<AleaNameFile> listeFiles;

    private boolean saveNbIfExist;

    /**
     * Instantiates a new Liste fichier alea.
     *
     * @param saveNbIfExist the save nb if exist
     */
    public ListeFichierAlea( boolean saveNbIfExist )
    {
        this.listeChiffreExistant = new ArrayList<>();
        this.listeFiles           = new ArrayList<>();

        this.saveNbIfExist = saveNbIfExist;
    }

    /**
     * Instantiates a new Liste fichier alea.
     *
     * @param files         the files
     * @param saveNbIfExist the save nb if exist
     */
    public ListeFichierAlea( Collection<File> files, boolean saveNbIfExist )
    {
        this(saveNbIfExist);

        this.addFiles(files);
    }

    /**
     * Add files.
     *
     * @param files the files
     */
    public void addFiles(Collection<File> files )
    {
        if( files.size() == 0 ) return;

        for (File f : files)
            this.addFile(f);
    }

    /**
     * Add file.
     *
     * @param f the f
     */
    public void addFile( File f )
    {
        int lastIndexOfP = f.getName().lastIndexOf(".");
        String fileName  = f.getName().substring(0, lastIndexOfP);

        AleaNameFile file = new AleaNameFile(fileName, f.getName().substring(lastIndexOfP), f, this.saveNbIfExist);

        this.listeFiles.add(file);

        if( file.getChiffrePostNom() != -1 )
        {
            if (this.listeChiffreExistant.contains(file.getChiffrePostNom()))
                file.setChiffrePostNom(-1);
            else
                this.listeChiffreExistant.add(file.getChiffrePostNom());
        }
    }

    /**
     * Clear.
     */
    public void clear()
    {
        this.listeFiles.clear();
        this.listeChiffreExistant.clear();
    }

    /**
     * Sets nb alea post name.
     *
     * @param dialog the dialog
     */
    public void setNbAleaPostName(DialogAvancement dialog)
    {
        int max = this.listeFiles.size();

        if( dialog != null )
        {
            dialog.setVisible(false);
            dialog.setTitle("Calcule des chiffres aléatoires a distribuer");
            dialog.setBorneMax(max);
            dialog.reset();
            dialog.setVisible(true);
        }

        for (AleaNameFile file : this.listeFiles)
        {
            int alea;

            do
            {
                alea = (int) Math.round(Math.random() * max);

                if ( alea == 0 ) alea = 1;
            }
            while( this.listeChiffreExistant.contains(alea) && this.listeChiffreExistant.size() != this.listeFiles.size() );

            if( file.setChiffrePostNomIfNotExist(alea) )
                this.listeChiffreExistant.add(alea);

            if( dialog != null )
            {
                dialog.avancerUneFois();
                dialog.setFichierCourant(file.getFullname());
            }
        }

        OptionalInt oi = this.listeChiffreExistant.stream().mapToInt(e -> e).max();

        if(oi.isPresent())
        {
            for (AleaNameFile file : this.listeFiles)
                file.setNbRound(String.valueOf(oi.getAsInt()).length());
        }

        if( dialog != null ) dialog.setVisible(false);
    }

    @NotNull
    @Override
    public Iterator<AleaNameFile> iterator()
    {
        return this.listeFiles.iterator();
    }

    @Override
    public Spliterator<AleaNameFile> spliterator()
    {
        return this.listeFiles.spliterator();
    }
}

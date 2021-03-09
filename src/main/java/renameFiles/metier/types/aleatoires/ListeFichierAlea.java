package renameFiles.metier.types.aleatoires;

import org.jetbrains.annotations.NotNull;
import renameFiles.ihm.dialogs.DialogAvancement;
import renameFiles.metier.types.AbstractListe;

import java.io.File;
import java.util.*;

/**
 * The type Liste fichier alea.
 */
public class ListeFichierAlea extends AbstractListe implements Iterable<AleaNameFile>
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
     * @return
     */
    public boolean addFiles(Collection<File> files )
    {
        if( files.size() == 0 ) return false;

        for (File f : files)
            if( !this.add(f) )
                return false;

        return true;
    }

    /**
     * Add file.
     *
     * @param obj the File
     */
    @Override
    public boolean add( Object obj )
    {
        if( obj instanceof Collection ) return this.addFiles( (Collection) obj);

        if( !( obj instanceof File) ) return false;

        File f = (File) obj;

        int lastIndexOfP = f.getName().lastIndexOf(".");
        String fileName  = f.getName().substring(0, lastIndexOfP);

        AleaNameFile file = new AleaNameFile(fileName, f.getName().substring(lastIndexOfP), f, this.saveNbIfExist);

        if( file.getChiffrePostNom() != -1 )
        {
            if (this.listeChiffreExistant.contains(file.getChiffrePostNom()))
                file.setChiffrePostNom(-1);
            else
                this.listeChiffreExistant.add(file.getChiffrePostNom());
        }

        return this.listeFiles.add(file);
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

    @Override
    public String traitement(DialogAvancement dialog)
    {
        StringBuilder sRet = new StringBuilder();

        this.setNbAleaPostName(dialog);

        dialog.setVisible(false);
        dialog.setTitle("Renommage en cours...");
        dialog.reset();
        dialog.setVisible(true);

        for (AleaNameFile aleaFile : this)
        {
            dialog.setFichierCourant(aleaFile.toString());

            if( aleaFile.getFile().renameTo(new File(aleaFile.getNewPath())) )
                sRet.append("file: ").append(aleaFile.getFullname()).append(aleaFile.getExtension()).append(
                        " -> <font color=\"rgb(0, 255, 255)\">").append(aleaFile).append("</font>\n");
            else
                sRet.append("<font color=\"red\">file: ").append(aleaFile.getFullname()).append(
                        aleaFile.getExtension()).append(" not renamed</font>\n");

            dialog.avancerUneFois();
        }

        dialog.setVisible(false);

        return sRet.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListeFichierAlea that = (ListeFichierAlea) o;
        return saveNbIfExist == that.saveNbIfExist && listeFiles.equals(that.listeFiles);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(listeFiles, saveNbIfExist);
    }
}

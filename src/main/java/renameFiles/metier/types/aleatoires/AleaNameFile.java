package renameFiles.metier.types.aleatoires;

import renameFiles.metier.types.BaseFile;

import java.io.File;

public class AleaNameFile extends BaseFile
{
    private int chiffrePostNom;

    private int nbRound;

    public AleaNameFile(String fullname, String extension, File file, boolean saveNbIfExist)
    {
        super(fullname, extension, file);

        int indexMoins = fullname.indexOf("-");

        if( saveNbIfExist )
        {
            try
            {
                this.chiffrePostNom = indexMoins != -1 ? Integer.parseInt(fullname.substring(0, indexMoins).trim()) : -1;
            }
            catch (NumberFormatException e) // cas ou le moins 1 n'est pas devant un chiffre
            {
                this.chiffrePostNom = indexMoins = -1;
            }
        }
        else
        {
            this.chiffrePostNom = -1;
        }

        if( indexMoins != -1 )
            this.name = this.fullname.substring(indexMoins+1).trim();

        this.nbRound = 2;
    }

    public void setChiffrePostNom( int nouveauChiffre )
    {
        this.chiffrePostNom = nouveauChiffre;
    }

    public boolean setChiffrePostNomIfNotExist( int nouveauChiffre )
    {
        if( this.chiffrePostNom != -1 || nouveauChiffre < 0 ) return false;

        this.setChiffrePostNom(nouveauChiffre);

        return true;
    }

    public int getChiffrePostNom()
    {
        return this.chiffrePostNom;
    }

    public int getNbRound()
    {
        return nbRound;
    }

    public void setNbRound(int nbRound)
    {
        this.nbRound = Math.max(nbRound, 2);
    }

    @Override
    public String toString()
    {
        if( chiffrePostNom != -1 )
            return String.format("%0" + this.nbRound + "d", this.chiffrePostNom) + " - " + this.name + this.extension;

        return this.fullname + this.extension;
    }

    public String getNewPath()
    {
        return this.getFile().getParent() + "/" + this.toString();
    }
}

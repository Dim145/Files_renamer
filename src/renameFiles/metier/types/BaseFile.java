package renameFiles.metier.types;

import java.io.File;
import java.util.ArrayList;

public class BaseFile
{
    protected ArrayList<Double> listeNombres;

    private final File file;

    protected String fullname;

    protected String fullFormatedName;
    protected String name;

    protected String extension;

    public BaseFile(String fullname, String extension )
    {
        this(fullname, extension, null);
    }

    public BaseFile(String fullname, String extension, File file )
    {
        this.listeNombres = new ArrayList<>();

        this.fullname = fullname;
        this.extension = extension;

        this.file = file;
    }

    public String getFullname()
    {
        return fullname;
    }

    public String getExtension()
    {
        return extension;
    }

    public String getName()
    {
        return this.name;
    }

    public void addNombre( double nombre )
    {
        this.listeNombres.add(nombre);
    }

    public double[] listNombre()
    {
        return this.listeNombres.stream().mapToDouble(element -> element).toArray();
    }

    public int getNbNombres()
    {
        return this.listeNombres.size();
    }

    public double getNombre( int index )
    {
        if( index < 0 || index > this.getNbNombres() ) return Double.MIN_VALUE;

        return this.listeNombres.get(index);
    }

    public String getFullFormatedName()
    {
        return fullFormatedName;
    }

    public void setFullFormatedName(String fullFormatedName)
    {
        this.fullFormatedName = fullFormatedName;
    }

    public void replaceFullFormatedName()
    {
        if( this.fullFormatedName == null ) return;

        for (double aDouble : this.listeNombres)
        {
            int tmp     = (int) aDouble;

            String finalS = aDouble % 1 == 0 ? String.format("%02d", tmp) : String.format(
                    "%02.2f", aDouble);

            this.fullFormatedName = this.fullFormatedName.replaceFirst("%%", finalS);
        }
    }

    public void remplirListeNombre()
    {
        for (int cpt = 0; cpt < this.fullname.length(); cpt++)
        {
            char lettre = fullname.charAt(cpt);

            if( Character.isDigit(lettre) )
            {
                StringBuilder tmp = new StringBuilder("" + lettre);

                lettre = ++cpt < fullname.length() ? fullname.charAt(cpt) : 'a';

                while( Character.isDigit(lettre) || lettre == '.' || lettre == ',' )
                {
                    tmp.append(lettre);

                    if( cpt < fullname.length()-1)
                        lettre = fullname.charAt(++cpt);
                    else
                        break;
                }

                if( tmp.toString().endsWith(".") || tmp.toString().endsWith(",") )
                    tmp.delete(tmp.length()-1, tmp.length());

                try
                {
                    this.addNombre(Double.parseDouble(tmp.toString().replaceAll(",", ".")));
                }
                catch (Exception ignored)
                { }
            }
        }
    }

    @Override
    public String toString()
    {
        String name = this.fullFormatedName == null ? this.fullname : this.fullFormatedName;

        if( name.contains("%%") ) name = this.fullname;

        return name + this.extension;
    }

    public File getFile()
    {
        return file;
    }
}

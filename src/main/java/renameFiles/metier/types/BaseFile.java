package renameFiles.metier.types;

import java.io.File;
import java.util.ArrayList;

/**
 * The type Base file.
 */
public class BaseFile
{
    /**
     * The Liste nombres.
     */
    protected ArrayList<Double> listeNombres;

    private final File file;

    /**
     * The Fullname.
     */
    protected final String fullname;

    /**
     * The Full formated name.
     */
    protected String fullFormatedName;
    /**
     * The Name.
     */
    protected String name;

    /**
     * The Extension.
     */
    protected String extension;

    /**
     * Instantiates a new Base file.
     *
     * @param fullname  the fullname
     * @param extension the extension
     */
    public BaseFile(String fullname, String extension )
    {
        this(fullname, extension, null);
    }

    /**
     * Instantiates a new Base file.
     *
     * @param fullname  the fullname
     * @param extension the extension
     * @param file      the file
     */
    public BaseFile(String fullname, String extension, File file )
    {
        this.listeNombres = new ArrayList<>();

        this.fullname = fullname;
        this.extension = extension;

        this.file = file;
    }

    /**
     * Gets fullname.
     *
     * @return the fullname
     */
    public String getFullname()
    {
        return fullname;
    }

    /**
     * Gets extension.
     *
     * @return the extension
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Add nombre.
     *
     * @param nombre the nombre
     */
    public void addNombre( double nombre )
    {
        this.listeNombres.add(nombre);
    }

    /**
     * List nombre double [ ].
     *
     * @return the double [ ]
     */
    public double[] listNombre()
    {
        return this.listeNombres.stream().mapToDouble(element -> element).toArray();
    }

    /**
     * Gets nb nombres.
     *
     * @return the nb nombres
     */
    public int getNbNombres()
    {
        return this.listeNombres.size();
    }

    /**
     * Gets nombre.
     *
     * @param index the index
     * @return the nombre
     */
    public double getNombre( int index )
    {
        if( index < 0 || index > this.getNbNombres() ) return Double.MIN_VALUE;

        return this.listeNombres.get(index);
    }

    /**
     * Gets full formated name.
     *
     * @return the full formated name
     */
    public String getFullFormatedName()
    {
        return fullFormatedName;
    }

    /**
     * Sets full formated name.
     *
     * @param fullFormatedName the full formated name
     */
    public void setFullFormatedName(String fullFormatedName)
    {
        this.fullFormatedName = fullFormatedName;
    }

    /**
     * Replace full formated name boolean.
     *
     * @param blockIfNotMathPatern the block if not math patern
     * @return the boolean
     */
    public boolean replaceFullFormatedName(boolean blockIfNotMathPatern)
    {
        if( this.fullFormatedName == null ) return false;

        if( blockIfNotMathPatern )
        {
            int nbPourcentage = 0;

            for (int i = 0; i < this.fullFormatedName.length()-1; i++)
                if( "%%".equals( ("" + this.fullFormatedName.charAt(i)) + this.fullFormatedName.charAt(i+1) ) )
                    nbPourcentage++;

            if( nbPourcentage != this.listeNombres.size()) return false;
        }

        for (double aDouble : this.listeNombres)
        {
            int tmp     = (int) aDouble;

            String finalS = aDouble % 1 == 0 ? String.format("%02d", tmp) : String.format(
                    "%02.2f", aDouble);

            this.fullFormatedName = this.fullFormatedName.replaceFirst("%%", finalS);
        }

        return true;
    }

    /**
     * Remplir liste nombre.
     */
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

    /**
     * Gets file.
     *
     * @return the file
     */
    public File getFile()
    {
        return file;
    }

    public void setName( boolean replaceAllPointInName )
    {
        this.remplirListeNombre();
    }
}

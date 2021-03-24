package renameFiles.metier;

import renameFiles.Controleur;
import renameFiles.ihm.dialogs.DialogAvancement;
import renameFiles.metier.enums.FileType;
import renameFiles.metier.properties.PropertiesManager;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;
import renameFiles.metier.types.BaseFile;
import renameFiles.metier.types.BaseFileListe;
import renameFiles.metier.types.ListeInterface;
import renameFiles.metier.types.aleatoires.AleaNameFile;
import renameFiles.metier.types.aleatoires.ListeFichierAlea;
import renameFiles.metier.types.series.Serie;
import renameFiles.metier.types.series.VideoFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The type Metier.
 */
public class Metier
{
    public static final String[] tabPreferences = {"BlockIfNotMathPatern", "DarkMode", "SDL", "Language" };

    private final ArrayList<File> files;
    private final Controleur      ctrl;
    private final List<String> acceptedExtension;

    private boolean saveNbIfExistInAlea;

    private FileType typeCourant;
    private int      maxLevel;

    /**
     * Instantiates a new Metier.
     *
     * @param ctrl the ctrl
     */
    public Metier(Controleur ctrl )
    {
        this.files             = new ArrayList<>();
        this.acceptedExtension = new ArrayList<>();
        this.ctrl              = ctrl;

        this.typeCourant          = FileType.AUTRES;
        this.maxLevel             = 1;
    }

    /**
     * Sets preference file.
     */
    public void setupPreferenceFile()
    {
        try
        {
            PropertiesManager manager = PropertiesManager.getInstance();

            Class<Controleur> controleurClass = Controleur.class;

            for (String key : tabPreferences)
            {
                Method    m;
                Class<?>  c;

                try
                {
                    c = Boolean.class;
                    m = controleurClass.getMethod("set" + key, boolean.class);
                }
                catch (NoSuchMethodException e)
                {
                    try
                    {
                        c = Integer.class;
                        m = controleurClass.getMethod("set" + key, int.class);
                    }
                    catch (NoSuchMethodException exception)
                    {
                        continue;
                    }
                }

                Method parse = null;

                for (Method method : c.getMethods())
                {
                    if( method.getName().contains("parse") && method.getParameterCount() == 1 )
                    {
                        parse = method;
                        break;
                    }
                }

                String value = manager.getPropertie(key);

                if( value != null && parse != null ) try
                {
                    m.invoke(this.ctrl, parse.invoke(c, value));
                }
                catch (Exception ignored)
                {
                    value = null;
                }

                if( value == null )
                    manager.setPropertie(key, c == Boolean.class ? "false" : "1");
            }
        }
        catch (IOException e)
        {
            this.ctrl.printConsole("<font color=\"red\">Error, cannot create the preferences file</font>");
        }
    }

    public void setLanguesByPrefFile()
    {
        String locale = null;
        try
        {
            locale = PropertiesManager.getInstance().getPropertie("Language");
        }
        catch (IOException ignored)
        {

        }

        ResourceManager.getInstance().setLocale(locale != null ? new Locale(locale) : Locale.getDefault());
    }

    public void savePreferences(String key, String value)
    {
        try
        {
            PropertiesManager.getInstance().setPropertie(key, value);
        }
        catch (IOException ignored)
        {

        }
    }

    /**
     * Sets accepted extensions.
     *
     * @param extensions the extensions
     * @return the accepted extensions
     */
    public boolean setAcceptedExtensions( String extensions )
    {
        if( extensions == null || extensions.length() < 1) return false;

        this.acceptedExtension.clear();

        for( String s : extensions.split(",") )
        {
            if( s.isEmpty() || " ".equals(s) ) continue;

            if( s.startsWith(".") ) s = s.substring(1);

            this.acceptedExtension.add(s.trim());
        }

        return this.acceptedExtension.size() > 0;
    }

    /**
     * Sets save nb if exist in alea.
     *
     * @param saveNbIfExistInAlea the save nb if exist in alea
     */
    public void setSaveNbIfExistInAlea( boolean saveNbIfExistInAlea )
    {
        this.saveNbIfExistInAlea = saveNbIfExistInAlea;
    }

    /**
     * Rename with paterne in path.
     *
     * @param path                  the path
     * @param patern                the patern
     * @param replaceAllPointInName the replace all point in name
     */
    // Todo optimisé l'algorithme en utilisant vraiment les objets (et la reflexivité ?)
    public void renameWithPaterneInPath( String path, String patern, boolean replaceAllPointInName )
    {
        if( this.typeCourant == FileType.AUTRES && (patern == null || patern.length() < 1 || !patern.contains("%%")) )
        {
            this.ctrl.printConsole("<font color=\"red\">Saisissez un paterne avec au moins 1 fois \"%%\"</font>");
            return;
        }

        File repRacine = new File(path);

        this.files.clear();
        this.readRepertory(repRacine, 0);

        if( this.files.size() == 0 )
        {
            this.ctrl.printConsole("<font color=\"red\">Aucun fichier avec les extensions saisi n'ont été trouvés</font>");
            return;
        }

        DialogAvancement dialog = new DialogAvancement(this.typeCourant == FileType.SERIES ? "Lecture et recupération des donées..." : "Lecture et renommage des fichiers...", 0, this.files.size());

        Thread t = new Thread(() ->
        {
            ArrayList<ListeInterface> lists = new ArrayList<>();

            lists.add(new ListeFichierAlea(this.saveNbIfExistInAlea));
            lists.add(new BaseFileListe(replaceAllPointInName));

            dialog.setVisible(true);

            for (File file : this.files)
            {
                String fileName  = file.getName().substring(0, file.getName().lastIndexOf("."));
                String extension = file.getName().substring(file.getName().lastIndexOf("."));

                BaseFile baseFile;

                switch (this.typeCourant)
                {
                    case ALEANAME: baseFile = new AleaNameFile(fileName, extension, file, this.saveNbIfExistInAlea);break;
                    case SERIES  : baseFile = VideoFile.getVideoFileFromFile(file, replaceAllPointInName);break;
                    case AUTRES  : baseFile = new BaseFile(fileName, extension, file);break;
                    default      : baseFile = null;
                }

                if( baseFile == null )
                {
                    this.ctrl.printConsole("<font color=\"red\">file: " + fileName + " n'est pas du bon type</font>");
                    return;
                }

                baseFile.setFullFormatedName(patern);
                baseFile.setName(replaceAllPointInName);

                boolean isAjouter = false;
                for (ListeInterface liste : lists)
                {
                    if (liste.add(baseFile))
                        isAjouter = true;
                }

                if( !isAjouter ) // l'ajout ne peut echouer que dans le cas d'une serie
                {
                    lists.add(new Serie(baseFile.getName()));
                    lists.get(lists.size()-1).add(baseFile);
                }

                dialog.avancerUneFois();
                dialog.setFichierCourant(fileName);
            }

            dialog.setVisible(false);
            dialog.reset();
            dialog.setTitle(ResourceManager.getInstance().getString(Resources.RENAME_FILES));
            dialog.setVisible(true);

            for (ListeInterface liste : lists)
                this.ctrl.addTextToTampon(liste.traitement(dialog), false);

            if( dialog.isVisible() )
                dialog.setVisible(false);

            this.ctrl.addTextToTampon("<center color=\"red\">FIN</center>", true);
        });

        t.start();
    }

    private void readRepertory( final File rep, int level )
    {
        if( rep.isFile() || rep.getName().equals("renamed") ) return;

        if( level < 0) level = 0;

        File[] temp = rep.listFiles();

        if( temp == null ) return;

        for (File file : temp)
            if (file.isFile())
            {
                if (this.accept(file.getName())) this.files.add(file);
            }
            else if (file.isDirectory() && level <= this.maxLevel )
            {
                readRepertory(file, ++level);
            }
    }

    /**
     * Accept boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean accept(String name)
    {
        if( this.acceptedExtension.contains("*") )
            return true;

        if( this.acceptedExtension.size() > 0 )
            for( String s : this.acceptedExtension )
                if( name.endsWith("." + s))
                    return true;

        return false;
    }

    /**
     * Sets block if not math patern.
     *
     * @param b the b
     */
    public void setBlockIfNotMathPatern(boolean b)
    {
    }

    /**
     * Gets type courant.
     *
     * @return the type courant
     */
    public FileType getTypeCourant()
    {
        return typeCourant;
    }

    /**
     * Sets type courant.
     *
     * @param typeCourant the type courant
     */
    public void setTypeCourant(FileType typeCourant)
    {
        this.typeCourant = typeCourant;
    }

    /**
     * Sets max level.
     *
     * @param niveauDeRecherche the niveau de recherche
     */
    public void setMaxLevel( int niveauDeRecherche )
    {
        if( niveauDeRecherche < 0 ) return;

        this.maxLevel = niveauDeRecherche;

        this.savePreferences(Metier.tabPreferences[2], String.valueOf(this.maxLevel));
    }

    /**
     * Gets level max.
     *
     * @return the level max
     */
    public int getLevelMax()
    {
        return this.maxLevel;
    }
}

package renameFiles.metier;

import renameFiles.Controleur;
import renameFiles.ihm.dialogs.DialogAvancement;
import renameFiles.metier.enums.FileType;
import renameFiles.metier.types.AbstractListe;
import renameFiles.metier.types.BaseFile;
import renameFiles.metier.types.BaseFileListe;
import renameFiles.metier.types.aleatoires.AleaNameFile;
import renameFiles.metier.types.aleatoires.ListeFichierAlea;
import renameFiles.metier.types.series.Serie;
import renameFiles.metier.types.series.VideoFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * The type Metier.
 */
public class Metier
{
    /**
     * The constant tabPreferences.
     */
    public static final String[] tabPreferences = {"BlockIfNotMathPatern", "DarkMode", "SDL" };

    private final ArrayList<File> files;
    private final Controleur      ctrl;
    private final List<String> acceptedExtension;

    private boolean blockIfNotMathPatern;
    private boolean saveNbIfExistInAlea;

    private FileType typeCourant;
    private int      maxLevel;

    private File preferencesFile;

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
        this.blockIfNotMathPatern = true;
        this.maxLevel             = 1;
        this.preferencesFile      = null;
    }

    /**
     * Sets preference file.
     */
    public void setupPreferenceFile()
    {
        File appDirectorie = new File(System.getProperty("user.home") + "/.FileRenamer");

        if( !appDirectorie.exists() )
            if( appDirectorie.mkdir() ) this.ctrl.printConsole("File \".FileRenamer\" created.");
            else                        this.ctrl.printConsole("<font color=\"red\">Error, cannot create the app file in user directory</font>");

        this.preferencesFile = new File(appDirectorie.getAbsolutePath() + "/.preferences.conf");

        if( this.preferencesFile.exists() )
            this.readPreferenceFile();
        else
            this.createPrefFile();
    }

    private void createPrefFile()
    {
        try
        {
            if( this.preferencesFile.createNewFile() )
            {
                this.ctrl.printConsole("File preferences created");
            }
            else
            {
                this.ctrl.printConsole("<font color=\"red\">Error, cannot create the preferences file</font>");
            }

            if( this.preferencesFile.exists() )
            {
                HashMap<String, Object> prefs = new HashMap<>();

                for (int i = 0; i < Metier.tabPreferences.length-1; i++)
                    prefs.put(Metier.tabPreferences[i], false);

                prefs.put(Metier.tabPreferences[2], 2);

                this.savePreferences(prefs, true);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void readPreferenceFile()
    {
        try(Scanner scanner = new Scanner(this.preferencesFile))
        {
            HashMap<String, String> prefs = new HashMap<>();

            while( scanner.hasNext() )
            {
                String line = scanner.nextLine();
                String[] tab = line.split("=");

                if( tab.length < 2 ) continue;

                prefs.put(tab[0], tab[1]);
            }

            List<String> listPref = Arrays.asList(Metier.tabPreferences);

            Class<Controleur> controleurClass = Controleur.class;

            for (String key : prefs.keySet())
            {
                if( !listPref.contains(key) ) continue;

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
                    if( method.getName().contains("parse") )
                    {
                        parse = method;
                        break;
                    }
                }

                if( parse != null )
                    m.invoke(this.ctrl, parse.invoke(c, prefs.get(key)));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Save preferences.
     *
     * @param hashMap   the hash map
     * @param clearFile the clear file
     */
    public void savePreferences(HashMap<String, Object> hashMap, boolean clearFile )
    {
        String fileValue = "";
        if( !clearFile ) fileValue = this.readAllInPrefFile();

        if ( !hashMap.containsKey( Metier.tabPreferences[2]) )
            hashMap.put(Metier.tabPreferences[2], this.maxLevel);

        try(FileWriter writer = new FileWriter(this.preferencesFile))
        {
            writer.append(fileValue);

            for (String key : hashMap.keySet())
                writer.append(key).append("=").append(String.valueOf(hashMap.get(key))).append("\n");
        }
        catch (Exception e)
        {
            this.ctrl.printConsole("<font color=\"red\">Error in file writting");
            e.printStackTrace();
        }
    }

    private String readAllInPrefFile()
    {
        StringBuilder res = new StringBuilder();

        try(Scanner scanner = new Scanner(this.preferencesFile))
        {
            scanner.reset();

            while( scanner.hasNext() )
                res.append(scanner.nextLine()).append("\n");
        }
        catch (Exception e) { e.printStackTrace(); }

        return res.toString();
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

        new Thread(() ->
        {
            ArrayList<AbstractListe> lists = new ArrayList<>();

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
                for (AbstractListe liste : lists)
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

            for (AbstractListe liste : lists)
                this.ctrl.printConsole(liste.traitement(dialog));

            if( dialog.isVisible() )
                dialog.setVisible(false);
        }).start();
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
        this.blockIfNotMathPatern = b;
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

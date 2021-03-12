package renameFiles.metier.resources;

public enum Resources
{
    TITLE("title"),
    APP_NAME("app_name"),
    CB_REPLACEPBYS("cb_saveNbIfExist"),
    CB_SAVENBIFEXIST("cb_replacePbyS"),
    BTN_LAUNCH("btn_launch"),
    PATH("path"),
    TYPE_APP("type_ap"),
    EXTENSIONS("extensions"),
    NAME_PATERN("name_patern"),
    NB_SD("nb_sd"),
    TOOLTIP_NB_SD("toolTip_nb_sd"),
    TREATMENT("treatment"),
    CREATEDBY("created_by"),
    DEVELOPEDIN("developedIn"),
    VERSION("version_ap"),
    ABOUT_TITLE("title_about"),
    MENU_OPTION("menu_option"),
    HELP("help"),
    BLOCK_NOT_MATCH("block_not_match"),
    DARK_THEME("dark_theme"),
    ABOUT("about"),
    NO_REQUIRED("no_required"),
    NO_TO_WRITE("no_to_write"),
    LANGUAGES("languages"),
    FRENCH("french"),
    ENGLISH("english");


    private final String resourceKey;

    Resources(String resourceKey )
    {
        this.resourceKey = resourceKey;
    }

    public String getKey()
    {
        return this.resourceKey;
    }

    @Override
    public String toString()
    {
        return this.getKey();
    }
}

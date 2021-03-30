package renameFiles.metier.enums;

import renameFiles.metier.types.series.Episode;

/**
 * The enum File type.
 */
public enum FileType
{
    /**
     * Autres file type.
     */
    AUTRES(null),
    /**
     * Aleaname file type.
     */
    ALEANAME(null),
    /**
     * Series file type.
     */
    SERIES(Episode.extensions);

    private final String[] extensions;

    FileType( String[] extensions )
    {
        this.extensions = extensions;
    }

    /**
     * Gets list extension in string.
     *
     * @return the list extension in string
     */
    public String getListExtensionInString()
    {
        if( this.extensions == null ) return "";

        StringBuilder res = new StringBuilder();

        for (String extension : this.extensions)
            res.append(extension).append(",");

        res.setLength(res.length()-1);

        return res.toString();
    }
}

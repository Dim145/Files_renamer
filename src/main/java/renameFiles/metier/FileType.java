package renameFiles.metier;

import renameFiles.metier.types.videos.VideoFile;

public enum FileType
{
    AUTRES(null),
    VIDEOS(VideoFile.extensions);

    private final String[] extensions;

    FileType( String[] extensions )
    {
        this.extensions = extensions;
    }

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

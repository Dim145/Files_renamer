package renameFiles.metier.enums;

public enum Definitions
{
    SD (480 ),
    HD (720 ),
    FHD(1080),
    UHD(2160),
    UHD_8K (4320);

    private final int qualiter;

    Definitions(int qualiter)
    {
        this.qualiter = qualiter;
    }

    public int getQualiter()
    {
        return this.qualiter;
    }
}

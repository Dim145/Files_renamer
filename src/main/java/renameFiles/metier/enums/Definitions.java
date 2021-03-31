package renameFiles.metier.enums;

public enum Definitions
{
    SD ("SD" , 480 ),
    HD ("HD" , 720 ),
    FHD("FHD", 1080),
    UHD("UHD", 2160),
    UHD_4K("4K", 2160),
    UHD_8K ("8K", 4320),
    UHD_16K("16K", 8640),
    UHD_32K("32K", 17280);

    public static Definitions getByPixels(int qualiter)
    {
        for (Definitions def : Definitions.values())
            if( def.getQualiter() == qualiter )
                return def;

        return null;
    }


    private final int    qualiter;
    private final String ecriture;

    Definitions(String ecriture, int qualiter)
    {
        this.qualiter = qualiter;
        this.ecriture = ecriture;
    }

    public int getQualiter()
    {
        return this.qualiter;
    }

    @Override
    public String toString()
    {
        return this.ecriture;
    }
}

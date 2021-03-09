package renameFiles.metier.types;

import renameFiles.ihm.dialogs.DialogAvancement;

public abstract class AbstractListe
{
    public abstract String  traitement(DialogAvancement dialog);
    public abstract boolean add(Object o);
}

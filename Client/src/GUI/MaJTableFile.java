package GUI;

import Tools.MaJTable;
import Tools.MonModele;
import cloudBox.CloudFile;
import java.util.Collection;

public class MaJTableFile extends MaJTable<CloudFile> {
    
    private Collection<CloudFile> data;
    
    private String[] titres = {"Author", "Name File"};
    private String[] methodes = { "getOwner", "getNameFile"};

    private int[] largeurs = {50, 150};

    public MaJTableFile() {
        super();
    }

    public MaJTableFile(Collection<CloudFile> data) {
        super();
        setData(data);
    }

    public void setPresentation(String[] titres, String[] methodes,
                                                            int[] largeurs) {
        this.titres=titres;
        this.methodes=methodes;
        this.largeurs=largeurs;
        if (data!=null) {
            setData(data);
        }
    }

    public void setData(Collection<CloudFile> col) {
        this.data=col;
        setModel(new MonModele<CloudFile>(col,titres,methodes));
        setColumnWidth(largeurs);
    }
    
}

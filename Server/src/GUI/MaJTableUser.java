package GUI;

import Tools.MaJTable;
import Tools.MonModele;
import cloudBox.User;
import java.util.Collection;

public class MaJTableUser extends MaJTable<User> {
    
    private Collection<User> data;
    private String[] titres = { "Author", "NbrFile" };
    private String[] methodes = { "getName", "getNbrFile" };

    private int[] largeurs = {50, 150};

    public MaJTableUser() {
        super();
    }

    public MaJTableUser(Collection<User> data) {
       super();
       setData(data);
    }

    public void setPresentation(String[] titres, String[] methodes, int[] largeurs) 
    {
        this.titres=titres;
        this.methodes=methodes;
        this.largeurs=largeurs;
        if (data!=null) {
            setData(data);
        }
    }

    public final void setData(Collection<User> col) {
        
        this.data=col;
        setModel(new MonModele<>(col,titres,methodes));
        setColumnWidth(largeurs);
    }
    
}

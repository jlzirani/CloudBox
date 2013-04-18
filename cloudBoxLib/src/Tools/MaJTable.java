package Tools;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Classe générique de présentation de liste d'objets
 * @author ADT
 */

public class MaJTable<T> extends JPanel {

    private JTable maTable;
    private MonModele<T> monModele;
    private T objSelected = null;
    public static final String EVENT_DOUBLECLICK="DoubleClick", 
		EVENT_SELECTED="Selected", 
		EVENT_UNSELECTED="Unselected";


    protected MaJTable(MonModele<T> monModele) {
        this.monModele=monModele;
        setLayout(new BorderLayout());
        maTable = new JTable(monModele);
        add(new JScrollPane(maTable));
        maTable.setDragEnabled(false);
        maTable.getTableHeader().setReorderingAllowed(false);
        maTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gereEvtSelections();
    }

    protected MaJTable() {
        setLayout(new BorderLayout());
        maTable = new JTable();
        add(new JScrollPane(maTable));
        maTable.setDragEnabled(false);
        maTable.getTableHeader().setReorderingAllowed(false);
        maTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gereEvtSelections();
    }

    /**
     *
     * @param monModele
     */
    protected  void setModel(MonModele<T> monModele) {
        this.monModele = monModele;
        maTable.setModel(monModele);
    }

    /**
     * retourne le nombre de lignes du modèle
     * @return
     */
    public int getRowCount() {
        
        return maTable.getRowCount();
    }

    /**
     * retourne le numéro de la ligne sélectionnée,
     * -1 si rien n'est s�lectionné
     * @return
     */

    public int getSelectedRow() {
        return maTable.getSelectedRow();
    }

    /**
     * retourne l'objet de type T sélectionné
     * @return
     */
    
    public T getSelectedObject() {
        if (maTable.getSelectedRow() < 0) {
            return null;
        } else {
            return monModele.getObject(maTable.getSelectedRow());
        }
    }

    private void gereEvtSelections() {
        maTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                //ne prend pas en compte l'événement de préparation
                // à la sélection
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    firePropertyChange(EVENT_UNSELECTED, objSelected, null);
                    objSelected = null;
                } else {
                    int selectedRow = lsm.getMinSelectionIndex();
                    firePropertyChange(EVENT_SELECTED, objSelected,
                            getSelectedObject());
                    objSelected = getSelectedObject();
                }
            }
        });
        
        maTable.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    doubleClick();
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }
    

    private void doubleClick(){
        firePropertyChange(EVENT_DOUBLECLICK, null, objSelected);
    }


    /**
     * spécifie la largeur d'affichage des colonnes
     * @param largeurs tableau des largeurs de colonnes à appliquer
     */
    public void setColumnWidth(int[] largeurs){
        int i=0;
        while (i<largeurs.length && i<maTable.getColumnCount()){
            maTable.getColumn(maTable.getColumnName(i)).setPreferredWidth(largeurs[i]);
            i++;
        }
        
    }     
}

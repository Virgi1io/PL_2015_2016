package ASA;

import SLR.ReglaAPV;
import java.util.ArrayList;

/**
 *
 * @author Juan Antonio Contreras Fernández.
 */
public class Gramatica {
    
    private ArrayList<Definicion> ListaDefinciones = new ArrayList<Definicion>();
    
    /**
     * Añade una nueva definicion al conjunto de la gramatica.
     * @param def 
     */
    public void addDefinicion(Definicion def)
    {
        ListaDefinciones.add(def);
    }
    
    /**
     * Devuelve una lista con todas las reglas preparadas para el automata
     * de prefijos viables.
     * @return 
     */
    public ArrayList<ReglaAPV> getReglasAPV()
    {
        ArrayList<ReglaAPV> devolver = new ArrayList<ReglaAPV>();
        for(Definicion d: ListaDefinciones)
        {
            devolver.addAll(d.getReglasAPV());
        }
        return devolver;
    }
    
}

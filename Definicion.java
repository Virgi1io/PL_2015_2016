package ASA;

import java.util.ArrayList;
import java.util.List;
import SLR.ReglaAPV;

/**
 * Esta clase define a una definicion de la gramatica junto con su
 * conjunto de reglas asignadas.
 * 
 * @author Juan Antonio Contreras Fernández
 * Curso: 2015/2016
 */
public class Definicion {
    
    private final Simbolo id;
    private List<Regla> ListaReglas = new ArrayList<Regla>();
    
    /**
     * Constructor de la clase defincion id sera el simbolo inicial de
     * la definicion.
     * 
     * @param _id 
     */
    public Definicion(Simbolo _id)
    {
        id = _id;
    }
    
    /**
     * Devuelve el simbolo id de la definicion.
     * @return Simbolo
     */
    public Simbolo getid()
    {
        return id;
    }
    
    /**
     * Añade una nueva regla a la lista de la definicion.
     * @param rule
     */
    public void addRegla(Regla rule)
    {
        ListaReglas.add(rule);
    }
    
    /**
     * Devuelve el conjunto de reglas de la definicion completa en forma de reglas para SLR.
     * @return ArrayList
     */
    public ArrayList<ReglaAPV> getReglasAPV()
    {
        ArrayList<ReglaAPV> Reglas = new ArrayList<>();
        for(Regla r:ListaReglas)
        {
            Reglas.add(new ReglaAPV(id, r.getListaSimbolos(), 0, 0));
        }
        return Reglas;
    }
    
}

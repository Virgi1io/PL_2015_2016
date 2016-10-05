package ASA;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Clase que define una regla con su conjunto de simbolos.
 * 
 * @author Juan A. Contreras Fernández
 */
public class Regla {
    
    //Conjunto de simbolos de la regla.
    private ArrayList<Simbolo> ListaSimbolos = new ArrayList<Simbolo>();
    
    /**
     * Permite saber si una regla es de tipo lambda.
     * @return 
     */
    public boolean esLambda()
    {
        return ListaSimbolos.isEmpty();
    }
    
    /**
     * Añade un nuevo simbolo al conjunto de la regla.
     * 
     * @param nuevo 
     */
    public void add(Simbolo nuevo)
    {
        ListaSimbolos.add(nuevo);
    }
    
    /**
     * Devuelve la lista de simbolos de la regla.
     * @return ArrayList
     */
    public ArrayList<Simbolo> getListaSimbolos()
    {
        return ListaSimbolos;
    }

    /**
     * Sobrecarga del metodo equals para la comparacion de reglas entre si
     * @param rule
     * @return boolean
     */
    @Override
    public boolean equals(Object rule)
    {
        if(rule instanceof Regla)
        {
            Regla aux = (Regla)rule;
            
            if(aux.ListaSimbolos.size() != ListaSimbolos.size())
                    return false;
            for(int i = 0; i < ListaSimbolos.size(); i++)
            {
                if(!ListaSimbolos.get(i).equals(aux.ListaSimbolos.get(i)))
                    return false;
            }
        }
        return true;
    }
}

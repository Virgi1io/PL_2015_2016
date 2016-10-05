package SLR;

import ASA.Gramatica;
import ASA.Simbolo;
import java.util.ArrayList;

/**
 * Clase que maneja el desarrollo del automata de prefijos viables y controla su expansion
 * @author Juan Antonio Contreras Fernández
 */
public class APV 
{
    private final Estado inicial;//Estado inicial del sistema
    private Estado actual;//Estado actual de generacion del sistema
    private final ArrayList<ReglaAPV> iniciales;//Lista de reglas del automata.
    private int nEstados = 0;//Numero de estados del automata.
    TablaDR table = null;
    
    /**
     * Crea a partir de una gramatica el automata de prefijos viables
     * @param gram
     */
    public APV(Gramatica gram)
    {
        //Conjunto de reglas que tendra el estado inicial.
        ArrayList<ReglaAPV> Estado_inicial = new ArrayList<>();
        
        //Obtenemos todas las reglas de la gramatica en forma de ReglaAPV.
        iniciales = gram.getReglasAPV();
        for(int i = 1; i <= iniciales.size(); i++)
        {
            iniciales.get(i-1).setNumero(i);
        }
        
        //Creamos la regla inicial x-> simbolo inicial.
        ArrayList<Simbolo> primero = new ArrayList<Simbolo>();
        primero.add(iniciales.get(0).getInicial());
        ReglaAPV primera = new ReglaAPV(new Simbolo("X",0),primero, 0, 0);
        Estado_inicial.add(primera);
        
        //Desarrollamos las reglas a partir de la inicial.
        ArrayList<ReglaAPV> temporal = (ArrayList<ReglaAPV>) iniciales.clone();
        if(primera.getSiguiente().tipo() == 0)//Si no es terminal se añaden los productos de dicho simbolo
        {
            for(int i = 0; i < Estado_inicial.size(); i++)//Para cada regla del estado
            {
                if(Estado_inicial.get(i).getSiguiente().tipo() == 0)
                {
                    for(int j = 0; j < temporal.size(); j++)//Buscamos las reglas producidas
                    {
                        if(Estado_inicial.get(i).getSiguiente().equals(temporal.get(j).getInicial()))
                        {
                            Estado_inicial.add(temporal.remove(j));
                            j--;//AL usar remove la lista reduce en 1 su capacidad y necesitamos volver.
                        }
                    }
                }
            }
        }
        
        //Seteamos los estados inicial y actal al unico existente.        
        this.inicial = new Estado(Estado_inicial, new Simbolo("",0));
        inicial.setNumero(0);
        this.actual = this.inicial;
        System.out.println("Generado estado inicial: ");
        System.out.println("Estado " + inicial.getNestado() + inicial.getTransicion().getNombre());
    }
    
    /**
     * Si el estado existe devuelve dicho estado para referenciarlo, si no devuelve null.
     * @param _e
     * @return Estado
     */
    public Estado buscar(Estado _e)
    {
        ArrayList<Estado> visitados = new ArrayList<>();
        ArrayList<Estado> pendientes = new ArrayList<>();
        pendientes.add(inicial);
        
        //Recorrido en anchura para ver si ya existe el estado
        while(pendientes.size() > 0)
        {
            Estado act = pendientes.remove(0);
            if(act.equals(_e))
                return act;
            else
            {
                visitados.add(act);
                pendientes.addAll(act.getListaSiguientes());
                pendientes.removeAll(visitados);
            }
        }        
        return null;
    }
    
    /**
     * Desarrolla el algoritmo SLR sobre el estado inicial creado y desarrolla el
     * automata de prefijos viables.
     * 
     */
    public void slr()
    {
        ArrayList<Estado> Pendiente = new ArrayList<>();
        ArrayList<Estado> Visitados = new ArrayList<>();
        
        Pendiente.add(inicial);
        //Mientras queden producciones en el estado.
        while(Pendiente.size() > 0)
        {
            actual = Pendiente.remove(0);
            System.out.println("Evaluando estado " + actual.getNestado() + "....");
            if(Visitados.indexOf(actual) == -1)
            {   
                while(!actual.getProducciones())
                {
                    //Generamos un nuevo estado
                    Estado aux = actual.generaSiguiente(iniciales);
                    Estado find = buscar(aux);
                    //Si el estado no existia lo añadimos a siguientes
                    if(find == null)
                    {
                        nEstados++;
                        aux.setNumero(nEstados);
                        System.out.println("E" + actual.getNestado() +":" + " Transicion " + 
                                aux.getTransicion().getNombre() + " Genera el estado "
                                                            + aux.getNestado() + ".");
                        actual.addSiguiente(aux);
                    }
                    //Si existe lo linkamos a siguiente
                    else
                    {
                        System.out.println("E"+ actual.getNestado() + ":" + " Transicion " + 
                                            find.getTransicion().getNombre() + " Enlaza el estado " + 
                                            find.getNestado() + " .");
                        actual.addSiguiente(find);
                    }
                }
                //Añadimos los siguientes generados.
                Pendiente.addAll(actual.getListaSiguientes());
                Visitados.add(actual);
                Pendiente.removeAll(Visitados);
            }
        }
            
    }
    
    /**
     * Inicia la tabla con el tamaño designado por los simbolos y los estados.
     * @param Terminales
     * @param Noterminales 
     */
    public void Iniciar_Tabla(ArrayList<String> Terminales, ArrayList<String> Noterminales)
    {
        table = new TablaDR(Terminales, Noterminales, iniciales, nEstados+1);
    }
    
    /**
     * Crea la tabla de desplazamiento/reduccion despues de haber creado el automata.
     * @return Tabla
     * @throws java.lang.Exception
     */
    public TablaDR Fill_Table() throws Exception
    {
        if(table == null)
            throw new Exception("Tabla no inicializada");
        
        ArrayList<Estado> Pendiente = new ArrayList<>();
        ArrayList<Estado> Visitado = new ArrayList<>();
        actual = inicial;
        Pendiente.add(actual);
        
        while(!Pendiente.isEmpty())
        {
            actual = Pendiente.remove(0);//Cogemos el siguiente 
            //Primero evaluamos el estado actual.
            int tipo = actual.getTipoEstado();
            System.out.println("Evaluando estado " + actual.getNestado() + ":...........................");
            if(tipo == Integer.MIN_VALUE)
            {
                System.out.println("Estado con x->Expr., añado aceptar con $");
                table.addAction(actual.getNestado(), "EOF", Integer.MIN_VALUE);//Añadimos accion aceptar.
            }                
            if(tipo < 0 && tipo > Integer.MIN_VALUE)
            {
                //Calculamos los siguietes al numero de la regla.
                System.out.println("E"+actual.getNestado()+": Accion_reduccion por regla "+ (-tipo) );
                ArrayList<Simbolo> siguientes = getSiguientes(iniciales.get(tipo*(-1)-1).getInicial());
                for(int i = 0; i < siguientes.size(); i++)
                {
                    System.out.println("\t Simbolo: "+ siguientes.get(i).getNombre() + " reducir por regla " + 
                                        (-tipo));
                    table.addAction(actual.getNestado(), siguientes.get(i).getNombre(), tipo);
                }
            }
            for(Estado e: actual.getListaSiguientes())
            {
                if(e.getTransicion().tipo() == 0)
                {
                    System.out.println("E" + actual.getNestado() + ": Ir_a " + e.getNestado() + " con " + 
                                        e.getTransicion().getNombre());
                    table.addGoTo(actual.getNestado(), e.getTransicion().getNombre(), e.getNestado());
                }
                else
                {
                    System.out.println("E" + actual.getNestado() + ": Accion_desplazar_a " + 
                                        e.getNestado() + " con " + e.getTransicion().getNombre());
                    table.addAction(actual.getNestado(), e.getTransicion().getNombre(), e.getNestado());
                }
            }
            Visitado.add(actual);//Añadimos a visitados
            Pendiente.addAll(actual.getListaSiguientes());//Añadimos los siguientes
            //Evitamos la redundancia.
            Pendiente.removeAll(Visitado);                       
        }
        return table;
    }
    
    /**
     * Devuelve una lista con los primeros del simbolo s dado
     * @param s
     * @return ArrayList
     */
    public ArrayList<Simbolo> getPrimeros(Simbolo s)
    {
        ArrayList ret = new ArrayList<>(), aux = new ArrayList<>();
                
        //Para cada regla del conjunto.
        for(ReglaAPV r : iniciales)
        {            
            if(r.getInicial().equals(s) && !r.getInicial().equals(r.getSiguiente()))
            {
                if(r.getSiguiente().tipo()==0)//Si es no terminal llamada recursiva.
                {
                    //Buscamos los primeros del simbolo
                    aux = getPrimeros(r.getSiguiente());
                    
                    //Mientras primero incluya lambda seguimos avanzando.
                    if(aux.indexOf(new Simbolo("Lambda",1)) != -1);
                    {                        
                        ReglaAPV rAux = r.transicion(r.getSiguiente());
                        if(rAux != null)
                        {
                            ArrayList<Simbolo> temp = getPrimeros(rAux.getSiguiente());
                            aux.removeAll(temp);
                            aux.addAll(temp);
                        }
                    }
                    ret.removeAll(aux);
                    ret.addAll(aux);
                }
                else
                    ret.add(r.getSiguiente());//Si es terminal añade a la lista.                    
            }            
        }
        return ret;
    }
    
    /**
     * Devuelve una lista de simbolos siguientes al simbolo dado.
     * @param s
     * @return 
     */
    public ArrayList<Simbolo> getSiguientes(Simbolo s)
    {
        ArrayList<Simbolo> ret = new ArrayList<>();
        
        //Si es la regla inicial se añade el dolar.
        if(iniciales.get(0).getInicial().equals(s))
            ret.add(new Simbolo("EOF",1));
        
        //Para cada regla del conjunto.
        for(ReglaAPV r: iniciales)
        {
            Simbolo aux = r.getSiguienteA(s);
            //Si existe un simbolo siguiente a "s" en la regla.
            if(!aux.getNombre().equals("vacio"))
            {
                if(aux.tipo() == 1)
                {
                    ret.remove(aux);//Eliminamos para no repetir.
                    ret.add(aux);
                }
                else
                {
                    //Si ha terminado la regla mis siguientes son sus siguientes.
                    if(aux.equals(r.getInicial()))
                    {
                        ArrayList<Simbolo> sigAux = getSiguientes(aux);
                        ret.removeAll(sigAux);
                        ret.addAll(sigAux);
                    }
                    //Si el simbolo es no terminal, añado sus primeros.
                    else
                    {
                        ArrayList<Simbolo> primAux = getPrimeros(aux);
                        //Si entre sus primeros existe un lambda añado sus siguientes.
                        if(primAux.indexOf(new Simbolo("Lambda",1)) != -1)
                        {
                            ArrayList<Simbolo> sigAux = getSiguientes(aux);
                            primAux.removeAll(sigAux);
                            primAux.addAll(sigAux);
                        }
                        ret.removeAll(primAux);
                        ret.addAll(primAux);
                    }    
                }
            }
        }        
        return ret;
    }
}

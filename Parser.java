package analizador_sintactico;

//------------------------------------------------------------------//

import Analizador_Lexico.Token;
import Analizador_Lexico.TokenConstants;
import Analizador_Lexico.JLexer;
import ASA.Definicion;
import ASA.Gramatica;
import ASA.Regla;
import ASA.Simbolo;
import SLR.APV;
import SLR.TablaDR;
import Generadores_de_Archivos.Parser_Writer;
import Generadores_de_Archivos.Simbol_Writer;
import java.util.ArrayList;

/**
 * 
 *  
 * @author Juan A. Contreras Fernández
 *
 */
public class Parser implements TokenConstants {

	/**
	 * Analizador lexico
	 */
	private JLexer lexer;

	/**
	 * Siguiente token de la cadena de entrada
	 */
	private Token nextToken;

        /**
         * Objeto para escribir los ficheros .java necesarios para la practica.
         */
        Simbol_Writer ficheros = null;
        
        /**
         * Conjunto de caracteres de la gramatica para escribir en los ficheros
         * y crear la tabla de desplazamiento/reduccion SLR.
         *
        */
        ArrayList<String> Terminales = new ArrayList<>();
        ArrayList<String> Noterminales = new ArrayList<>();
        
	/**
	 * Metodo para el analisis de un fichero.
	 * 
	 * @param filename
	 * @return boolean
	 */
	public boolean parse(String filename) {
		try {
			this.lexer = new JLexer(filename);
			this.nextToken = lexer.getNextToken();   
                        //Añadimos el simboo EOF
                        Terminales.add("EOF");
                        //Parseamos la gramatica.
                        Gramatica g =  parseGramatica();
                        //Escribimos TokenConstants y SymbolConstants
                        ficheros = new Simbol_Writer();
                        ficheros.escribe(Terminales, Noterminales);
                        System.out.println("Fichero TokenConstants.java creado----------------------------");
                        System.out.println("Fichero SymbolConstants.java creado---------------------------");
                        System.out.println("Gramatica generada--------------------------------------------");
                        //Creacion del automata de prefijos variables
                        APV slr = new APV(g);                        
                        slr.slr();                        
                        System.out.println("\n\nAnalisis SLR generado-----------------------------------------");
                        slr.Iniciar_Tabla(Terminales, Noterminales);
                        System.out.println("\nGenerando Tabla de Desplazamiento/Reduccion");
                        TablaDR tabla = slr.Fill_Table();
                        Parser_Writer Pw = new Parser_Writer();
                        Pw.Escribir_Tabla(tabla, Terminales, Noterminales);
                        System.out.println("\nTabla de reduccion generada-----------------------------------");
                        
                    return nextToken.getKind() == EOF;
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return false;
		}
	}

	/**
	 * Punto de entrada para ejecutar pruebas del analizador sintactico
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0) return;

		Parser parser = new Parser();
		if(parser.parse(args[0])) {
			System.out.println("Correcto");
		} else {
			System.out.println("Incorrecto");
		}
	}

	/**
	 * Metodo que consume un token de la cadena de entrada
	 * @param kind
	 * @throws SintaxException
	 */
	private void match(int kind) throws SintaxException {
		if(nextToken.getKind() == kind) nextToken = lexer.getNextToken();
		else throw new SintaxException(nextToken,kind);
	}

	/**
	 * Analiza el simbolo <Gramatica>
	 * @throws SintaxException
	 */
	private Gramatica parseGramatica() throws SintaxException {
		int[] expected = { NOTERMINAL, EOF };
                Gramatica g = new Gramatica();
		switch(nextToken.getKind()) {
			case NOTERMINAL://Simbolo inicial de gramatica                                
                                parseGramaticaP(g);
                                return g;
			case EOF://Gramatica vacia
                                return g;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el simbolo <GramaticaP>
	 * @throws SintaxException
	 */
	private void parseGramaticaP(Gramatica g) throws SintaxException {
		int[] expected = { NOTERMINAL, EOF };
		switch(nextToken.getKind()) {
			case NOTERMINAL://Simbolo de definicion
				parseDefinicion(g);
                                parseGramaticaP(g);//Siguiente simbolo no terminal
				break;
			case EOF://Gramatica terminada.
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el simbolo <Definicion>
	 * @throws SintaxException
	 */
	private void parseDefinicion(Gramatica g) throws SintaxException {
		int[] expected = { NOTERMINAL };
		switch(nextToken.getKind()) {
			case NOTERMINAL:         
                                //Creamos una regla y la deficion que la contendra.
                                Regla r = new Regla();
                                Definicion d = new Definicion(new Simbolo(nextToken.getLexeme(), 0));
                                //Guardamos el simbolo Noterminal encontrado.
                                Noterminales.remove(nextToken.getLexeme());
                                Noterminales.add(nextToken.getLexeme());
                                //Consumimos los caracteres
				match(NOTERMINAL);
				match(EQ);
                                //Analizamos la lista de reglas de la definicion.
				parseListaReglas(d, r);
                                match(SEMICOLON);                                
                                g.addDefinicion(d); 
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el simbolo <ListaReglas>
	 * @throws SintaxException
	 */
	private void parseListaReglas(Definicion d, Regla r) throws SintaxException {
		int[] expected = { NOTERMINAL, TERMINAL, SEMICOLON };
		switch(nextToken.getKind()) {
			case NOTERMINAL:
                        case TERMINAL:
                        case SEMICOLON:
                                //Analizamos la regla
				parseRegla(d, r);
                                //Encadenamos con el resto de las reglas de la definicion.
                                parseListaReglasP(d, r);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el simbolo <ListaReglasP>
	 * @throws SintaxException
	 */
	private void parseListaReglasP(Definicion d, Regla r) throws SintaxException {
		int[] expected = { BAR, SEMICOLON};
		switch(nextToken.getKind()) {
			case BAR:
                                match(BAR);//Concatenacion de reglas
                                d.addRegla(r);
                                r = new Regla();
                                parseRegla(d, r);
				parseListaReglasP(d, r);
				break;
			case SEMICOLON://Final del conjunto de reglas de la definicion.
                                d.addRegla(r);
                                r = new Regla();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el simbolo <Regla>
	 * @throws SintaxException
	 */
	private void parseRegla(Definicion d, Regla r) throws SintaxException {
		int[] expected = { NOTERMINAL, TERMINAL, BAR, SEMICOLON };
		switch(nextToken.getKind()) {
			case NOTERMINAL:
			case TERMINAL:
			case BAR:
			case SEMICOLON:
				parseReglaP(d, r);//Analizamos la regla
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el simbolo <ReglaP>
	 * @throws SintaxException
	 */
	private void parseReglaP(Definicion d, Regla r) throws SintaxException {
		int[] expected = { NOTERMINAL, TERMINAL, BAR, SEMICOLON };
		switch(nextToken.getKind()) {
			case NOTERMINAL:
                        case TERMINAL://Analizamos el simbolo encontrado y lo añádimos
				parseSimbolo(d, r);
                                parseReglaP(d, r);
				break;                                                
                        case BAR:
                        case SEMICOLON:
                                break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el simbolo <Simbolo>
	 * @throws SintaxException
	 */
	private void parseSimbolo(Definicion d, Regla r) throws SintaxException {
		int[] expected = { NOTERMINAL, TERMINAL };
		switch(nextToken.getKind()) {
			case NOTERMINAL://Añadimos simbolo noterminal a la regla                                
                                r.add(new Simbolo(nextToken.getLexeme(), 0));                                
				match(NOTERMINAL);
				break;
			case TERMINAL://Añadimos simbolo terminal a la regla.
                                String next = nextToken.getLexeme();
                                //Tratamiento para escritura de TokenConstants
                                next = next.replace('<', ' ');
                                next = next.replace('>', ' ');
                                Terminales.add(next);//Guardamos el simbolo
                                r.add(new Simbolo(next, 1));//Lo añadimos a la regla.
				match(TERMINAL);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
}

package Analizador_Lexico;

/**
 * Autor Francisco Jose Moreno Velo.
 * Modificacion: Juan Antonio Contreras Fernández.
 * Curso: 2015/2016.
 * Asignatura: Procesadores de lenguaje.
 * 
 * Fichero: JLexer.java
 * Descripcion: clase principal del lexer implementado para el trabajo final.
 */



import java.io.*;

/**
 * Clase que implementa el analizador lexico de la practica final
 * 
 * @author Juan A. Contreras Fernández
 *
 */
public class JLexer extends Lexer implements TokenConstants {

	/**
	 * Transiciones del automata del analizador lexico
	 * 
	 * @param state 
	 * @param symbol 
	 * @return Estado
	 */
	protected int transition(int state, char symbol) {
		switch(state) {
			case 0: 
                                if(symbol == ' ' || symbol == '\n') return 2;
                                else if(symbol == '\t' || symbol == '\r') return 2;
                                else if(symbol == '/') return 3;
				else if(symbol >= 'a' && symbol <= 'z') return 7;
				else if(symbol >= 'A' && symbol <= 'Z') return 7;
				else if(symbol == '<') return 8;
				else if(symbol == ':') return 11;
				else if(symbol == '|') return 14;
				else if(symbol == ';') return 15;			
			case 3:
				if(symbol == '*') return 4;
                                else return -1;
                        case 4:
                                if(symbol == '*') return 5;
                                else 
                                    return 4;                                
			case 5:
				if(symbol == '/') return 6;
				else if(symbol == '*') return 5;
                                else
                                    return 4;
			case 7:
				if(symbol >= 'a' && symbol <= 'z') return 7;
				else if(symbol >= 'A' && symbol <= 'Z') return 7;
			case 8:
				if(symbol >= 'a' && symbol <= 'z') return 9;
				else if(symbol >= 'A' && symbol <= 'Z') return 9;
				else return -1;
			case 9:
                                if(symbol >= 'a' && symbol <= 'z') return 9;
				else if(symbol >= 'A' && symbol <= 'Z') return 9;
                                else if(symbol >= '0' && symbol <= '9') return 9;
                                else if(symbol == '>') return 10;
				else return -1;
			case 11:
				if(symbol == ':') return 12;
				else return -1;
			case 12:
				if(symbol >= '=') return 13;
				else return -1;
			case 15:
				if(symbol == '\'') return 16;
				else return -1;
                        default:
                            return -1;
		}
	}
	
	/**
	 * Verifica si un estado es final
	 * 
	 * @param state Estado
	 * @return true, si el estado es final
	 */
	protected boolean isFinal(int state) {
		if(state <=0 || state > 42) return false;
		switch(state) {
			case 0:
			case 1:
			case 3:
			case 4:
			case 5:
			case 8:
			case 9:
			case 11:
			case 12:
				return false;
			default: 
				return true;
		}
	}
	
	/**
	 * Genera el componente l�xico correspondiente al estado final y
	 * al lexema encontrado. Devuelve null si la acci�n asociada al
	 * estado final es omitir (SKIP).
	 * 
	 * @param state Estado final alcanzado
	 * @param lexeme Lexema reconocido
	 * @param row Fila de comienzo del lexema
	 * @param column Columna de comienzo del lexema
	 * @return Componente l�xico correspondiente al estado final y al lexema
	 */
	protected Token getToken(int state, String lexeme, int row, int column) {
		switch(state) {
			case 2: return null;
			case 6: return null;
			case 7: return new Token(NOTERMINAL,lexeme, row, column);
			case 10: return new Token(TERMINAL, lexeme, row, column);
			case 13: return new Token(EQ, lexeme, row, column);
			case 14: return new Token(BAR, lexeme, row, column);
			case 15: return new Token(SEMICOLON, lexeme, row, column);
			default: return null;
		}
	}	
	
	/**
	 * Constructor de la clase
	 * @param filename Nombre del fichero fuente
	 * @throws IOException En caso de problemas con el flujo de entrada
	 */
	public JLexer(String filename) throws IOException {
		super(filename);
	}
	
	/**
	 * Punto de entrada para ejecutar pruebas del analizador lexico
	 * @param args
	 */
	public static void main(String[] args) {
                //Si no hay archivo termina
		if(args.length == 0) return;
		try {
			JLexer lexer = new JLexer(args[0]);
			Token tk;
			do {
				tk = lexer.getNextToken();
				System.out.println(tk.toString());
			} while(tk.getKind() != Token.EOF);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

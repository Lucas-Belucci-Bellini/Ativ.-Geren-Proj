/**
 * Ponto de entrada simples do projeto.
 * Mantém o exemplo de inicialização e demonstra um laço de repetição no terminal.
 */
public class Main {

    /** Método chamado pela JVM quando o programa é iniciado. */
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        // Demonstra cinco iterações e a impressão do valor atual do contador.
        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }
    }
}

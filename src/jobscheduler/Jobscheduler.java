/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

import java.io.IOException;


/**
 *
 * Classe principal, que contei o metodo principal do simulador Job Scheduler
 */
public class Jobscheduler {
    /**
     * A variavel running permite saber em que momento todos os processos terminaram
     * a sua execução
    */
    
    public static boolean running = true;
    public static pipeline pipeline;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        /*
        Inicialização do objecto pipeline que é a classe responsavel por actualizar
        os varios stages e incrementar o nmero de ciclos
        */
        pipeline = new pipeline();
        /*
        Leitura e carregamentos dos dados do ficheiro dados.txt que 
        contém todos os processos
        */
        fileInput file = new fileInput("dados.txt");
        file.carregaDados();
        
        do {
            pipeline.executa();
           
        } while (running);
    }
}

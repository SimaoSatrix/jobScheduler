/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Le o conteudo de um ficheiro que contem os dados a serem processados pelo
 * simulador.
 */
public class fileInput {

    FileReader file;
    BufferedReader br;
    ArrayList<String> dados = new ArrayList<String>();

    /**
     * Inicializa a leitura do ficheiro, lendo o conteudo inteiro do ficheiro.
     *
     * @param caminho
     * @throws java.io.FileNotFoundException
     */
    public fileInput(String caminho) throws FileNotFoundException, IOException {

        try {
            this.file = new FileReader(caminho);
            this.br = new BufferedReader(file);
            init();
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Erro de I/O!");
            System.exit(0);
        }
    }

    /**
     * Inicializa a leitura do ficheiro, lendo o conteudo inteiro do ficheiro.
     *
     * @throws IOException
     */
    private void init() throws IOException {
        String out = "";
        while ((out = br.readLine()) != null) {
            dados.add(out);
        }
        int num_p = dados.size();
        int cont = num_p - 10;
        if (num_p > 10) {
            System.out.println("O numero máximo de processos em execução simultânea é 10, neste momento existem " + num_p + " processos");
            System.out.println("Apenas serão executados os 10 primeiros processos...");

            while (dados.size() > 10) {
                dados.remove(cont + 9);
                cont--;
            }

        }
    }

    /**
     * Retorna o numero de processos.
     *
     * @return
     */
    public int getNumero_processos() {
        int line = dados.size();
        Collections.sort(dados);
        return line;
    }

    /**
     * Retorna o tempo do processo quando chega ao stage NEW
     *
     * @param proc
     * @return
     */
    public int getTempo_processo(int proc) {
        String aux = dados.get(proc);
        String[] time = aux.split(" ");
        return Integer.valueOf(time[0]);
    }

    /**
     * Retorna os dados associados a um processo n.
     *
     * @param n
     * @return
     */
    public int[] getDados_processo(int n) {

        String aux = this.dados.get(n);
        String[] d = aux.split((" "));

        int[] result = new int[d.length-1];
        for (int i = 1; i < result.length; i++) {
            result[i - 1] = Integer.valueOf(d[i]);
        }

        return result;
    }

    /**
     * Carrega os dados lidos para o pipeline
     *
     *
     */
    public void carregaDados() {

        int num_processos = getNumero_processos();
        pipeline.numProcesses = num_processos;
        for (int i = 0; i < num_processos; i++) {
            int tempoinicial = this.getTempo_processo(i);
            PCB pcb = new PCB();
            Pagina pagina = new Pagina();
            pcb.setPid(pcb.proximo_Pid());
            pipeline.Lista_ids.add(pcb.getPid());
            pcb.setInstante_inicial(tempoinicial);
            pagina.preencherPagina(pcb, this.getDados_processo(i));
            jobscheduler.pipeline.processo_a_admitir(pcb);

        }
    }

}

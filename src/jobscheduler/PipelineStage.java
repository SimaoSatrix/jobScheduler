/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

/**
 * Classe abstracta que contém metodos partilhados em todos os stages da
 * pipeline
 */
import java.util.LinkedList;

public abstract class PipelineStage {

    protected LinkedList<PCB> listadeespera;
    protected int estado_do_pipeline;
    protected LinkedList<PCB> listaprocessosadmitidos;
    protected int gotobegin_max;
    protected int fork_max;

    public PipelineStage(int tipo_estado) {
        estado_do_pipeline = tipo_estado;
    }

    /**
     * Adiciona uma processo à lista de espera
     *
     * @param processo
     */
    public void adiciona_processo_lista_espera(PCB processo) {
        listadeespera.add(processo);
    }

    /**
     * Obtem tamanho da lista de processos
     *
     * @return
     */
    public int tamanho_lista_processos() {
        return listadeespera.size();
    }

    /**
     * Actualiza e gere os processos que estao em filaespera no estado
     *
     * @param nr_ciclos
     */
    public abstract void actualiza_estado(int nr_ciclos);

    /**
     * Faz o output do tempo de espera no estado ready de cada processo
     * @param i
     * @return 
     */
    public int output_estado_processos(int i) {
        int aux = -2;
        for (PCB pcb : listadeespera) {
            if (pcb.getPid() == i) {
                aux = pcb.getEstado();
            }
        }
        return aux;
    }

}

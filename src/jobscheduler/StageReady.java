/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

import java.util.LinkedList;

/**
 * Gere os processos em estado Ready
 */
public class StageReady extends PipelineStage {

    public StageReady(int tipo_estado) {
        super(tipo_estado);
        listadeespera = new LinkedList<>();
        listaprocessosadmitidos = new LinkedList<>();

    }

    /**
     * Actualiza e gere os processos em fila de espera
     *
     * @param nr_ciclos
     */
    @Override
    public void actualiza_estado(int nr_ciclos) {

        if (listaprocessosadmitidos.size() > 0) {

            for (PCB pcb : listaprocessosadmitidos) {

                pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_READY].adiciona_processo_lista_espera(pcb);

            }
            listaprocessosadmitidos.clear();
        }

        if (listadeespera.size() > 0 && listadeespera.getFirst().getCiclo_actividade() < nr_ciclos
                && pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_RUN].tamanho_lista_processos() == 0) {
            PCB processo = listadeespera.getFirst();
            //listaprocessosadmitidos.add(processo);
            processo.setEstado(Pipeline_ouput.PIPELINE_STAGE_RUN);

            pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_RUN].adiciona_processo_lista_espera(listadeespera.remove());
            processo.setCiclo_actividade(nr_ciclos);
        }
    }
}

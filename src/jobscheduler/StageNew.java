/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Gere os processos no estado New
 */
public class StageNew extends PipelineStage {

    public StageNew(int tipo_estado) {
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
        if (listadeespera.size() > 0 && listadeespera.getFirst().getCiclo_actividade() < nr_ciclos) {
            for (Iterator<PCB> it = listadeespera.iterator(); it.hasNext();) {
                PCB processo = it.next();
                processo.setEstado(Pipeline_ouput.PIPELINE_STAGE_READY);
                Jobscheduler.pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_READY].listaprocessosadmitidos.add(processo);

                processo.setCiclo_actividade(nr_ciclos);

                it.remove();

            }
        }
    }
}

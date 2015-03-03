/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */

package jobscheduler;

import java.util.ArrayList;
/**
 * Classe responsavel por actualizar os varios stages e incrementar o numeros de
 * ciclos
 */
import java.util.LinkedList;
import static jobscheduler.Jobscheduler.pipeline;


public class pipeline implements Pipeline_ouput {

    public static int numProcesses = 0;
    public static int numciclos = 0;
    private static LinkedList<PCB> lista_espera_processos_por_criar;
    public static PipelineStage[] pipelineStages;
    public static ArrayList<Integer> Lista_ids = new ArrayList<>();
    public static int modoOutput = 0;//variavel que decide qual o output a ser impresso
    static int ultimo_pid_criado = 1;
    public static Pagina espacoLivre = new Pagina();//criacao do espaco livre

    public pipeline() {
        lista_espera_processos_por_criar = new LinkedList<PCB>();

        espacoLivre.setTamanho(VirtualMemory.tamanho_total);//definicao do tamanho do espaco livre
        espacoLivre.setEndereco_inicial(0);//criar o endereco no qual a memoria paginada ira comecar

        pipelineStages = new PipelineStage[5];
        pipelineStages[0] = new StageNew(Pipeline_ouput.PIPELINE_STAGE_NEW);
        pipelineStages[1] = new StageReady(Pipeline_ouput.PIPELINE_STAGE_READY);
        pipelineStages[2] = new StageRun(Pipeline_ouput.PIPELINE_STAGE_RUN);
        pipelineStages[3] = new StageBlocked(Pipeline_ouput.PROCESS_DISK);
        pipelineStages[4] = new StageExit(Pipeline_ouput.PROCESS_EXIT);
    }

    /**
     * Adiciona um processo para ser admitido
     *
     * @param pcb
     */
    public static void processo_a_admitir(PCB pcb) {
        lista_espera_processos_por_criar.add(pcb);
    }

    /**
     * permite executar a logca da pipeline
     */
    @Override
    public void executa() {

        for (int i = 0; i < lista_espera_processos_por_criar.size(); i++) {
            PCB pcb = lista_espera_processos_por_criar.get(i);

            if (pcb.getInstante_inicial() == numciclos) {
                pcb.setEstado(Pipeline_ouput.PIPELINE_STAGE_NEW);
                pcb.setCiclo_actividade(numciclos);
                pipelineStages[Pipeline_ouput.PIPELINE_STAGE_NEW].adiciona_processo_lista_espera(pcb);
            } else {
                pipelineStages[Pipeline_ouput.PIPELINE_STAGE_BLOCKED].actualiza_estado(numciclos);
                pipelineStages[Pipeline_ouput.PIPELINE_STAGE_NEW].actualiza_estado(numciclos);
                pipelineStages[Pipeline_ouput.PIPELINE_STAGE_RUN].actualiza_estado(numciclos);
                pipelineStages[Pipeline_ouput.PIPELINE_STAGE_READY].actualiza_estado(numciclos);
                pipelineStages[Pipeline_ouput.PIPELINE_STAGE_EXIT].actualiza_estado(numciclos);

            }

        }

        System.out.println(this);

        numciclos++;
    }

    /**
     * Permite estruturar a forma como e feito o output dos processos INSTANTE |
     * Estado p1 |....| ESTADO Pn
     *
     * 
     * @return
     */
    @Override
    public String toString() {
        String output1 = "";
        if (modoOutput == Pipeline_ouput.OUTPUT_PROCESSOS) {
            output1 += numciclos;
            int aux = -2;
            int i;
            for (i = 1; i <= Lista_ids.size(); i++) {
                for (int j = 0; j < pipeline.pipelineStages.length; j++) {
                    aux = pipeline.pipelineStages[j].output_estado_processos(i);
                    if (aux != -2) {
                        output1 += "| " + Lista_ids.get(i - 1) + " " + Pipeline_ouput.strStage[aux] + "| ";
                    }
                }

            }
            return output1;
        } else {
            for (Pagina pagina : VirtualMemory.Paginas) {
                output1 += pagina.toString() + " \n\n";
            }
            output1 += "O espaco livre comeca no endereco: " + espacoLivre.getEndereco_inicial() + "\n"
                    + "O seu tamanho é de: " + espacoLivre.getTamanho() + "\n"
                    + "Número de vezes necessário alocar mais espaco:" + VirtualMemory.libertacaoEspaco + "\n\n_________________________\n";
                    
            return output1;
        }
    }
}

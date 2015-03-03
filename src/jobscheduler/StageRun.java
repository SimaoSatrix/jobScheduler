/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Gere os processos em estado Run
 */
public class StageRun extends PipelineStage {
    /*
     O escalonador utilizado foi o Round Robin.
     Cada processo otbém uma pequena unidade de tempo na CPU, neste caso 4.
     Ao fim de cada time quantum o processo será comutado e adicionado à fila de 
     ready.
     Caso o processo termine a sua execução ou passe para o estado waiting durante 
     o time quantum atribuido o escalonado selecionará o processo sequinte para execução
     e reseta o time Quantum.
     */

    public static int Quantum = 4;
    /**
     * Ciclos_executado_run é um contador que permite saber o numero de ciclos
     * que o processo permaneceu no esatdo run
     */
    private int ciclos_executados_run = 0;

    public StageRun(int tipo_estado) {
        super(tipo_estado);
        listadeespera = new LinkedList<>();
        listaprocessosadmitidos = new LinkedList<>();
        /**
         * Para ser possivel ver todo o periodo de simulação foi estabelido que
         * caso um processo execute uma operação Go to Begin, apenas o fará um
         * certo numero de vezes podendo ser alterado este valor. Apos esse
         * numeros de vezes o processo transitará para o estado exit, terminando
         * a execução do processo. Assim não ficará inifitamente em execução.
         * O mesmo com o Fork, com um maximo estipulado que so será modificado,
         * depois de um processo ter sido executado uma vez completamente 
         */
        gotobegin_max = 2;
        fork_max = 1;

    }

    /**
     * Actualiza e gere os processos que estao na fila de espera
     *
     * @param nr_ciclos
     */
    @Override
    public void actualiza_estado(int nr_ciclos) {
        scheduler(nr_ciclos);
    }

    /**
     * Escalonado do CPU
     *
     * @param nrciclos
     */
    public void scheduler(int nrciclos) {
        if (listadeespera.size() > 0 && listadeespera.getFirst().getCiclo_actividade() < nrciclos) {
            PCB processo = listadeespera.getFirst();
            ciclos_executados_run = incrementa_quantum(ciclos_executados_run);
            if (ciclos_executados_run != Quantum) {
                if (processo.instrucao_em_processo() == Pipeline_ouput.PROCESS_CPU
                        || processo.instrucao_em_processo() == Pipeline_ouput.PROCESS_NOP) {
                    processo.setCiclo_actividade(nrciclos);
                    processo.proximoPCBData();
                } else if (processo.instrucao_em_processo() == Pipeline_ouput.PROCESS_DISK) {

                    processo.setCiclo_actividade(nrciclos);
                    processo.proximoPCBData();
                    processo.setEstado(Pipeline_ouput.PIPELINE_STAGE_BLOCKED);
                    pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_BLOCKED].adiciona_processo_lista_espera(listadeespera.remove());
                    reseta_ciclos_CPU();
                } else if (processo.instrucao_em_processo() == Pipeline_ouput.PROCESS_FORK) {

                    if (processo.getFork() < fork_max) {
                        processo.setCiclo_actividade(nrciclos);
                        PCB novo_processo = new PCB(processo.proximo_Pid(), nrciclos, Pipeline_ouput.PIPELINE_STAGE_READY, processo.getCiclo_actividade(), processo.getGotobegin_counter(), processo.getFork() + 1);

                        if (VirtualMemory.tamanho_processos_acabados.isEmpty()||VirtualMemory.isBestFit(processo.getMemoriaOcupada())==false) {
                            Pagina pag= new Pagina();
                            int[] dados= new int[processo.getMemoriaOcupada()];
                            int espaco_vazio=0;
                            for (int i=0; i<processo.getMemoriaOcupada();i=i+VirtualMemory.tamanho_pagina){
                                espaco_vazio=VirtualMemory.tamanho_pagina-procura_processo_pagina(processo.getEnderecoInicial()+i).length;
                                System.arraycopy(procura_processo_pagina(processo.getEnderecoInicial()+i),0,dados,i,VirtualMemory.tamanho_pagina-espaco_vazio);

                            }
                            dados=Arrays.copyOf(dados, processo.getMemoriaOcupada()-espaco_vazio);
                            pag.preencherPagina(novo_processo, dados);
                        } else {
                            VirtualMemory.bestFit(processo, novo_processo);
                        }

                        pipeline.Lista_ids.add(novo_processo.getPid());
                        pipeline.numProcesses = pipeline.numProcesses + 1;
                        pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_READY].listaprocessosadmitidos.add(novo_processo);
                        processo.proximoPCBData();
                    } else {
                        processo.setCiclo_actividade(nrciclos);
                        processo.proximoPCBData();
                    }

                } else if (processo.instrucao_em_processo() == Pipeline_ouput.PROCESS_GOTOBEGIN) {

                    if (processo.getGotobegin_counter() == gotobegin_max) {

                        processo.setCiclo_actividade(nrciclos);
                        processo.setEstado(Pipeline_ouput.PIPELINE_STAGE_EXIT);
                        pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_EXIT].adiciona_processo_lista_espera(listadeespera.remove());
                        reseta_ciclos_CPU();
                    } else {

                        processo.setCiclo_actividade(nrciclos);
                        processo.setFork(processo.getFork() + 1);
                        processo.setGotobegin_counter(processo.getGotobegin_counter() + 1);
                        processo.setPosicao_instrucao(0);
                    }

                } else {
                    
                    processo.setCiclo_actividade(nrciclos);
                    processo.setEstado(Pipeline_ouput.PIPELINE_STAGE_EXIT);
                    pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_EXIT].adiciona_processo_lista_espera(listadeespera.remove());

                    reseta_ciclos_CPU();
                }
            } else {
                processo.setCiclo_actividade(nrciclos);
                processo.setEstado(Pipeline_ouput.PIPELINE_STAGE_READY);
                pipeline.pipelineStages[Pipeline_ouput.PIPELINE_STAGE_READY].listaprocessosadmitidos.add(listadeespera.remove());

                reseta_ciclos_CPU();
            }

        }
    }

    /**
     * Incrementa a variavel ciclos_executados_run em uma unidade
     *
     * @param ciclos_executados_run
     * @return
     */
    private int incrementa_quantum(int ciclos_executados_run) {
        return this.ciclos_executados_run = ciclos_executados_run + 1;
    }

    /**
     * Faz um reset aos ciclos do CPU
     */
    public void reseta_ciclos_CPU() {
        ciclos_executados_run = 0;
    }
    
    /**
     * procura as paginas consoante os enderecos e retorna os dados
     * presente nelas
     * @param endereco
     * @return 
     */

    public static int[] procura_processo_pagina(int endereco) {

        for (Pagina pagina : VirtualMemory.Paginas) {
            if (pagina.getEndereco_inicial() == endereco) {
                return pagina.getDados();

            }
        }
        return null;
    }

}

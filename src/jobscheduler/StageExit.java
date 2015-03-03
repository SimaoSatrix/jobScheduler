/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */


package jobscheduler;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 Gere os processos no estado exit
 */
public class StageExit extends PipelineStage {

    public StageExit(int tipo_estado) {
        super(tipo_estado);
        listadeespera = new LinkedList<>();

    }

    /**
     * Actualiza e gere os processos que estao em fila de espera
     * Liberta as paginas dos processo que acabaram agora o seu processamento
     * @param nr_ciclos
     */
    @Override
    public void actualiza_estado(int nr_ciclos) {
        int num_processos_completos = 0;

        for (PCB pcb : listadeespera) {

            num_processos_completos++;

            for (Pagina pagina : VirtualMemory.Paginas) {
                if (pcb.getPid() == pagina.getPagina_pid()) {
                    pagina.setPagina_pid(-1);
                    pagina.setAlocado(false);
                }
            }
            int soma_paginas=0;
            
            
            for(int i=1; i <VirtualMemory.Paginas.size(); i++){
                if(VirtualMemory.Paginas.get(i-1).getPagina_pid()==-1&&i-1==0){
                    soma_paginas=soma_paginas+VirtualMemory.tamanho_pagina;
                    VirtualMemory.enderecos_processos_acabados.add(VirtualMemory.Paginas.get(i-1).getEndereco_inicial());
                }
                else{
                    
                }
                
                if(VirtualMemory.Paginas.get(i).getPagina_pid()==-1&&VirtualMemory.Paginas.get(i-1).getPagina_pid()!=-1){
                    soma_paginas = soma_paginas + VirtualMemory.Paginas.get(i).getTamanho();
                    VirtualMemory.enderecos_processos_acabados.add(VirtualMemory.Paginas.get(i-1).getEndereco_inicial());                    
                }else if(VirtualMemory.Paginas.get(i).getPagina_pid()!=-1&&VirtualMemory.Paginas.get(i-1).getPagina_pid()==-1){
                    VirtualMemory.tamanho_processos_acabados.add(soma_paginas);
                    soma_paginas=0;
                }else if(VirtualMemory.Paginas.get(i-1).getPagina_pid()==-1&&VirtualMemory.Paginas.get(i).getPagina_pid()==-1){
                    soma_paginas = soma_paginas + VirtualMemory.Paginas.get(i).getTamanho();
                }else{
                    
                }
                    
            }

           

        }

        if (num_processos_completos == pipeline.numProcesses) {
            jobscheduler.Jobscheduler.running = false;
        }

    }

}

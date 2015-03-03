/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

/**
 * PCB (Process Control Block) - conjunto de atributos do processo Contem a
 * identificação do processo -> int pid Contem a informação de estado do
 * processador ->  int posicao_instrucao Contem a informação de controle do processo, 
 * ou seja, int estado (ex: Ready), suporte ao escalonamento, int evento->i
 * dentificação do evento que o processo está à espera.-> gotobegin_counter e fork, 
 * contadores que gravam o numero de vezes que tal instrucao é executada,
 * permitindo a posterior gestao da recorrência destas instrucoes no processo
 * ->enderecoInicial e memoriaOcupada variaveis necessárias à gestao da memoria,
 * um contendo o enderecoInicial da primeira pagina, e o outro a memoria ocupada
 * pelos dados do processo
 */
public class PCB {

    private int pid; 
    private int instante_inicial; 
    private int posicao_instrucao; 
    private int estado = Pipeline_ouput.PROCESS_NOT_CREATED;
    private int memoriaOcupada;
    private int enderecoInicial; 
    private int ciclo_actividade;
    private int gotobegin_counter = 0;
    private int fork = 0; 

    public PCB() {

    }

    //construtor PCB
    public PCB(int pid, int instante_inicial, int estado, int ciclo_actividade, int gotobegin_counter, int fork) {
        this.pid = pid;
        this.instante_inicial = instante_inicial;
        this.estado = estado;
        this.posicao_instrucao = 0;
        this.ciclo_actividade = ciclo_actividade;
        this.gotobegin_counter = gotobegin_counter;
        this.fork = fork;

    }

    // metodos getters
    public int getPid() {
        return pid;
    }

    public int getInstante_inicial() {
        return instante_inicial;
    }

    public int getPosica_instrucao() {
        return posicao_instrucao;
    }

    public int getEstado() {
        return estado;
    }

    public int getCiclo_actividade() {
        return ciclo_actividade;
    }


    public int getGotobegin_counter() {
        return gotobegin_counter;
    }


    public int getFork() {
        return fork;
    }

    public int getMemoriaOcupada() {
        return memoriaOcupada;
    }

    public int getEnderecoInicial() {
        return enderecoInicial;
    }



    //metodos setters
    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setInstante_inicial(int instante_inicial) {
        this.instante_inicial = instante_inicial;
    }

    public void setPosicao_instrucao(int posicao_instrucao) {
        this.posicao_instrucao = posicao_instrucao;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setCiclo_actividade(int ciclo_actividade) {
        this.ciclo_actividade = ciclo_actividade;
    }


    public void setGotobegin_counter(int gotobegin_counter) {
        this.gotobegin_counter = gotobegin_counter;
    }

    public void setFork(int fork) {
        this.fork = fork;
    }

    public void setMemoriaOcupada(int memoriaOcupada) {
        this.memoriaOcupada = memoriaOcupada;
    }

    public void setEnderecoInicial(int enderecoInicial) {
        this.enderecoInicial = enderecoInicial;
    }



    //avança para a proxima instrução do processo
    public void proximoPCBData() {
        posicao_instrucao++;
    }

    @Override
    public String toString() {
        return new String();
    }
    
    
    /**
     * Permite calcular o próximo PID
     * 
     * @return 
     */
    
    public int proximo_Pid() {
        int nextPid=pipeline.ultimo_pid_criado++;
        return nextPid;
    }

    /**
     * Permite saber qual a instrução do processo que esta em execução
     * 
     * @return 
     */
    
    public int instrucao_em_processo() {
        int instrucao=-1;
        int endereco = instrucaoNoEndereco();
        for(Pagina pagina : VirtualMemory.Paginas){
            if(pid==pagina.getPagina_pid()&&endereco>=pagina.getEndereco_inicial()&&endereco<pagina.getEndereco_inicial()+VirtualMemory.tamanho_pagina){
                instrucao=pagina.getDados()[endereco%VirtualMemory.tamanho_pagina];
            }
        }
        return instrucao;

    }
    /**
     * metodo responsavel por descobrir o endereco da instrucao em processo
     * 
     * @return 
     */
    public int instrucaoNoEndereco(){
        int endereco=enderecoInicial+posicao_instrucao;
         
        return endereco;
    }

}

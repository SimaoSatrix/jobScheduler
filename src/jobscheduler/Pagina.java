/*
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

/**
 * As paginas sao blocos de memoria contendo os dados relativos aos processos
 * -> pagina_pid identificativo do processo que ocupa a pagina, será -1 se o processo que
 * criou a pagina tenha ja terminado
 * -> dados[] sao os dados que fazem parte do processo
 * -> endereco_inicial da pagina, tamanho e espacoVazio que é maior que 0,
 * caso a pagina nao seja totalmente ocupada
 * ->o boleano alocado identifica se esta pagina tem um processo activo(true) 
 */
public class Pagina {

    private int pagina_pid;
    private int endereco_inicial;
    private int tamanho;
    private int dados[] = new int[VirtualMemory.tamanho_pagina];
    private boolean alocado;
    private int espacoVazio;
    
    //contrutores
    public Pagina() {
        this.pagina_pid = 0;
        this.endereco_inicial = 0;
        this.tamanho = 0;
        this.alocado = false;
        this.espacoVazio=VirtualMemory.tamanho_pagina;

    }

    public Pagina(int pagina_pid) {
        this.pagina_pid = pagina_pid;
        this.endereco_inicial = 0;
        this.tamanho = 0;
        this.alocado = false;
    }
    
    //getters
    public int getPagina_pid() {
        return pagina_pid;
    }

    public int getEndereco_inicial() {
        return endereco_inicial;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean isAlocado() {
        return alocado;
    }

    public int[] getDados() {
        return dados;
    }

    public int getEspacoVazio() {
        return espacoVazio;
    }
    
    
    //setters
    public void setPagina_pid(int pagina_pid) {
        this.pagina_pid = pagina_pid;
    }

    public void setEndereco_inicial(int endereco_inicial) {
        this.endereco_inicial = endereco_inicial;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public void setAlocado(boolean alocado) {
        this.alocado = alocado;
    }

    public void setDados(int[] dados) {
        this.dados = dados;
    }

    public void setEspacoVazio(int espacoVazio) {
        this.espacoVazio = espacoVazio;
    }

    
    /** metodo responsavel por criar espaco para as paginas, apartir do espaco livre
     * para o caso de nao haver espaco livre suficiente aloca nova
     * @return 
     */
    public Pagina libertacaoEspaco() {
        Pagina espacoLivre = pipeline.espacoLivre;
        if (espacoLivre.getTamanho() >= VirtualMemory.tamanho_pagina) {
            Pagina novaPagina = new Pagina();
            novaPagina.setEndereco_inicial(espacoLivre.getEndereco_inicial());
            novaPagina.setTamanho(VirtualMemory.tamanho_pagina);
            espacoLivre.setEndereco_inicial(espacoLivre.getEndereco_inicial() + VirtualMemory.tamanho_pagina);
            espacoLivre.setTamanho(espacoLivre.getTamanho() - novaPagina.getTamanho());
            return novaPagina;

        } else {
            espacoLivre.setTamanho(VirtualMemory.tamanho_total);
            VirtualMemory.setLibertacaoEspaco(VirtualMemory.getLibertacaoEspaco() + 1);
            Pagina novaPagina = new Pagina();
            novaPagina.setEndereco_inicial(espacoLivre.getEndereco_inicial());
            novaPagina.setTamanho(VirtualMemory.tamanho_pagina);
            espacoLivre.setEndereco_inicial(espacoLivre.getEndereco_inicial() + VirtualMemory.tamanho_pagina);
            espacoLivre.setTamanho(espacoLivre.getTamanho() - novaPagina.getTamanho());
            return novaPagina;

        }

    }
    /**metodo responsavel por preencher a(s) pagina(s) com as instrucoes do processo
     * calcula as paginas suficientes para conter todos os dados relativos ao processo
     * e depois preenche pagina a pagina
     * @param processo
     * @param dados 
     */


    public void preencherPagina(PCB processo, int[] dados) {
        int tamanho_instrucoes = dados.length;
        int nrPaginas;
        int[] array_aux;
        if (tamanho_instrucoes % VirtualMemory.tamanho_pagina == 0) {
           
            nrPaginas = tamanho_instrucoes / VirtualMemory.tamanho_pagina;
        } else {
            nrPaginas = tamanho_instrucoes / VirtualMemory.tamanho_pagina + 1;

        }
        processo.setEnderecoInicial(pipeline.espacoLivre.getEndereco_inicial());
        for (int i = 0; i < nrPaginas; i++) {
            Pagina nova_pagina = pipeline.espacoLivre.libertacaoEspaco();
            nova_pagina.setPagina_pid(processo.getPid());
            if (i+1==nrPaginas) {
                array_aux=VirtualMemory.retornarArray(dados, processo.getMemoriaOcupada(), tamanho_instrucoes, true);
            } else {
                array_aux=VirtualMemory.retornarArray(dados,processo.getMemoriaOcupada(), VirtualMemory.tamanho_pagina*(i+1), false);
            }
            processo.setMemoriaOcupada(processo.getMemoriaOcupada() + VirtualMemory.tamanho_pagina);
            nova_pagina.setDados(array_aux);
            nova_pagina.setAlocado(true);
            nova_pagina.setEspacoVazio(nova_pagina.getTamanho()-nova_pagina.getDados().length);
            VirtualMemory.adicionarPagina(nova_pagina);
        }
    }

    @Override
    public String toString() {
        String output;
        output = "O Pid na página é: " + getPagina_pid() + "\n"
                + "O endereco da pagina é:" + getEndereco_inicial() + "\n"
                + "O espaco vazio é: "+ getEspacoVazio()+"\n"
                + "As instrucoes presentes na pagina são: ";
        for (int i = 0; i < getDados().length; i++) {
            output += getDados()[i] + " ";
        }
        return output;

    }
}

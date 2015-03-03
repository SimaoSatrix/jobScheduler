/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */
package jobscheduler;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Classe que gere a memoria de paginas
 * ->Paginas,LinkedList que contem todas as paginas criadas para conter os processos ordenados por
 * endereco inicial de forma crescente
 * ->enderecos_processos_acabados e tamanho_processo acabados ArrayLists que contêm os tamanhos
 * de memoria(várias paginas seguidas se assim for o caso) e respectivos enderecos inicias da 
 * primeira pagina
 * ->tamanho_total inicial alocado para conter as paginas
 * -> tmanho_pagina, tamanho das paginas
 * -> libertacaoEspaco, contador que contem o número de vezes que espaco adicional foi necessário alocar
 *
 * 
 */
public class VirtualMemory {

    public static int tamanho_total = 400;
    public static int tamanho_pagina = 10;
    public static int libertacaoEspaco;
    private static final VirtualMemory instancia = new VirtualMemory();
    public static LinkedList<Pagina> Paginas;
    public static ArrayList<Integer> enderecos_processos_acabados = new ArrayList<>();
    public static ArrayList<Integer> tamanho_processos_acabados = new ArrayList<>();

    private VirtualMemory() {
        Paginas = new LinkedList<>();
        libertacaoEspaco = 0;

    }

    public static int getLibertacaoEspaco() {
        return libertacaoEspaco;
    }

    public static void setLibertacaoEspaco(int libertacaoEspaco) {
        VirtualMemory.libertacaoEspaco = libertacaoEspaco;
    }

    public static void adicionarPagina(Pagina pagina) {//adicao de nova pagina ao array
        Paginas.add(pagina);
    }

    /**
     * metodo responsavel por retornar o array com instrucoes com tamanho especifico de cada pagina    
     * 
     * @param dados
     * @param indice
     * @param indiceFinal
     * @param ultimaPagina
     * @return 
     */
    public static int[] retornarArray(int[] dados, int indice, int indiceFinal, boolean ultimaPagina) {
        int j = 0;
        if (ultimaPagina == false) {
            int[] array_aux = new int[tamanho_pagina]; 
            for (int i = indice; i < indiceFinal; i++) {
                array_aux[j] = dados[i];
                j++;

            }
            return array_aux;
        } else {
            int[] array_aux = new int[indiceFinal - indice];
            for (int i = indice; i < indiceFinal; i++) {
                array_aux[j] = dados[i];
                j++;
            }
            return array_aux;
        }
    }
    
    /**
     * metodo responsavel por esvaziar a pagina, mantendo a estrutura criada, quando um processo acaba,
     * disponibilizando assim mais espaco para alem da pagina espacoVazio
     * 
     * @param processo 
     */

    public static void libertarPagina(PCB processo) {

        for (Pagina pagina : Paginas) {
            if (processo.getPid() == pagina.getPagina_pid()) {
                pagina.setAlocado(false);
                pagina.setPagina_pid(-1);
                pagina.setEspacoVazio(VirtualMemory.tamanho_pagina);

            }
        }
        processo.setMemoriaOcupada(0);
    }

    /**
     * metodo responsavel por encontrar o conjunto de paginas sem processo activo seguidas na memoria
     * com o tamanho mais proximo, mas maior que o processo
     * posteriormente preenchendo essas paginas com os processos
     * @param processo
     * @param novoprocesso 
     */
    public static void bestFit(PCB processo, PCB novoprocesso) {

        int endereco_pai = processo.getEnderecoInicial();
        int endereco = -1;
        int melhor_espaco = processo.getMemoriaOcupada();
        for (int i = 0; i < tamanho_processos_acabados.size(); i++) {
            if (melhor_espaco <= tamanho_processos_acabados.get(i)) {
                melhor_espaco = tamanho_processos_acabados.get(i);
                endereco = enderecos_processos_acabados.get(i);
            }
        }
        novoprocesso.setEnderecoInicial(endereco);
        for (Pagina pagina : Paginas) {
            if (pagina.getEndereco_inicial() == endereco) {
                pagina.setDados(StageRun.procura_processo_pagina(endereco_pai));
                pagina.setPagina_pid(novoprocesso.getPid());
                pagina.setAlocado(true);
                pagina.setEspacoVazio(pagina.getTamanho() - pagina.getDados().length);
                novoprocesso.setMemoriaOcupada(novoprocesso.getMemoriaOcupada() + tamanho_pagina);
                endereco_pai = endereco_pai + tamanho_pagina;
            } else if (pagina.getEndereco_inicial() > endereco && pagina.getEndereco_inicial() < endereco + processo.getMemoriaOcupada()) {
                pagina.setDados(StageRun.procura_processo_pagina(endereco_pai));
                pagina.setPagina_pid(novoprocesso.getPid());
                pagina.setAlocado(true);
                pagina.setEspacoVazio(pagina.getTamanho() - pagina.getDados().length);
                novoprocesso.setMemoriaOcupada(novoprocesso.getMemoriaOcupada() + tamanho_pagina);
                endereco_pai = endereco_pai + tamanho_pagina;
            } else {

            }
        }

    }

    /**
     * metodo que verifica se é possivel ou nao preencher paginas usando o bestFit
     * @param tamanho
     * @return 
     */
    public static boolean isBestFit(int tamanho) {

        for (int i = 0; i < tamanho_processos_acabados.size(); i++) {
            if (tamanho < tamanho_processos_acabados.get(i)) {
                return true;
            }
        }
        return false;
    }

}

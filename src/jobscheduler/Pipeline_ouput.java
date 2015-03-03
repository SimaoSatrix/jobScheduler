/**
 * @author Simão Ramos (29035) Marlene Barroso (30178) Sistemos Operativos I- JobScheduler(1ª parte)
 * 2014
 */

package jobscheduler;

/**
 *Interface contendo todas as constantes usadas pelo jobscheduler 
 *
 */

public interface Pipeline_ouput {

    public static final int PROCESS_NOT_CREATED = -1;
    public static final int PROCESS_FORK=5;
    public static final int PROCESS_GOTOBEGIN = 4;
    public static final int PROCESS_NOP = 3;
    public static final int PROCESS_CPU = 1;
    public static final int PROCESS_DISK = 2;
    public static final int PROCESS_EXIT = 0;

    public static final int PIPELINE_STAGE_NEW = 0;
    public static final int PIPELINE_STAGE_READY = 1;
    public static final int PIPELINE_STAGE_RUN = 2;
    public static final int PIPELINE_STAGE_BLOCKED = 3;
    public static final int PIPELINE_STAGE_EXIT = 4;
    
    public static final int OUTPUT_PROCESSOS = 0;
    public static final int OUTPUT_MEMORIA = 1;

    public static String strStage[] = {"NEW", "WAIT/READY", "RUN", "BLOCKED", "EXIT"};
    

    public void executa();
    
}

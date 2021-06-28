package br.nom.belo.marcio.simuladorvoo;

class Aviao implements Runnable {

    private Aeroporto aeroporto;
    private String idAviao;
    private long tempoVoo=0;

    public Aviao(Aeroporto aeroporto, String idAviao,long tempoVoo) {
        this.aeroporto = aeroporto;
        this.idAviao = idAviao;
        this.tempoVoo=tempoVoo;
    }

    public void run() {
        try {
            Thread.sleep(tempoVoo/2);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        decolar();
        voar();
        aterrisar();
        
    }

    private void decolar() {
        System.out.println(idAviao + ": esperando pista...");
        
        String acao=idAviao + ": decolando...";
        aeroporto.esperarPistaDisponivel(acao); // Espera uma pista livre
    }

    private void voar() {
        System.out.println(idAviao + ": voando...");
        try {
            Thread.sleep(tempoVoo);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    private void aterrisar() {
        System.out.println(idAviao + ": esperando pista...");
        String acao=idAviao + ": aterissando...";
        aeroporto.esperarPistaDisponivel(acao); // Espera uma pista livre
    }
}

class Aeroporto implements Runnable {

    private boolean temPistaDisponivel = true;
    private String nomeAeroporto;

    public Aeroporto(String nomeAeroporto) {
        this.nomeAeroporto = nomeAeroporto;
    }
    /* Alterado por Ricardo e Rodrigo */
    public synchronized void esperarPistaDisponivel(String acao) {
        if(temPistaDisponivel)
        {
            System.out.println(acao);
            mudarEstadoPistaDisponivel();
        }
        else{
            
            mudarEstadoPistaDisponivel();
            System.out.println(acao); 
            
        }
    }

    public synchronized void mudarEstadoPistaDisponivel() {
        // Inverte o estado da pista.
        temPistaDisponivel = !temPistaDisponivel;
        System.out.println(nomeAeroporto + " tem pista disponível: " + 
                (temPistaDisponivel == true ? "Sim" : "Não"));
        // Notifica a mudanca de estado para quem estiver esperando.
        if(temPistaDisponivel) this.notify();
    }

    public void run() {
        System.out.println("Rodando aeroporto " + nomeAeroporto);
        while (true) {
            try {
                mudarEstadoPistaDisponivel();
                // Coloca a thread aeroporto dormindo por um tempo de 0 a 5s
                Thread.sleep((int)(Math.random()*5000)); 
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

}
/*
 * Simulador de voo com threads
 */
public final class SimuladorVoo {

    public static void main(String[] args) {

        System.out.println("Rodando simulador de voo.");

        // Constroi aeroporto e inicia sua execucao.
        // NÃO MEXER NESSE TRECHO
        Aeroporto santosDumont = new Aeroporto("Santos Dumont");
        Thread threadAeroporto = new Thread(santosDumont);
        
        // Constrói aviao e inicia sua execucao.
        // NÃO MEXER NESSE TRECHO
        /* Alterado por Ricardo e Rodrigo */
        Aviao aviao1 = new Aviao(santosDumont, "Avião 1",5000);
        Thread thread1 = new Thread(aviao1);
        
        Aviao aviao2 =  new Aviao(santosDumont, "Avião 2",8000);
        Thread thread2= new Thread(aviao2);

        
        Aviao aviao3 = new Aviao(santosDumont, "Avião 3",12000);
        Thread thread3 = new Thread(aviao3);

        
        Aviao aviao4 = new Aviao(santosDumont, "Avião 4",15000);
        Thread thread4 = new Thread(aviao4);
        
        Aviao aviao5 = new Aviao(santosDumont, "Avião 5",18000);
        Thread thread5 = new Thread(aviao5);


        // Inicia as threads
        threadAeroporto.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        synchronized(threadAeroporto){
        try {
            // Junta-se ao término da execução da thread do aeroporto
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
            threadAeroporto.stop();
            //threadAeroporto.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Terminando thread principal.");
        }

    }
}
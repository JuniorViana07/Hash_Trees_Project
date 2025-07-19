package util;

public class Estatisticas {
    public long comparacoes = 0;
    public long atribuicoes = 0;
    public long tempoNano = 0;
    public long migracoesParaAVL = 0;
    public long migracoesParaRB= 0;

    public void reset() {
        comparacoes = 0;
        atribuicoes = 0;
        tempoNano = 0;
    }

    //acabou sendo feito direto na main, mas pode ser organizado pra chamar apenas a função e ficar mais organizado
    public void printResumo(String label) {
        System.out.println("=== " + label + " ===");
        System.out.println("Comparações: " + comparacoes);
        System.out.println("Atribuições: " + atribuicoes);
        System.out.println("Tempo (ms): " + (tempoNano / 1_000_000.0));
    }
}

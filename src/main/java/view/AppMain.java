package view;

import model.Registro;
import service.HashTabela;
import util.Estatisticas;

import java.io.*;
import java.util.List;

public class AppMain {
    public static void main(String[] args) {
        HashTabela tabela = new HashTabela();
        String arquivo = "src/main/java/util/transacoes.csv";

        long tempoTotalIni = System.nanoTime();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine(); // cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length < 5) continue;

                Registro r = new Registro(
                        partes[0],
                        partes[1],
                        partes[2],
                        Float.parseFloat(partes[3].replace(",", ".")),
                        partes[4]
                );

                tabela.inserirPorId(r);
                tabela.inserirPorOrigem(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Registro> porId = tabela.buscarPorId("ID00000");
        List<Registro> porOrigem = tabela.buscarPorOrigem("ORIG005");
        List<Registro> porIntervalo = tabela.buscarPorOrigemEIntervalo("ORIG005", "2021-01-01", "2023-12-31");

        long tempoTotalFim = System.nanoTime();

        Estatisticas estat = tabela.estat;

        System.out.println("\n==== RELATÓRIO FINAL ====");
        System.out.println("Comparações: " + estat.comparacoes);
        System.out.println("Atribuições: " + estat.atribuicoes);
        System.out.println("Migrações para AVL: " + estat.migracoesParaAVL);
        System.out.println("Migrações para RB: " + estat.migracoesParaRB);
        System.out.println("Tempo total (ms): " + ((tempoTotalFim - tempoTotalIni) / 1_000_000.0));
        System.out.println("Tempo acumulado operações (ms): " + (estat.tempoNano / 1_000_000.0));

        gerarRelatorioCSV(estat, tempoTotalIni, tempoTotalFim);
    }

    private static void gerarRelatorioCSV(Estatisticas estat, long ini, long fim) {
        String nomeArquivo = "relatorio_final.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(nomeArquivo))) {
            pw.println("Métrica,Valor");
            pw.println("Comparações," + estat.comparacoes);
            pw.println("Atribuições," + estat.atribuicoes);
            pw.println("Migrações para AVL: " + estat.migracoesParaAVL);
            pw.println("Migrações para RB: " + estat.migracoesParaRB);
            pw.println("Tempo total (ms)," + ((fim - ini) / 1_000_000.0));
            pw.println("Tempo acumulado operações (ms)," + (estat.tempoNano / 1_000_000.0));

            System.out.println("Relatório salvo em: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }
}

package app;

import model.Registro;
import service.HashTabela;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class MainTeste {
    public static void main(String[] args) {
        HashTabela tabela = new HashTabela();

        // Lê o CSV e insere os registros
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/util/transacoes.csv"))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                String id = partes[0];
                String origem = partes[1];
                String destino = partes[2];
                float valor = Float.parseFloat(partes[3].replace(",", "."));
                String timestamp = partes[4];

                Registro r = new Registro(id, origem, destino, valor, timestamp);
                tabela.inserirPorId(r);
                tabela.inserirPorOrigem(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Testa busca por origem
        String origemTeste = "ORIG005";
        List<Registro> encontrados = tabela.buscarPorOrigem(origemTeste);
        System.out.println("Encontrados por origem " + origemTeste + ": " + encontrados.size());
        for (Registro r : encontrados) {
            System.out.println(r);
        }

        // Testa busca por origem + intervalo
        String tsInicio = "2021-01-01";
        String tsFim = "2021-12-31";
        List<Registro> intervalo = tabela.buscarPorOrigemEIntervalo(origemTeste, tsInicio, tsFim);
        System.out.println("Encontrados por origem " + origemTeste + " no intervalo " + tsInicio + " a " + tsFim + ": " + intervalo.size());
        for (Registro r : intervalo) {
            System.out.println(r);
        }
    }
    // essa main serviu pra fazer alguns testes de busca, fiz as buscas de ORI005, e ORI005 + 2021-01-01 a 2021-12-31
    // pode ser melhorada pra ter um menu de pesquisas depois
}

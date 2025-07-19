package service;

import model.Registro;
import util.Estatisticas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTabela {

    private static final int TAMANHO_HASH_ID = 512;
    private List<LinkedList<Registro>> hashId;
    private HashTentativaQuadratica<String, Object> hashOrigem;
    public Estatisticas estat = new Estatisticas();


    public HashTabela() {
        hashId = new ArrayList<>(TAMANHO_HASH_ID);
        for (int i = 0; i < TAMANHO_HASH_ID; i++) {
            hashId.add(new LinkedList<>());
            estat.atribuicoes++;
        }
        hashOrigem = new HashTentativaQuadratica<>();
    }

    public void inserirPorId(Registro registro) {
        long ini = System.nanoTime();
        int indice = hash(registro.getId(), TAMANHO_HASH_ID);
        hashId.get(indice).add(registro);
        estat.tempoNano += System.nanoTime() - ini;
    }

    public List<Registro> buscarPorId(String id) {
        long ini = System.nanoTime();
        int indice = hash(id, TAMANHO_HASH_ID);
        List<Registro> lista = hashId.get(indice);
        List<Registro> resultado = new ArrayList<>();

        for (Registro r : lista) {
            estat.comparacoes++;
            if (r.getId().equals(id)) {
                resultado.add(r);
                estat.atribuicoes++;
            }
        }
        estat.tempoNano += System.nanoTime() - ini;
        return resultado;
    }

    /* Inserção por Origem será jogada na HashQuadratica caso valor==null
    na lista caso o numero de registros > 0,
    na AVL caso > 3
    na RB caso h da AVL > 10 */

    public void inserirPorOrigem(Registro registro) {
        long ini = System.nanoTime();
        Object valor = hashOrigem.get(registro.getOrigem());

        estat.comparacoes++;
        if (valor == null) {
            List<Registro> lista = new ArrayList<>();
            lista.add(registro);
            estat.atribuicoes++;
            hashOrigem.put(registro.getOrigem(), lista);
        }
        else if (valor instanceof List) {
            List<Registro> lista = (List<Registro>) valor;
            lista.add(registro);
            estat.atribuicoes++;
            estat.comparacoes++;

            if (lista.size() > 3) {
                System.out.println("Migrando para AVL: " + registro.getOrigem());
                estat.migracoesParaAVL++;
                TreeAVL<Registro> arvore = new TreeAVL<>(estat);
                for (Registro r : lista) {
                    arvore.insert(r);
                    estat.atribuicoes++;
                }
                hashOrigem.put(registro.getOrigem(), arvore);
            } else {
                hashOrigem.put(registro.getOrigem(), lista);
            }
        }
        else if (valor instanceof TreeAVL) {
            TreeAVL<Registro> arvore = (TreeAVL<Registro>) valor;
            arvore.insert(registro);
            estat.atribuicoes++;
            estat.comparacoes++;

            if (arvore.getHeight() > 10) {
                System.out.println("Migrando para RB: " + registro.getOrigem());
                estat.migracoesParaRB++;
                TreeRB<Registro> rb = new TreeRB<>(estat);
                List<Registro> registros = new ArrayList<>();
                arvore.collectInOrder(registros);
                for (Registro r : registros) {
                    rb.insert(r);
                    estat.atribuicoes++;
                }
                hashOrigem.put(registro.getOrigem(), rb);

            }
        }
        else if (valor instanceof TreeRB) {
            TreeRB<Registro> rb = (TreeRB<Registro>) valor;
            rb.insert(registro);
            estat.atribuicoes++;
            estat.comparacoes++;
        }
        estat.tempoNano += System.nanoTime() - ini;
    }

    public List<Registro> buscarPorOrigem(String origem) {
        long ini = System.nanoTime();
        Object valor = hashOrigem.get(origem);
        List<Registro> resultado = new ArrayList<>();
        estat.comparacoes++;
        if (valor == null) {
            return resultado;
        }
        else if (valor instanceof List) {
            resultado.addAll((List<Registro>) valor);
            estat.atribuicoes++;
        }
        else if (valor instanceof TreeAVL) {
            ((TreeAVL<Registro>) valor).collectInOrder(resultado);
        }
        else if (valor instanceof TreeRB) {
            ((TreeRB<Registro>) valor).collectInOrder(resultado);
        }
        estat.tempoNano += System.nanoTime() - ini;
        return resultado;
    }

    public List<Registro> buscarPorOrigemEIntervalo(String origem, String tsInicio, String tsFim) {
        long ini = System.nanoTime();
        Object valor = hashOrigem.get(origem);
        List<Registro> resultado = new ArrayList<>();
        estat.comparacoes++;
        if (valor == null) {
            return resultado;
        }

        List<Registro> registros = new ArrayList<>();
        estat.comparacoes++;
        if (valor instanceof List) {
            registros.addAll((List<Registro>) valor);

        }
        else if (valor instanceof TreeAVL) {
            ((TreeAVL<Registro>) valor).collectInOrder(registros);
        }
        else if (valor instanceof TreeRB) {
            ((TreeRB<Registro>) valor).collectInOrder(registros);
        }

        // Filtrar por intervalo de timestamp
        for (Registro r : registros) {
            String ts = r.getTimestamp();
            estat.comparacoes++;
            if (ts.compareTo(tsInicio) >= 0 && ts.compareTo(tsFim) <= 0) {
                resultado.add(r);
                estat.atribuicoes++;
            }
        }
        estat.tempoNano += System.nanoTime() - ini;
        return resultado;
    }


    private int hash(String chave, int tamanho) {
        return Math.abs(chave.hashCode()) % tamanho;
    }
}

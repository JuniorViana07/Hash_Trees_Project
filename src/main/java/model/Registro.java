package model;

public class Registro implements Comparable<Registro> {
    String id;
    String origem;
    String destino;
    float valor;
    String timestamp;

    public Registro(String id, String origem, String destino, float valor, String timestamp) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
        this.timestamp = timestamp;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setNome(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Registro outro) {
        return this.id.compareTo(outro.id);
    }

    @Override
    public String toString() {
        return id + "," + origem + "," + destino + "," + valor + "," + timestamp;
    }
}

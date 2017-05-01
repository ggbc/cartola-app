package br.com.myapp.ultracartola.business;

/**
 * Created by Gustavo on 29/04/2017.
 */

public class Team {
    private int timeId;
    private String nome;
    private String nomeCartola;
    private String urlEscudoPng;
    private double pontos;

    public Team() {

    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeCartola() {
        return nomeCartola;
    }

    public void setNomeCartola(String nomeCartola) {
        this.nomeCartola = nomeCartola;
    }

    public String getUrlEscudoPng() {
        return urlEscudoPng;
    }

    public void setUrlEscudoPng(String urlEscudoPng) {
        this.urlEscudoPng = urlEscudoPng;
    }

    public double getPontos() {
        return pontos;
    }

    public void setPontos(double pontos) {
        this.pontos = pontos;
    }
}

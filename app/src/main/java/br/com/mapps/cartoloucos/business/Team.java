package br.com.mapps.cartoloucos.business;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Gustavo on 29/04/2017.
 */

public class Team implements Comparable<Team>{
    private int timeId;
    private String nome;
    private String nomeCartola;
    private String urlEscudoPng;
    private Double pontos;
    private ArrayList<Athlet> atletas;

    // Used only on the SearchTeamActivity
    private boolean checked;

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

    public Double getPontos() {
        return pontos;
    }

    public void setPontos(Double pontos) {
        this.pontos = pontos;
    }

    public ArrayList<Athlet> getAtletas() {
        return atletas;
    }

    public void setAtletas(ArrayList<Athlet> atletas) {
        this.atletas = atletas;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int compareTo(@NonNull Team team) throws IllegalArgumentException{
        if (this.getPontos() > team.getPontos()) {
            return 1;
        }
        else {
            if (this.getPontos() < team.getPontos()) {
                return -1;
            } else {
                return this.getNome().compareTo(team.getNome());
            }
        }
    }
}

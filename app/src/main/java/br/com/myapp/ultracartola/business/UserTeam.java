package br.com.myapp.ultracartola.business;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 08/03/2017.
 */

public class UserTeam {

    private int mTimeId;
    private List<Athlet> atletas;
    private JSONObject mTime;
    private double mPatrimonio;
    private int mEsquemaId;
    private double mPontos;
    private double mValorTime;
    private int mRodadaAtual;

    public UserTeam(int id) {
        this.mTimeId = id;
        this.atletas = new ArrayList<Athlet>();
    }

    public int getTimeId() {
        return mTimeId;
    }

    public List<Athlet> getAtletas() {
        return this.atletas;
    }

    public void addAtleta(Athlet atleta) {
        this.atletas.add(atleta);
    }

    public JSONObject getmTime() {
        return mTime;
    }

    public void setmTime(JSONObject mTime) {
        this.mTime = mTime;
    }

    public double getmPatrimonio() {
        return mPatrimonio;
    }

    public void setmPatrimonio(double mPatrimonio) {
        this.mPatrimonio = mPatrimonio;
    }

    public int getmEsquemaId() {
        return mEsquemaId;
    }

    public void setmEsquemaId(int mEsquemaId) {
        this.mEsquemaId = mEsquemaId;
    }

    public double getmPontos() {
        return mPontos;
    }

    public void setmPontos(double mPontos) {
        this.mPontos = mPontos;
    }

    public double getmValorTime() {
        return mValorTime;
    }

    public void setmValorTime(double mValorTime) {
        this.mValorTime = mValorTime;
    }

    public int getmRodadaAtual() {
        return mRodadaAtual;
    }

    public void setmRodadaAtual(int mRodadaAtual) {
        this.mRodadaAtual = mRodadaAtual;
    }

}

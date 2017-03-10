package br.com.myapp.ultracartola.business;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Gustavo on 08/03/2017.
 */

public class UserTeam {
    private List<Athlet> atletas;
    private JSONObject mClubes;
    private JSONObject mPosicoes;
    private JSONObject mStatus;
    private JSONObject mTime;
    private double mPatrimonio;
    private int mEsquemaId;
    private double mPontos;
    private double mValorTime;
    private int mRodadaAtual;

    public UserTeam() {
        this.atletas = new ArrayList<Athlet>();
    }

    public List<Athlet> getAtletas() {
        return this.atletas;
    }

    public void addAtleta(Athlet atleta) {
        this.atletas.add(atleta);
    }

    public JSONObject getmClubes() {
        return mClubes;
    }

    public void setmClubes(JSONObject mClubes) {
        this.mClubes = mClubes;
    }

    public JSONObject getmPosicoes() {
        return mPosicoes;
    }

    public void setmPosicoes(JSONObject mPosicoes) {
        this.mPosicoes = mPosicoes;
    }

    public JSONObject getmStatus() {
        return mStatus;
    }

    public void setmStatus(JSONObject mStatus) {
        this.mStatus = mStatus;
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

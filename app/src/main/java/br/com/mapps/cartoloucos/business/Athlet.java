package br.com.mapps.cartoloucos.business;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.mapps.cartoloucos.common.Common;

/**
 * Created by Gustavo on 08/03/2017.
 */

public class Athlet {
    private String nome;
    private String apelido;
    private String fotoUrl;
    private int atletaId;
    private int rodadaId;
    private int clubeId;
    private int posicaoId;
    private int statusId;
    private double pontos;
    private double preco;
    private double variacao;
    private double media;
    private int jogos;
//    private Match partida;
//    private Scout scout;

    private String fotoClubeUrl;


//    {
//        nome: "Maicon Pereira Roque",
//                apelido: "Maicon",
//            foto: "https://s.glbimg.com/es/sde/f/2016/05/01/f3000bd2b412a315dad8796d7080a492_FORMATO.png",
//            atleta_id: 71227,
//            rodada_id: 37,
//            clube_id: 276,
//            posicao_id: 3,
//            status_id: 7,
//            pontos_num: 9.1,
//            preco_num: 10.53,
//            variacao_num: 1.88,
//            media_num: 3.03,
//            jogos_num: 31,
//            partida: {
//        clube_casa_id: 276,
//                clube_casa_posicao: 10,
//                clube_visitante_id: 344,
//                aproveitamento_mandante: [
//        "",
//                "",
//                "",
//                "",
//                ""
//        ],
//        aproveitamento_visitante: [
//        "",
//                "",
//                "",
//                "",
//                ""
//        ],
//        clube_visitante_posicao: 19,
//                partida_data: "2016-12-11 17:00:00",
//                local: "Pacaembu",
//                valida: true,
//                placar_oficial_mandante: 5,
//                placar_oficial_visitante: 0,
//                url_confronto: "",
//                url_transmissao: ""
//    },
//        scout: {
//            A: 1,
//                    FC: 1,
//                    FD: 1,
//                    FF: 2,
//                    PE: 3,
//                    RB: 1,
//                    SG: 1
//        }
//    },


//    nome: "Paulo Autuori",
//    apelido: "Paulo Autuori",
//    foto: "https://s.glbimg.com/es/sde/f/2017/04/23/51bd7e83b21a81fb576ba2321746e749_FORMATO.png",
//    atleta_id: 36943,
//    rodada_id: 1,
//    clube_id: 293,
//    posicao_id: 6,
//    status_id: 7,
//    pontos_num: 0,
//    preco_num: 10,
//    variacao_num: 0,
//    media_num: 0,
//    jogos_num: 0,
//    scout: { }

    public Athlet(JSONObject object) {
        try {
            this.nome = object.getString("nome");
            this.apelido = object.getString("apelido");
            this.fotoUrl = object.getString("foto");
            this.atletaId = object.getInt("atleta_id");
            this.rodadaId = object.getInt("rodada_id");
            this.clubeId = object.getInt("clube_id");
            this.posicaoId = object.getInt("posicao_id");
            this.statusId = object.getInt("status_id");
            this.pontos= object.getDouble("pontos_num");
            this.preco = object.getDouble("preco_num");
            this.variacao = object.getDouble("variacao_num");
            this.media = object.getDouble("media_num");
            this.jogos = object.getInt("jogos_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNome() {
        return nome;
    }

    public String getApelido() {
        return apelido;
    }

    public String getFotoUrl() {
        return fotoUrl.replace("FORMATO", "80x80");
    }

    public int getAtletaId() {
        return atletaId;
    }

    public int getClubeId() {
        return clubeId;
    }

    public int getPosicaoId() {
        return posicaoId;
    }

    public double getPontos() {
        return pontos;
    }

    public double getPreco() {
        return preco;
    }

    public double getVariacao() {
        return variacao;
    }

    public double getMedia() {
        return media;
    }

    public int getJogos() {
        return jogos;
    }

    public String getFotoClubeUrl() {

        if ( getClubeId() != 0 )
        {
            Common.setClubPhotoUrl();
            return Common.CLUBS.get(getClubeId());
        }

        return fotoClubeUrl;
    }
}

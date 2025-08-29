package animais.model;

import jakarta.persistence.*;

@Entity
public class Animal {

    public Long getId() { return id;}

    public void setId(Long id) { this.id = id;}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String especie;
    private String raca;
    private Sexo sexo;
    private Integer idade_aproximada;
    private Tamanho tamanho;
    private String urlImagem;

    public enum Sexo { MACHO, FEMEA }
    public enum Tamanho { PEQUENO, MEDIO, GRANDE }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Integer getIdade_aproximada() {
        return idade_aproximada;
    }

    public void setIdade_aproximada(Integer idade_aproximada) {
        this.idade_aproximada = idade_aproximada;
    }

    public Tamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(Tamanho tamanho) {
        this.tamanho = tamanho;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

}

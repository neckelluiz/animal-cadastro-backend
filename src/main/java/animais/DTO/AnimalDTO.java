package animais.DTO;

public class AnimalDTO {
    private String especie;
    private String raca;
    private String sexo;
    private Integer idade_aproximada;
    private String tamanho;
    private String urlImagem;

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getIdade_aproximada() {
        return idade_aproximada;
    }

    public void setIdade_aproximada(Integer idade_aproximada) {
        this.idade_aproximada = idade_aproximada;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }



}
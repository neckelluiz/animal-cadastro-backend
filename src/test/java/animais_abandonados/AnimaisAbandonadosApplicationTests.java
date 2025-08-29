package animais.controller;

import animais.DTO.AnimalDTO;
import animais.model.Animal;
import animais.repository.AnimalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de teste para o AnimalController.
 * Usa @WebMvcTest para focar no teste dos controllers,
 * sem carregar toda a aplicação Spring.
 */
@WebMvcTest(AnimalController.class)
class AnimalControllerTest {

    // Objeto para simular requisições HTTP para o controller
    @Autowired
    private MockMvc mockMvc;

    // Objeto para converter objetos Java em JSON e vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    // O repositório real é substituído por um "mock" (uma versão falsa)
    // para que os testes do controller não dependam do banco de dados real.
    @Autowired
    private AnimalRepository animalRepository;

    // Objetos de exemplo que serão usados em vários testes
    private Animal animalExemplo1;
    private AnimalDTO animalDTOExemplo1;

    /**
     * Configurações iniciais antes de cada teste.
     * Aqui criamos os objetos de exemplo e resetamos o mock do repositório.
     */
    @BeforeEach
    void setup() {
        // Inicializa um objeto Animal de exemplo
        animalExemplo1 = new Animal();
        animalExemplo1.setId(1L);
        animalExemplo1.setEspecie("CACHORRO");
        animalExemplo1.setRaca("Golden Retriever");
        animalExemplo1.setSexo(Animal.Sexo.MACHO);
        animalExemplo1.setIdade_aproximada(3);
        animalExemplo1.setTamanho(Animal.Tamanho.GRANDE);
        animalExemplo1.setUrlImagem("http://url.com/golden.jpg");

        // Inicializa um objeto AnimalDTO de exemplo, que é o que o frontend envia
        animalDTOExemplo1 = new AnimalDTO();
        animalDTOExemplo1.setEspecie("CACHORRO");
        animalDTOExemplo1.setRaca("Golden Retriever");
        animalDTOExemplo1.setSexo("MACHO");
        animalDTOExemplo1.setIdade_aproximada(3);
        animalDTOExemplo1.setTamanho("GRANDE");
        animalDTOExemplo1.setUrlImagem("http://url.com/golden.jpg");

        // Limpa todas as interações e stubs do mock do repositório
        // Isso garante que cada teste seja independente.
        Mockito.reset(animalRepository);
    }

    /**
     * Configuração para o mock do repositório.
     * Em vez de usar @MockBean (que está depreciado), criamos o mock explicitamente aqui
     * e o Spring o injeta onde AnimalRepository é necessário.
     */
    @TestConfiguration
    static class AnimalControllerTestConfig {
        @Bean
        public AnimalRepository animalRepository() {
            return Mockito.mock(AnimalRepository.class); // Cria uma versão "falsa" do repositório
        }
    }

    /**
     * Teste: Deve listar todos os animais.
     * Verifica se o endpoint GET /animais retorna uma lista de animais e o status OK (200).
     */
    @Test
    void deveListarTodosOsAnimais() throws Exception {
        // Quando o método findAll() do repositório for chamado, retorne nossa lista de exemplo
        when(animalRepository.findAll()).thenReturn(Arrays.asList(animalExemplo1));

        // Simula uma requisição GET para /animais
        mockMvc.perform(get("/animais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especie").value(animalExemplo1.getEspecie())); // Verifica um campo do primeiro animal retornado
    }

    /**
     * Teste: Deve buscar um animal por ID.
     * Verifica se o endpoint GET /animais/{id} retorna o animal correto e o status OK (200).
     */
    @Test
    void deveBuscarAnimalPorId() throws Exception {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animalExemplo1));

        mockMvc.perform(get("/animais/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.raca").value(animalExemplo1.getRaca()));
    }

    /**
     * Teste: Deve retornar "Não Encontrado" para animal inexistente.
     * Verifica se o endpoint GET /animais/{id} retorna 404 Not Found para um ID que não existe.
     */
    @Test
    void deveRetornarNaoEncontradoParaAnimalInexistente() throws Exception {
        when(animalRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/animais/2"))
                .andExpect(status().isNotFound());
    }

    /**
     * Teste: Deve criar um novo animal.
     * Verifica se o endpoint POST /animais cria um animal com sucesso e retorna 201 Created.
     */
    @Test
    void deveCriarNovoAnimal() throws Exception {
        when(animalRepository.save(any(Animal.class))).thenReturn(animalExemplo1);

        mockMvc.perform(post("/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animalDTOExemplo1)))
                .andExpect(status().isCreated());
    }

    /**
     * Teste: Deve retornar "Bad Request" para Sexo inválido ao criar.
     * Verifica se a criação falha com 400 Bad Request se o campo 'sexo' no DTO for inválido.
     */
    @Test
    void deveRetornarErroParaSexoInvalidoAoCriar() throws Exception {
        animalDTOExemplo1.setSexo("INVALIDO");

        mockMvc.perform(post("/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animalDTOExemplo1)))
                .andExpect(status().isBadRequest()); // Espera 400 Bad Request
    }

    /**
     * Teste: Deve atualizar um animal existente.
     * Verifica se o endpoint PUT /animais/{id} atualiza os dados de um animal e retorna 200 OK.
     */
    @Test
    void deveAtualizarAnimalExistente() throws Exception {
        AnimalDTO animalDTOAtualizado = new AnimalDTO();
        animalDTOAtualizado.setEspecie("GATO");

        Animal animalAtualizadoEsperado = new Animal();
        animalAtualizadoEsperado.setId(1L);
        animalAtualizadoEsperado.setEspecie("GATO");
        animalAtualizadoEsperado.setRaca(animalExemplo1.getRaca());
        animalAtualizadoEsperado.setSexo(animalExemplo1.getSexo());
        animalAtualizadoEsperado.setIdade_aproximada(animalExemplo1.getIdade_aproximada());
        animalAtualizadoEsperado.setTamanho(animalExemplo1.getTamanho());
        animalAtualizadoEsperado.setUrlImagem(animalExemplo1.getUrlImagem());


        // Quando findById(1L) for chamado, retorne o animal original
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animalExemplo1));
        // Quando save() for chamado com qualquer Animal, retorne o animal com os dados atualizados
        when(animalRepository.save(any(Animal.class))).thenReturn(animalAtualizadoEsperado);

        mockMvc.perform(put("/animais/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animalDTOAtualizado)))
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(jsonPath("$.especie").value("GATO"));
    }

    /**
     * Teste: Deve deletar um animal.
     * Verifica se o endpoint DELETE /animais/{id} remove um animal e retorna 204 No Content.
     */
    @Test
    void deveDeletarAnimal() throws Exception {
        when(animalRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/animais/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Teste: Deve retornar "Não Encontrado" ao deletar animal inexistente.
     * Verifica se o endpoint DELETE /animais/{id} retorna 404 Not Found para um ID que não existe.
     */
    @Test
    void deveRetornarNaoEncontradoAoDeletarAnimalInexistente() throws Exception {
        when(animalRepository.existsById(2L)).thenReturn(false);

        mockMvc.perform(delete("/animais/2"))
                .andExpect(status().isNotFound());
    }
}
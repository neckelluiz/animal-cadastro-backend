package animais.controller;

import animais.DTO.AnimalDTO;
import animais.model.Animal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import animais.repository.AnimalRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalRepository animalRepository;

    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @GetMapping
    public List<Animal> listarTodosAnimais() {
        return animalRepository.findAll();
    }

    @GetMapping("/{id}")
    public Animal buscarPorId(@PathVariable Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado"));
    }

    @PostMapping
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalDTO animalDTO) {
        Animal animal = new Animal();

        animal.setEspecie(animalDTO.getEspecie());
        animal.setRaca(animalDTO.getRaca());

        try {
            if (animalDTO.getSexo() != null) {
                animal.setSexo(Animal.Sexo.valueOf(animalDTO.getSexo().toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sexo inválido: " + animalDTO.getSexo());
        }

        try {
            if (animalDTO.getTamanho() != null) {
                animal.setTamanho(Animal.Tamanho.valueOf(animalDTO.getTamanho().toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tamanho inválido: " + animalDTO.getTamanho());
        }

        if (animalDTO.getIdade_aproximada() != null) animal.setIdade_aproximada(animalDTO.getIdade_aproximada());
        if (animalDTO.getUrlImagem() != null) animal.setUrlImagem(animalDTO.getUrlImagem());

        Animal savedAnimal = animalRepository.save(animal);
        return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
    }

    // Método para atualizar um animal existente
    @PutMapping("/{id}")
    public ResponseEntity<Animal> atualizarAnimal(@PathVariable Long id, @RequestBody AnimalDTO animalDTO) {
        Optional<Animal> animalExistente = animalRepository.findById(id);
        if (animalExistente.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado");
        }

        Animal animal = animalExistente.get();
        // Atualiza apenas os campos que vieram no DTO
        if (animalDTO.getEspecie() != null) animal.setEspecie(animalDTO.getEspecie());
        if (animalDTO.getRaca() != null) animal.setRaca(animalDTO.getRaca());
        if (animalDTO.getIdade_aproximada() != null) animal.setIdade_aproximada(animalDTO.getIdade_aproximada());
        if (animalDTO.getUrlImagem() != null) animal.setUrlImagem(animalDTO.getUrlImagem());

        // Tratamento para Enums (garantindo que os valores sejam válidos)
        try {
            if (animalDTO.getSexo() != null) {
                animal.setSexo(Animal.Sexo.valueOf(animalDTO.getSexo().toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sexo inválido: " + animalDTO.getSexo());
        }

        try {
            if (animalDTO.getTamanho() != null) {
                animal.setTamanho(Animal.Tamanho.valueOf(animalDTO.getTamanho().toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tamanho inválido: " + animalDTO.getTamanho());
        }


        Animal updatedAnimal = animalRepository.save(animal);
        return ResponseEntity.ok(updatedAnimal);
    }

    // EXCLUIR UM ANIMAL (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletarAnimal(@PathVariable Long id) {
        if (!animalRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado");
        }
        animalRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Animal excluído com sucesso!");

        return ResponseEntity.ok(response);
    }
}
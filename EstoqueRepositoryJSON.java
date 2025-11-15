package br.com.sistema.automotivo.repository;

import br.com.sistema.automotivo.model.Marca;
import br.com.sistema.automotivo.model.Modelo;
import br.com.sistema.automotivo.model.Veiculo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class EstoqueRepositoryJSON {

    private final Path arquivo = Path.of("estoque.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public record BackupDTO(List<Marca> marcas, List<Modelo> modelos, List<Veiculo> veiculos) {}

    // salva o backup completo (marcas, modelos, veículos)
    public void salvar(List<Marca> marcas, List<Modelo> modelos, List<Veiculo> veiculos) {
        BackupDTO dto = new BackupDTO(marcas, modelos, veiculos);
        try (Writer w = Files.newBufferedWriter(arquivo)) {
            gson.toJson(dto, w);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar JSON: " + e.getMessage(), e);
        }
    }

    // carrega o backup (retorna null se não existir)
    public BackupDTO carregar() {
        if (!Files.exists(arquivo)) return null;
        try (Reader r = Files.newBufferedReader(arquivo)) {
            return gson.fromJson(r, BackupDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler JSON: " + e.getMessage(), e);
        }
    }

    // utilitário: ler raw string (opcional)
    public String carregarRaw() {
        if (!Files.exists(arquivo)) return null;
        try {
            return Files.readString(arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo JSON: " + e.getMessage(), e);
        }
    }
}


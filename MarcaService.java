package br.com.sistema.automotivo.service;

import br.com.sistema.automotivo.model.Marca;
import br.com.sistema.automotivo.model.Modelo;
import br.com.sistema.automotivo.repository.MarcaRepositorySQL;
import br.com.sistema.automotivo.repository.ModeloRepositorySQL;

import java.util.List;

public class MarcaService {

    private final MarcaRepositorySQL repository = new MarcaRepositorySQL();
    private final ModeloRepositorySQL modeloRepository = new ModeloRepositorySQL();

    // -----------------------------------
    // CADASTRAR MARCA
    // -----------------------------------
    public void cadastrarMarca(String nome) {

        // valida nome vazio
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("❌ O nome da marca não pode estar vazio.");
            return;
        }

        // valida duplicidade
        List<Marca> existentes = repository.listar();
        boolean duplicada = existentes.stream()
                .anyMatch(m -> m.getNome().equalsIgnoreCase(nome));

        if (duplicada) {
            System.out.println("❌ ERRO: Já existe uma marca com esse nome!");
            return;
        }

        repository.inserir(new Marca(0, nome));
        System.out.println("✔ Marca cadastrada!");
    }

    // -----------------------------------
    // LISTAR
    // -----------------------------------
    public List<Marca> listarMarcas() {
        return repository.listar();
    }

    // -----------------------------------
    // BUSCAR POR ID
    // -----------------------------------
    public Marca buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    // -----------------------------------
    // REMOVER
    // -----------------------------------
    public void removerMarca(int id) {

        // impedir remover marca com modelos
        List<Modelo> modelos = modeloRepository.listar();
        boolean emUso = modelos.stream()
                .anyMatch(m -> m.getMarca().getId() == id);

        if (emUso) {
            System.out.println("❌ Não é possível remover. Existem modelos cadastrados nesta marca.");
            return;
        }

        boolean ok = repository.remover(id);
        if (ok)
            System.out.println("✔ Marca removida com sucesso!");
        else
            System.out.println("❌ Marca não encontrada.");
    }
}

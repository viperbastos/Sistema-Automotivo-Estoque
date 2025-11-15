package br.com.sistema.automotivo.service;

import br.com.sistema.automotivo.model.Marca;
import br.com.sistema.automotivo.model.Modelo;
import br.com.sistema.automotivo.model.Veiculo;
import br.com.sistema.automotivo.repository.MarcaRepositorySQL;
import br.com.sistema.automotivo.repository.ModeloRepositorySQL;
import br.com.sistema.automotivo.repository.VeiculoRepositorySQL;

import java.util.List;

public class ModeloService {

    private final ModeloRepositorySQL repository = new ModeloRepositorySQL();
    private final MarcaRepositorySQL marcaRepository = new MarcaRepositorySQL();
    private final VeiculoRepositorySQL veiculoRepository = new VeiculoRepositorySQL();

    // -----------------------------------
    // CADASTRAR MODELO
    // -----------------------------------
    public void cadastrarModelo(String nome, int idMarca) {

        // validar nome vazio
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("❌ O nome do modelo não pode estar vazio.");
            return;
        }

        // validar marca existe
        Marca marca = marcaRepository.buscarPorId(idMarca);
        if (marca == null) {
            System.out.println("❌ Marca não encontrada!");
            return;
        }

        // impedir duplicidade (modelo + marca)
        List<Modelo> existentes = repository.listar();
        boolean duplicado = existentes.stream()
                .anyMatch(m -> m.getNome().equalsIgnoreCase(nome)
                        && m.getMarca().getId() == idMarca);

        if (duplicado) {
            System.out.println("❌ Este modelo já está cadastrado nesta marca.");
            return;
        }

        repository.inserir(new Modelo(0, nome, marca));
        System.out.println("✔ Modelo cadastrado!");
    }

    // -----------------------------------
    // LISTAR
    // -----------------------------------
    public List<Modelo> listarModelos() {
        return repository.listar();
    }

    // -----------------------------------
    // BUSCAR POR ID
    // -----------------------------------
    public Modelo buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    // -----------------------------------
    // REMOVER (retorna boolean para o MenuView)
    // -----------------------------------
    public boolean removerModelo(int id) {

        // impedir remover modelo com veículos vinculados
        List<Veiculo> veiculos = veiculoRepository.listar();
        boolean emUso = veiculos.stream()
                .anyMatch(v -> v.getModelo() != null && v.getModelo().getId() == id);

        if (emUso) {
            System.out.println("❌ Não é possível remover. Existem veículos cadastrados com este modelo.");
            return false;
        }

        boolean ok = repository.remover(id);

        if (ok)
            System.out.println("✔ Modelo removido!");
        else
            System.out.println("❌ Modelo não encontrado.");

        return ok;
    }
}

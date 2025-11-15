package br.com.sistema.automotivo.service;

import br.com.sistema.automotivo.model.Marca;
import br.com.sistema.automotivo.model.Modelo;
import br.com.sistema.automotivo.model.Veiculo;
import br.com.sistema.automotivo.repository.*;

import java.util.List;

public class EstoqueService {

    private final RelatorioRepositorySQL relatorioRepository = new RelatorioRepositorySQL();
    private final VeiculoRepositorySQL repoVeiculo = new VeiculoRepositorySQL();
    private final ModeloRepositorySQL repoModelo = new ModeloRepositorySQL();
    private final EstoqueRepositoryJSON repoJSON = new EstoqueRepositoryJSON();

    // -----------------------------
    // CREATE
    // -----------------------------
    public void adicionarVeiculo(Veiculo v) {

        if (v.getPlaca() == null || v.getPlaca().trim().isEmpty()) {
            System.out.println("❌ A placa não pode ser vazia!");
            return;
        }

        if (repoVeiculo.buscarPorPlaca(v.getPlaca()) != null) {
            System.out.println("❌ Já existe veículo com esta placa.");
            return;
        }

        if (v.getAno() < 1950 || v.getAno() > 2050) {
            System.out.println("❌ Ano inválido.");
            return;
        }

        if (v.getPreco() <= 0) {
            System.out.println("❌ Preço deve ser maior que zero.");
            return;
        }

        Modelo modelo = repoModelo.buscarPorId(v.getModelo().getId());
        if (modelo == null) {
            System.out.println("❌ Modelo não encontrado!");
            return;
        }

        repoVeiculo.inserir(v);
        System.out.println("✔ Veículo adicionado!");
    }

    // -----------------------------
    // READ
    // -----------------------------
    public List<Veiculo> listarVeiculos() {
        return repoVeiculo.listar();
    }

    public Veiculo buscarPorPlaca(String placa) {
        return repoVeiculo.buscarPorPlaca(placa);
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    public void atualizarVeiculo(Veiculo v) {
        repoVeiculo.atualizar(v);
        System.out.println("✔ Veículo atualizado!");
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    public void removerVeiculo(String placa) {
        if (repoVeiculo.remover(placa)) {
            System.out.println("✔ Veículo removido!");
        } else {
            System.out.println("❌ Veículo não encontrado.");
        }
    }

    // -----------------------------
    // RELATÓRIOS
    // -----------------------------
    public int relatorioTotalVeiculos() {
        return relatorioRepository.contarVeiculos();
    }

    public double relatorioValorTotal() {
        return relatorioRepository.valorTotal();
    }

    public long relatorioDisponiveis() {
        return relatorioRepository.contarPorStatus("disponivel");
    }

    public long relatorioVendidos() {
        return relatorioRepository.contarPorStatus("vendido");
    }

    public void relatorioPorMarca() {
        relatorioRepository.contarPorMarca();
    }

    // -----------------------------
// EXPORTAÇÃO JSON (Backup)
// -----------------------------
    public void exportarJSON() {
        try {
            MarcaRepositorySQL marcaRepo = new MarcaRepositorySQL();
            ModeloRepositorySQL modeloRepo = new ModeloRepositorySQL();
            VeiculoRepositorySQL veiculoRepo = new VeiculoRepositorySQL();

            List<Marca> marcas = marcaRepo.listar();
            List<Modelo> modelos = modeloRepo.listar();
            List<Veiculo> veiculos = veiculoRepo.listar();

            repoJSON.salvar(marcas, modelos, veiculos);
            System.out.println("✔ Backup salvo em estoque.json");

        } catch (Exception e) {
            System.out.println("❌ Erro ao exportar JSON: " + e.getMessage());
        }
    }

    // -----------------------------
// IMPORTAÇÃO JSON (Restaurar)
// -----------------------------
    public void importarJSON() {
        try {
            var dto = repoJSON.carregar();
            if (dto == null) {
                System.out.println("❌ Arquivo estoque.json não encontrado.");
                return;
            }

            MarcaRepositorySQL marcaRepo = new MarcaRepositorySQL();
            ModeloRepositorySQL modeloRepo = new ModeloRepositorySQL();
            VeiculoRepositorySQL veiculoRepo = new VeiculoRepositorySQL();

            // --- Marcas ---
            List<Marca> marcasExistentes = marcaRepo.listar();
            for (Marca m : dto.marcas()) {
                boolean existe = marcasExistentes.stream()
                        .anyMatch(x -> x.getNome().equalsIgnoreCase(m.getNome()));

                if (!existe) {
                    marcaRepo.inserir(m);
                    marcasExistentes = marcaRepo.listar();
                }
            }

            // --- Modelos ---
            List<Modelo> modelosExistentes = modeloRepo.listar();
            for (Modelo md : dto.modelos()) {

                Marca marcaBanco = marcaRepo.listar().stream()
                        .filter(x -> x.getNome().equalsIgnoreCase(md.getMarca().getNome()))
                        .findFirst()
                        .orElse(null);

                if (marcaBanco != null) {
                    boolean existe = modelosExistentes.stream()
                            .anyMatch(x ->
                                    x.getNome().equalsIgnoreCase(md.getNome()) &&
                                            x.getMarca().getId() == marcaBanco.getId()
                            );

                    if (!existe) {
                        modeloRepo.inserir(new Modelo(0, md.getNome(), marcaBanco));
                        modelosExistentes = modeloRepo.listar();
                    }
                }
            }

            // --- Veículos ---
            for (Veiculo v : dto.veiculos()) {

                if (veiculoRepo.buscarPorPlaca(v.getPlaca()) != null)
                    continue;

                Modelo modeloBanco = modeloRepo.listar().stream()
                        .filter(x ->
                                x.getNome().equalsIgnoreCase(v.getModelo().getNome()) &&
                                        x.getMarca().getNome().equalsIgnoreCase(
                                                v.getModelo().getMarca().getNome()
                                        )
                        )
                        .findFirst()
                        .orElse(null);

                if (modeloBanco != null) {
                    Veiculo novo = new Veiculo(
                            v.getPlaca(),
                            v.getAno(),
                            v.getPreco(),
                            v.getStatus(),
                            modeloBanco
                    );
                    veiculoRepo.inserir(novo);
                }
            }

            System.out.println("✔ Importação concluída sem duplicar.");

        } catch (Exception e) {
            System.out.println("❌ Erro ao importar JSON:");
            e.printStackTrace();
        }
    }
}


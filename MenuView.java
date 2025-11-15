package br.com.sistema.automotivo.view;

import br.com.sistema.automotivo.model.Marca;
import br.com.sistema.automotivo.model.Modelo;
import br.com.sistema.automotivo.model.Veiculo;
import br.com.sistema.automotivo.service.MarcaService;
import br.com.sistema.automotivo.service.ModeloService;
import br.com.sistema.automotivo.service.EstoqueService;
import br.com.sistema.automotivo.util.ValidacaoUtils;

import java.util.List;

public class MenuView {

    private final MarcaService marcaService = new MarcaService();
    private final ModeloService modeloService = new ModeloService();
    private final EstoqueService estoqueService = new   EstoqueService();

    public void exibirMenu() {
        int opcao;

        do {
            System.out.println("\n====== SISTEMA AUTOMOTIVO ======");
            System.out.println("1 - Cadastrar Marca");
            System.out.println("2 - Listar Marcas");
            System.out.println("3 - Cadastrar Modelo");
            System.out.println("4 - Listar Modelos");
            System.out.println("5 - Cadastrar Veículo");
            System.out.println("6 - Listar Veículos");
            System.out.println("7 - Buscar Veículo por Placa");
            System.out.println("8 - Atualizar Veículo");
            System.out.println("9 - Remover Veículo");
            System.out.println("10 - Relatórios");
            System.out.println("11 - Remover Modelo");
            System.out.println("12 - Exportar JSON (Backup)");
            System.out.println("13 - Importar JSON (Restaurar)");
            System.out.println("0 - Sair");



            opcao = ValidacaoUtils.lerInt("Opção: ");

            switch (opcao) {
                case 1 -> cadastrarMarca();
                case 2 -> listarMarcas();
                case 3 -> cadastrarModelo();
                case 4 -> listarModelos();
                case 5 -> cadastrarVeiculo();
                case 6 -> listarVeiculos();
                case 7 -> buscarVeiculo();
                case 8 -> atualizarVeiculo();
                case 9 -> removerVeiculo();
                case 10 -> menuRelatorios();
                case 11 -> removerModelo();
                case 12 -> estoqueService.salvarJSON();   // EXPORTAR
                case 13 -> estoqueService.carregarJSON(); // IMPORTAR
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    // ----------------------------
    // MARCAS
    // ----------------------------
    private void cadastrarMarca() {
        String nome = ValidacaoUtils.lerTexto("Nome da marca: ");
        marcaService.cadastrarMarca(nome);
    }

    private void listarMarcas() {
        List<Marca> lista = marcaService.listarMarcas();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma marca cadastrada.");
            return;
        }
        lista.forEach(System.out::println);
    }

    // ----------------------------
    // MODELOS
    // ----------------------------
    private void cadastrarModelo() {
        String nome = ValidacaoUtils.lerTexto("Nome do modelo: ");
        listarMarcas();
        int idMarca = ValidacaoUtils.lerInt("Informe o ID da marca: ");

        Marca marca = marcaService.buscarPorId(idMarca);
        if (marca == null) {
            System.out.println("Marca não encontrada!");
            return;
        }

        modeloService.cadastrarModelo(nome, idMarca);
    }
    private void removerModelo() {
        // mostra os modelos
        listarModelos();

        // pergunta o ID
        int id = ValidacaoUtils.lerInt("ID do modelo para remover: ");

        // tenta remover via serviço
        boolean ok = modeloService.removerModelo(id);

        if (ok) {
            System.out.println("✔ Modelo removido com sucesso!");
        } else {
            System.out.println("❌ Não foi possível remover. Verifique se o modelo existe ou se há veículos vinculados.");
        }
    }

    private void listarModelos() {
        List<Modelo> lista = modeloService.listarModelos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum modelo cadastrado.");
            return;
        }
        lista.forEach(System.out::println);
    }

    // ----------------------------
    // VEÍCULOS
    // ----------------------------
    private void cadastrarVeiculo() {
        listarModelos();
        int idModelo = ValidacaoUtils.lerInt("ID do modelo: ");

        Modelo modelo = modeloService.buscarPorId(idModelo);
        if (modelo == null) {
            System.out.println("Modelo não encontrado!");
            return;
        }

        String placa = ValidacaoUtils.lerTexto("Placa: ");

        // Verifica se já existe veículo com essa placa
        Veiculo existing = estoqueService.buscarPorPlaca(placa);
        if (existing != null) {
            System.out.println("❌ Já existe um veículo com essa placa.");
            return;
        }

        int ano = ValidacaoUtils.lerInt("Ano: ");
        double preco = ValidacaoUtils.lerDouble("Preço: ");
        String status = ValidacaoUtils.lerStatus("Status (Disponível / Vendido): ");

        Veiculo v = new Veiculo(placa, ano, preco, status, modelo);
        // Caso sua classe Veiculo tenha outro construtor, ajuste a ordem/assinatura conforme seu código.
        // Aqui assumimos Veiculo(String placa, int ano, double preco, String status, Modelo modelo)

        estoqueService.adicionarVeiculo(v);
    }

    private void listarVeiculos() {
        List<Veiculo> lista = estoqueService.listarVeiculos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private void buscarVeiculo() {
        String placa = ValidacaoUtils.lerTexto("Placa: ");
        Veiculo v = estoqueService.buscarPorPlaca(placa);
        if (v == null) {
            System.out.println("Veículo não encontrado!");
        } else {
            System.out.println(v);
        }
    }

    private void atualizarVeiculo() {
        String placa = ValidacaoUtils.lerTexto("Placa do veículo: ");
        Veiculo v = estoqueService.buscarPorPlaca(placa);

        if (v == null) {
            System.out.println("Veículo não encontrado!");
            return;
        }

        int novoAno = ValidacaoUtils.lerInt("Novo ano: ");
        double novoPreco = ValidacaoUtils.lerDouble("Novo preço: ");
        String novoStatus = ValidacaoUtils.lerStatus("Novo status (Disponível / Vendido): ");

        v.setAno(novoAno);
        v.setPreco(novoPreco);
        v.setStatus(novoStatus);

        estoqueService.atualizarVeiculo(v);
    }

    private void removerVeiculo() {
        String placa = ValidacaoUtils.lerTexto("Placa do veículo: ");
        estoqueService.removerVeiculo(placa);
    }

    // ----------------------------
    // RELATÓRIOS
    // ----------------------------
    private void menuRelatorios() {
        System.out.println("\n---- RELATÓRIOS ----");
        System.out.println("1 - Total de veículos");
        System.out.println("2 - Total por marca");
        System.out.println("3 - Valor total em estoque");
        System.out.println("4 - Disponíveis / Vendidos");
        System.out.println("0 - Voltar");

        int opcao = ValidacaoUtils.lerInt("Opção: ");

        switch (opcao) {
            case 1 -> relatorioTotalVeiculos();
            case 2 -> relatorioPorMarca();
            case 3 -> relatorioValorTotal();
            case 4 -> relatorioStatus();
            case 0 -> {}
            default -> System.out.println("Opção inválida!");
        }
    }

    private void relatorioTotalVeiculos() {
        int total = estoqueService.relatorioTotalVeiculos();
        System.out.println("TOTAL: " + total);
    }

    private void relatorioPorMarca() {
        System.out.println("\nVeículos por marca:");
        estoqueService.relatorioPorMarca();
    }

    private void relatorioValorTotal() {
        double total = estoqueService.relatorioValorTotal();
        System.out.printf("Valor total do estoque: R$ %.2f%n", total);
    }

    private void relatorioStatus() {
        long disponiveis = estoqueService.relatorioDisponiveis();
        long vendidos = estoqueService.relatorioVendidos();

        System.out.println("Disponíveis: " + disponiveis);
        System.out.println("Vendidos: " + vendidos);
    }
}

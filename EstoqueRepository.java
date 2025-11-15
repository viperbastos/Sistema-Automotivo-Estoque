package br.com.sistema.automotivo.repository;

import br.com.sistema.automotivo.model.Veiculo;
import java.util.ArrayList;
import java.util.List;

public class EstoqueRepository {
    private final List<Veiculo> veiculos = new ArrayList<>();

    public void adicionar(Veiculo veiculo) {
        veiculos.add(veiculo);
    }

    public List<Veiculo> listar() {
        return new ArrayList<>(veiculos); // retorna cópia
    }

    public boolean removerPorPlaca(String placa) {
        return veiculos.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
    }

    public Veiculo buscarPorPlaca(String placa) {
        return veiculos.stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElse(null);
    }

    public List<Veiculo> filtrarPorMarca(String marca) {
        return veiculos.stream()
                .filter(v -> v.getModelo().getMarca().getNome().equalsIgnoreCase(marca))
                .toList();
    }

    public List<Veiculo> filtrarPorModelo(String modelo) {
        return veiculos.stream()
                .filter(v -> v.getModelo().getNome().equalsIgnoreCase(modelo))
                .toList();
    }

    public List<Veiculo> filtrarPorAno(int ano) {
        return veiculos.stream()
                .filter(v -> v.getAno() == ano)
                .toList();
    }

    public List<Veiculo> filtrarPorPrecoMax(double preco) {
        return veiculos.stream()
                .filter(v -> v.getPreco() <= preco)
                .toList();
    }

    public List<Veiculo> filtrarPorPrecoMin(double preco) {
        return veiculos.stream()
                .filter(v -> v.getPreco() >= preco)
                .toList();
    }

    public List<Veiculo> filtrarPorIntervalo(double min, double max) {
        return veiculos.stream()
                .filter(v -> v.getPreco() >= min && v.getPreco() <= max)
                .toList();
    }

    public List<Veiculo> filtrarPorStatus(String status) {
        return veiculos.stream()
                .filter(v -> v.getStatus().equalsIgnoreCase(status))
                .toList();
    }


}


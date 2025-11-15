package br.com.sistema.automotivo.repository;

import br.com.sistema.automotivo.model.Marca;
import java.util.ArrayList;
import java.util.List;

public class MarcaRepository {
    private final List<Marca> marcas = new ArrayList<>();

    public void adicionar(Marca m) {
        marcas.add(m);
    }

    public List<Marca> listar() {
        return new ArrayList<>(marcas);
    }

    public Marca buscarPorId(int id) {
        return marcas.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

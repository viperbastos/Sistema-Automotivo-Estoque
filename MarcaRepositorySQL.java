package br.com.sistema.automotivo.repository;

import br.com.sistema.automotivo.database.ConnectionFactory;
import br.com.sistema.automotivo.model.Marca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaRepositorySQL {

    // -----------------------------
    // INSERIR
    // -----------------------------
    public void inserir(Marca marca) {
        String sql = "INSERT INTO marca (nome) VALUES (?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, marca.getNome());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir marca: " + e.getMessage(), e);
        }
    }

    // -----------------------------
    // LISTAR TODAS
    // -----------------------------
    public List<Marca> listar() {
        List<Marca> lista = new ArrayList<>();
        String sql = "SELECT * FROM marca";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Marca(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar marcas: " + e.getMessage(), e);
        }

        return lista;
    }

    // -----------------------------
// REMOVER POR ID
// -----------------------------
    public boolean remover(int id) {
        String sql = "DELETE FROM marca WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover marca: " + e.getMessage(), e);
        }
    }

    // -----------------------------
    // BUSCAR POR ID
    // -----------------------------
    public Marca buscarPorId(int id) {
        String sql = "SELECT * FROM marca WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Marca(
                            rs.getInt("id"),
                            rs.getString("nome")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar marca: " + e.getMessage(), e);
        }

        return null;
    }
}

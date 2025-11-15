package br.com.sistema.automotivo.repository;

import br.com.sistema.automotivo.database.ConnectionFactory;
import br.com.sistema.automotivo.model.Marca;
import br.com.sistema.automotivo.model.Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloRepositorySQL {

    // INSERT
    public void inserir(Modelo modelo) {
        String sql = "INSERT INTO modelo (nome, id_marca) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, modelo.getNome());
            st.setInt(2, modelo.getMarca().getId());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir modelo: " + e.getMessage(), e);
        }
    }

    // LISTAR
    public List<Modelo> listar() {
        List<Modelo> lista = new ArrayList<>();

        String sql = """
            SELECT 
                mo.id AS modelo_id,
                mo.nome AS modelo_nome,
                ma.id AS marca_id,
                ma.nome AS marca_nome
            FROM modelo mo
            JOIN marca ma ON mo.id_marca = ma.id
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {

                Marca marca = new Marca(
                        rs.getInt("marca_id"),
                        rs.getString("marca_nome")
                );

                Modelo modelo = new Modelo(
                        rs.getInt("modelo_id"),
                        rs.getString("modelo_nome"),
                        marca
                );

                lista.add(modelo);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar modelos: " + e.getMessage(), e);
        }

        return lista;
    }

    // BUSCAR POR ID
    public Modelo buscarPorId(int id) {

        String sql = """
            SELECT 
                mo.id AS modelo_id,
                mo.nome AS modelo_nome,
                ma.id AS marca_id,
                ma.nome AS marca_nome
            FROM modelo mo
            JOIN marca ma ON mo.id_marca = ma.id
            WHERE mo.id = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {

                    Marca marca = new Marca(
                            rs.getInt("marca_id"),
                            rs.getString("marca_nome")
                    );

                    return new Modelo(
                            rs.getInt("modelo_id"),
                            rs.getString("modelo_nome"),
                            marca
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar modelo por ID: " + e.getMessage(), e);
        }

        return null;
    }

    // REMOVER POR ID
    public boolean remover(int id) {
        String sql = "DELETE FROM modelo WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Erro ao remover modelo: " + e.getMessage());
        }

        return false;
    }
}

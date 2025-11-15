package br.com.sistema.automotivo.repository;

import br.com.sistema.automotivo.database.ConnectionFactory;
import br.com.sistema.automotivo.model.Marca;
import br.com.sistema.automotivo.model.Modelo;
import br.com.sistema.automotivo.model.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoRepositorySQL {

    // -----------------------------
    // INSERIR
    // -----------------------------
    public void inserir(Veiculo v) {
        String sql = "INSERT INTO veiculo (placa, ano, preco, status, id_modelo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, v.getPlaca());
            st.setInt(2, v.getAno());
            st.setDouble(3, v.getPreco());
            st.setString(4, v.getStatus());
            st.setInt(5, v.getModelo().getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir veículo: " + e.getMessage(), e);
        }
    }

    // -----------------------------
    // LISTAR
    // -----------------------------
    public List<Veiculo> listar() {
        List<Veiculo> lista = new ArrayList<>();

        String sql = """
            SELECT 
                v.placa, v.ano, v.preco, v.status,
                mo.id AS modelo_id, mo.nome AS modelo_nome,
                ma.id AS marca_id, ma.nome AS marca_nome
            FROM veiculo v
            JOIN modelo mo ON v.id_modelo = mo.id
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

                Veiculo v = new Veiculo(
                        rs.getString("placa"),
                        rs.getInt("ano"),
                        rs.getDouble("preco"),
                        rs.getString("status"),
                        modelo
                );

                lista.add(v);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar veículos: " + e.getMessage(), e);
        }

        return lista;
    }

    // -----------------------------
    // BUSCAR POR PLACA
    // -----------------------------
    public Veiculo buscarPorPlaca(String placa) {

        String sql = """
            SELECT 
                v.placa, v.ano, v.preco, v.status,
                mo.id AS modelo_id, mo.nome AS modelo_nome,
                ma.id AS marca_id, ma.nome AS marca_nome
            FROM veiculo v
            JOIN modelo mo ON v.id_modelo = mo.id
            JOIN marca ma ON mo.id_marca = ma.id
            WHERE v.placa = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, placa);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {

                    Marca marca = new Marca(
                            rs.getInt("marca_id"),
                            rs.getString("marca_nome")
                    );

                    Modelo modelo = new Modelo(
                            rs.getInt("modelo_id"),
                            rs.getString("modelo_nome"),
                            marca
                    );

                    return new Veiculo(
                            rs.getString("placa"),
                            rs.getInt("ano"),
                            rs.getDouble("preco"),
                            rs.getString("status"),
                            modelo
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar veículo por placa: " + e.getMessage(), e);
        }

        return null;
    }

    // -----------------------------
    // REMOVER
    // -----------------------------
    public boolean remover(String placa) {

        String sql = "DELETE FROM veiculo WHERE placa = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, placa);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover veículo: " + e.getMessage(), e);
        }
    }

    // -----------------------------
    // ATUALIZAR
    // -----------------------------
    public void atualizar(Veiculo v) {

        String sql = """
            UPDATE veiculo 
            SET ano = ?, preco = ?, status = ?, id_modelo = ?
            WHERE placa = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, v.getAno());
            st.setDouble(2, v.getPreco());
            st.setString(3, v.getStatus());
            st.setInt(4, v.getModelo().getId());
            st.setString(5, v.getPlaca());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar veículo: " + e.getMessage(), e);
        }
    }
}

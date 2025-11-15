package br.com.sistema.automotivo.repository;

import br.com.sistema.automotivo.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RelatorioRepositorySQL {

    // TOTAL DE VEÍCULOS
    public int contarVeiculos() {
        String sql = "SELECT COUNT(*) AS total FROM veiculo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (Exception e) {
            System.err.println("Erro ao contar veículos: " + e.getMessage());
        }

        return 0;
    }

    // TOTAL POR MARCA
    public void contarPorMarca() {
        String sql = """
            SELECT ma.nome AS marca, COUNT(*) AS total
            FROM veiculo v
            JOIN modelo mo ON v.id_modelo = mo.id
            JOIN marca ma ON mo.id_marca = ma.id
            GROUP BY ma.nome
            ORDER BY total DESC
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getString("marca") + ": " + rs.getInt("total"));
            }

        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório por marca: " + e.getMessage());
        }
    }

    // VALOR TOTAL EM ESTOQUE
    public double valorTotal() {
        String sql = "SELECT SUM(preco) AS total FROM veiculo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            if (rs.next()) return rs.getDouble("total");

        } catch (Exception e) {
            System.err.println("Erro ao calcular valor total: " + e.getMessage());
        }

        return 0.0;
    }

    // STATUS (DISPONÍVEL / VENDIDO)
    public long contarPorStatus(String status) {
        String sql = "SELECT COUNT(*) AS total FROM veiculo WHERE LOWER(status) = LOWER(?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, status);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getLong("total");
            }

        } catch (Exception e) {
            System.err.println("Erro ao contar por status: " + e.getMessage());
        }

        return 0;
    }
}

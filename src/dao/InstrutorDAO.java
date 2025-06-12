package dao;

import instrutor.Instrutor; // Importa a classe Instrutor
import util.DBUtil; // Importa a classe DBUtil

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstrutorDAO {

    public void addInstrutor(Instrutor instrutor) {
        String SQL = "INSERT INTO instrutores (cpf, nome, especialidade, email, telefone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); // Obtém a conexão do DBUtil
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, instrutor.getCpf()); // Define o CPF
            pstmt.setString(2, instrutor.getNome()); // Define o Nome
            pstmt.setString(3, instrutor.getEspecialidade()); // Define a Especialidade
            pstmt.setString(4, instrutor.getEmail()); // Define o Email
            pstmt.setString(5, instrutor.getTelefone()); // Define o Telefone
            pstmt.executeUpdate(); // Executa a inserção
            System.out.println("Instrutor adicionado com sucesso ao DB.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao adicionar instrutor: " + e.getMessage());
        }
    }

    public List<Instrutor> getAllInstrutores() {
        List<Instrutor> instrutores = new ArrayList<>();
        String SQL = "SELECT * FROM instrutores";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) { // Executa a consulta
            while (rs.next()) { // Itera sobre os resultados
                Instrutor instrutor = new Instrutor(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("especialidade"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
                instrutores.add(instrutor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao listar instrutores: " + e.getMessage());
        }
        return instrutores;
    }

    public Instrutor getInstrutorByCpf(String cpf) {
        String SQL = "SELECT * FROM instrutores WHERE cpf = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, cpf);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // Se encontrar um instrutor
                    return new Instrutor(
                            rs.getString("cpf"),
                            rs.getString("nome"),
                            rs.getString("especialidade"),
                            rs.getString("email"),
                            rs.getString("telefone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar instrutor por CPF: " + e.getMessage());
        }
        return null; // Retorna null se não encontrar
    }

    public void updateInstrutor(Instrutor instrutor) {
        String SQL = "UPDATE instrutores SET nome = ?, especialidade = ?, email = ?, telefone = ? WHERE cpf = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, instrutor.getNome());
            pstmt.setString(2, instrutor.getEspecialidade());
            pstmt.setString(3, instrutor.getEmail());
            pstmt.setString(4, instrutor.getTelefone());
            pstmt.setString(5, instrutor.getCpf());
            pstmt.executeUpdate(); // Executa a atualização
            System.out.println("Instrutor atualizado com sucesso no DB.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao atualizar instrutor: " + e.getMessage());
        }
    }

    public void deleteInstrutor(String cpf) {
        String SQL = "DELETE FROM instrutores WHERE cpf = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, cpf);
            pstmt.executeUpdate(); // Executa a exclusão
            System.out.println("Instrutor excluído com sucesso do DB.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao excluir instrutor: " + e.getMessage());
        }
    }
}
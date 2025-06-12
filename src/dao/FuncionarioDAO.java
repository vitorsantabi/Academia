package dao;

import funcionario.Funcionario; // Importa a classe Funcionario
import util.DBUtil; // Importa a classe DBUtil

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    // Adicionar um novo funcionário
    public void addFuncionario(Funcionario funcionario) {
        String SQL = "INSERT INTO funcionarios (cpf, nome, funcao, email, telefone, dataAdmissao) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, funcionario.getCpf());
            pstmt.setString(2, funcionario.getNome());
            pstmt.setString(3, funcionario.getFuncao());
            pstmt.setString(4, funcionario.getEmail());
            pstmt.setString(5, funcionario.getTelefone());
            pstmt.setString(6, funcionario.getDataAdmissao());
            pstmt.executeUpdate();
            System.out.println("Funcionário adicionado com sucesso ao DB.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao adicionar funcionário: " + e.getMessage());
        }
    }

    // Obter todos os funcionários
    public List<Funcionario> getAllFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String SQL = "SELECT cpf, nome, funcao, email, telefone, dataAdmissao FROM funcionarios";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("funcao"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("dataAdmissao")
                );
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }
        return funcionarios;
    }

    // Obter funcionário por CPF
    public Funcionario getFuncionarioByCpf(String cpf) {
        String SQL = "SELECT cpf, nome, funcao, email, telefone, dataAdmissao FROM funcionarios WHERE cpf = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, cpf);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Funcionario(
                            rs.getString("cpf"),
                            rs.getString("nome"),
                            rs.getString("funcao"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("dataAdmissao")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar funcionário por CPF: " + e.getMessage());
        }
        return null;
    }

    // Atualizar um funcionário existente
    public void updateFuncionario(Funcionario funcionario) {
        String SQL = "UPDATE funcionarios SET nome = ?, funcao = ?, email = ?, telefone = ? WHERE cpf = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, funcionario.getNome());
            pstmt.setString(2, funcionario.getFuncao());
            pstmt.setString(3, funcionario.getEmail());
            pstmt.setString(4, funcionario.getTelefone());
            pstmt.setString(5, funcionario.getCpf()); // CPF no WHERE
            pstmt.executeUpdate();
            System.out.println("Funcionário atualizado com sucesso no DB.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
        }
    }

    // Deletar um funcionário
    public void deleteFuncionario(String cpf) {
        String SQL = "DELETE FROM funcionarios WHERE cpf = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, cpf);
            pstmt.executeUpdate();
            System.out.println("Funcionário excluído com sucesso do DB.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao excluir funcionário: " + e.getMessage());
        }
    }
}
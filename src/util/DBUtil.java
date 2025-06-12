package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {

                System.out.println("Desculpe, não foi possível encontrar o arquivo db.properties. Certifique-se de que ele está na pasta 'resources' do seu projeto.");
            }
            properties.load(input);
        } catch (Exception ex) {
            // Imprime o stack trace para depuração de erros na leitura do arquivo de propriedades
            ex.printStackTrace();
            System.err.println("Erro ao carregar o arquivo db.properties: " + ex.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        // Carrega o driver JDBC para SQL Server
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            // Imprime o stack trace e lança uma SQLException se o driver não for encontrado
            e.printStackTrace();
            throw new SQLException("Erro: Driver JDBC do SQL Server não encontrado. Certifique-se de que o JAR do driver está no classpath.");
        }

        // Tenta estabelecer a conexão com o banco de dados usando as propriedades
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public static void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o statement: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o result set: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
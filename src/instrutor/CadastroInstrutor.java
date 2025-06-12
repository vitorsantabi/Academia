package instrutor;

import dao.InstrutorDAO; // Importa o DAO que você criou
import java.util.List;
import java.util.Scanner;

public class CadastroInstrutor {
    private InstrutorDAO instrutorDAO = new InstrutorDAO(); // Instancia o DAO
    private Scanner scanner; // Removido o 'new Scanner(System.in)' pois já é passado pelo Main

    // O construtor pode ser simplificado, ou o scanner passado para o método execute
    public CadastroInstrutor() {
        // O scanner será passado pelo método execute
    }

    public void execute(Scanner scanner) { // Recebe o scanner do Main
        this.scanner = scanner; // Atribui o scanner recebido
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> criarInstrutor();
                case 2 -> listarInstrutores();
                case 3 -> atualizarInstrutor();
                case 4 -> removerInstrutor();
                case 5 -> System.out.println("Saindo do sistema de Instrutores...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 5);
    }

    private void exibirMenu() {
        System.out.println("=== SISTEMA DE CADASTRO DE INSTRUTORES ===");
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Cadastrar Instrutor");
        System.out.println("2. Listar Instrutores");
        System.out.println("3. Atualizar Instrutor");
        System.out.println("4. Remover Instrutor");
        System.out.println("5. Sair");
    }

    private void criarInstrutor() {
        System.out.println("\n=== NOVO CADASTRO ===");
        String cpf = lerDado("CPF: ", false);

        if (instrutorDAO.getInstrutorByCpf(cpf) != null) { // Usa o DAO para verificar se o CPF já existe
            System.out.println("Erro: CPF já cadastrado!");
            return;
        }

        String nome = lerDado("Nome: ", false);
        String especialidade = lerDado("Especialidade: ", false);
        String email = lerDado("Email: ", true);
        String telefone = lerDado("Telefone: ", false);

        try {
            Instrutor novo = new Instrutor(cpf, nome, especialidade, email, telefone);
            instrutorDAO.addInstrutor(novo); // Usa o DAO para adicionar o instrutor ao banco
            System.out.println("\nInstrutor cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarInstrutores() {
        System.out.println("\n=== LISTA DE INSTRUTORES ===");
        List<Instrutor> instrutores = instrutorDAO.getAllInstrutores(); // Usa o DAO para obter todos os instrutores
        if (instrutores.isEmpty()) {
            System.out.println("Nenhum instrutor cadastrado.");
            return;
        }

        for (int i = 0; i < instrutores.size(); i++) {
            System.out.printf("%d. %s%n", i+1, instrutores.get(i));
        }
    }

    private void atualizarInstrutor() {
        System.out.println("\n=== ATUALIZAR CADASTRO ===");
        String cpf = lerDado("Digite o CPF do instrutor: ", false);
        Instrutor instrutor = instrutorDAO.getInstrutorByCpf(cpf); // Usa o DAO para encontrar o instrutor

        if (instrutor == null) {
            System.out.println("Instrutor não encontrado!");
            return;
        }

        System.out.println("Dados atuais:");
        System.out.println(instrutor);

        System.out.println("\nNovos dados (deixe em branco para manter o atual):");
        String nome = lerDadoOpcional("Nome [" + instrutor.getNome() + "]: ");
        String especialidade = lerDadoOpcional("Especialidade [" + instrutor.getEspecialidade() + "]: ");
        String email = lerDadoOpcional("Email [" + instrutor.getEmail() + "]: ");
        String telefone = lerDadoOpcional("Telefone [" + instrutor.getTelefone() + "]: ");

        try {
            if (!nome.isEmpty()) instrutor.setNome(nome);
            if (!especialidade.isEmpty()) instrutor.setEspecialidade(especialidade);
            if (!email.isEmpty()) instrutor.setEmail(email);
            if (!telefone.isEmpty()) instrutor.setTelefone(telefone);
            instrutorDAO.updateInstrutor(instrutor); // Usa o DAO para atualizar o instrutor no banco
            System.out.println("Dados atualizados com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na atualização: " + e.getMessage());
        }
    }

    private void removerInstrutor() {
        System.out.println("\n=== REMOVER INSTRUTOR ===");
        String cpf = lerDado("Digite o CPF do instrutor: ", false);
        Instrutor instrutor = instrutorDAO.getInstrutorByCpf(cpf); // Usa o DAO para encontrar o instrutor

        if (instrutor == null) {
            System.out.println("Instrutor não encontrado!");
            return;
        }

        System.out.println("Dados do instrutor:");
        System.out.println(instrutor);

        String confirmacao = lerDado("Tem certeza que deseja remover? (S/N): ", false);
        if (confirmacao.equalsIgnoreCase("S")) {
            instrutorDAO.deleteInstrutor(instrutor.getCpf()); // Usa o DAO para excluir o instrutor do banco
            System.out.println("Instrutor removido com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    // Os métodos lerDado, lerDadoOpcional e lerInteiro permanecem os mesmos
    private String lerDado(String mensagem, boolean isEmail) {
        while (true) {
            System.out.print(mensagem);
            String dado = scanner.nextLine().trim();

            if (dado.isEmpty()) {
                System.out.println("Este campo é obrigatório!");
                continue;
            }

            if (isEmail && !dado.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
                System.out.println("Formato de email inválido! Ex: nome@dominio.com");
                continue;
            }

            return dado;
        }
    }

    private String lerDadoOpcional(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private int lerInteiro() {
        while (true) {
            try {
                System.out.print("Escolha uma opção: ");
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) { // Mudança para InputMismatchException para nextInt()
                System.out.println("Por favor, digite um número válido!");
                scanner.nextLine(); // Consome a entrada inválida
            }
        }
    }
}
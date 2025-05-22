package edu.felipebuso;

import edu.felipebuso.model.Event;
import edu.felipebuso.model.User;
import edu.felipebuso.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EventService eventService = new EventService();
    private static User currentUser = null;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        System.out.println(
            "=== Bem-vindo ao Sistema de Eventos da sua Cidade ===");
        cadastroUsuario();

        boolean running = true;
        while (running) {
            exibirMenu();
            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrarEvento();
                case 2 -> listarEventos();
                case 3 -> participarEvento();
                case 4 -> cancelarParticipacao();
                case 5 -> listarEventosConfirmados();
                case 6 -> {
                    System.out.println("Encerrando o sistema. Até logo!");
                    running = false;
                }
                default ->
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void cadastroUsuario() {
        System.out.println("Para começar, cadastre seu usuário.");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        currentUser = new User(nome, email, cidade);
        System.out.println("\nUsuário cadastrado com sucesso!\n");
    }

    private static void exibirMenu() {
        System.out.println("\nEscolha uma opção:");
        System.out.println("1 - Cadastrar evento");
        System.out.println("2 - Listar eventos");
        System.out.println("3 - Participar de evento");
        System.out.println("4 - Cancelar participação");
        System.out.println("5 - Listar eventos confirmados");
        System.out.println("6 - Sair");
        System.out.print("Opção: ");
    }

    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número: ");
            }
        }
    }

    private static void cadastrarEvento() {
        System.out.println("\n=== Cadastro de Evento ===");
        System.out.print("Nome do evento: ");
        String nome = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        System.out.print("Categoria (Festa, Esportivo, Show, Outro): ");
        String categoria = scanner.nextLine();

        System.out.print("Data e hora (dd/MM/yyyy HH:mm): ");
        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(scanner.nextLine(), formatter);
        } catch (Exception e) {
            System.out.println("Data/hora inválida. Operação cancelada.");
            return;
        }

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        eventService.createEvent(
            nome,
            endereco,
            categoria,
            dataHora,
            descricao
        );
        System.out.println("Evento cadastrado com sucesso!");
    }

    private static void listarEventos() {
        System.out.println("\n=== Eventos da Cidade ===");
        List<Event> eventos = eventService.listAllEventsSorted();

        LocalDateTime agora = LocalDateTime.now();
        for (int i = 0; i < eventos.size(); i++) {
            Event e = eventos.get(i);
            System.out.println("[" + i + "] " + e.getName() + " - " + e.getCategory() + " - " + e.getDateTime()
                .format(formatter));

            if (e.getDateTime().isBefore(agora)) {
                System.out.println("  > Evento já ocorreu");
            } else if (e.getDateTime().isAfter(agora)) {
                System.out.println("  > Evento futuro");
            } else {
                System.out.println("  > Evento ocorrendo agora");
            }
        }
    }

    private static void participarEvento() {
        System.out.println("\n=== Participar de Evento ===");
        listarEventos();

        System.out.print("Digite o número do evento para participar: ");
        int idx = lerInteiro();

        if (idx < 0 || idx >= eventService.getEvents().size()) {
            System.out.println("Evento inválido.");
            return;
        }

        eventService.confirmParticipation(currentUser, idx);
        System.out.println("Sua participação foi confirmada!");
    }

    private static void cancelarParticipacao() {
        System.out.println("\n=== Cancelar Participação ===");
        List<Event> userEvents = eventService.listUserEvents(currentUser);

        if (userEvents.isEmpty()) {
            System.out.println("Você não está participando de nenhum evento.");
            return;
        }

        for (int i = 0; i < userEvents.size(); i++) {
            Event e = userEvents.get(i);
            System.out.println("[" + i + "] " + e.getName() + " - " + e.getDateTime()
                .format(formatter));
        }

        System.out.print(
            "Digite o número do evento para cancelar participação: ");
        int idx = lerInteiro();

        if (idx < 0 || idx >= userEvents.size()) {
            System.out.println("Evento inválido.");
            return;
        }

        Event eventoCancelar = userEvents.get(idx);
        int realIndex = eventService.getEvents().indexOf(eventoCancelar);

        eventService.cancelParticipation(currentUser, realIndex);
        System.out.println("Participação cancelada.");
    }

    private static void listarEventosConfirmados() {
        System.out.println("\n=== Seus Eventos Confirmados ===");
        List<Event> userEvents = eventService.listUserEvents(currentUser);

        if (userEvents.isEmpty()) {
            System.out.println("Você não confirmou presença em nenhum evento.");
            return;
        }

        for (Event e : userEvents) {
            System.out.println(e.getName() + " - " + e.getDateTime()
                .format(formatter));
        }
    }
}


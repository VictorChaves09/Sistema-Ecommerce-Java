import Atores.Administrador;
import Atores.Cliente;
import Carrinho.ItemCarrinho;
import Facade.AdministradorEcommerceFacade;
import Facade.ClienteEcommerceFacade;
import Pedido.Pedido;
import Produto.ComboProdutoComposite;
import Produto.ProdutoComponent;
import Produto.ProdutoLeaf;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ClienteEcommerceFacade clienteFacade = new ClienteEcommerceFacade();
    private static AdministradorEcommerceFacade administradorFacade = new AdministradorEcommerceFacade();
    private static Cliente cliente = null;
    private static Administrador administrador = null;

    public static void main(String[] args){
        System.out.println("====== BEM-VINDO(A) AO SISTEMA DE E-COMMERCE ======");

        boolean ecommerceRodando = true;
        while (ecommerceRodando) {
            System.out.println("\nEscolha: ");
            System.out.println("  1. Menu do Cliente.");
            System.out.println("  2. Menu do Administrador.");
            System.out.println("  0. Sair.");
            int opçao = Integer.parseInt(scanner.nextLine());

            switch (opçao) {
                case 1:
                    menuCliente();
                    break;
                case 2:
                    menuAdministrador();
                    break;
                case 0:
                    ecommerceRodando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }


    public static void menuCliente() {
        System.out.println("\nEscolha: ");
        System.out.println("  1. Criar conta.");
        System.out.println("  2. Fazer login.");
        int opcao = Integer.parseInt(scanner.nextLine());

        switch (opcao) {
            case 1:
                System.out.println("\nDigite seu nome: ");
                String nomeCadastro = scanner.nextLine();
                System.out.println("Digite seu email: ");
                String emailCadastro = scanner.nextLine();
                System.out.println("Digite sua senha: ");
                String senhaCadastro = scanner.nextLine();
                clienteFacade.criarConta(nomeCadastro, emailCadastro, senhaCadastro);
                break;
            case 2:
                System.out.println("\nDigite seu email: ");
                String emailLogin = scanner.nextLine();
                System.out.println("Digite sua senha: ");
                String senhaLogin = scanner.nextLine();
                cliente = clienteFacade.login(emailLogin, senhaLogin);
                if (cliente != null) {
                    menuClienteLogado();
                }
                break;
            default:
                System.out.println("Opção Inválida.");
                break;
        }
    }

    public static void menuClienteLogado() {
        boolean clienteLogado = true;
        ProdutoComponent produto = null;
        Long id;
        while (clienteLogado) {
            System.out.println("\nOlá, " + cliente.getNome() + ". O que deseja fazer?");
            System.out.println("  1. Visualizar produtos.");
            System.out.println("  2. Visualizar carrinho.");
            System.out.println("  3. Adicionar ao carrinho.");
            System.out.println("  4. Remover do carrinho.");
            System.out.println("  5. Finalizar compra.");
            System.out.println("  6. Visualizar pedidos.");
            System.out.println("  7. Acompanhar produto.");
            System.out.println("  8. Checar notificações.");
            System.out.println("  9. Limpar notificações.");
            System.out.println("  0. Logout.");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    List<ProdutoComponent> produtosLoja = clienteFacade.visualizarProdutos();
                    produtosLoja.forEach(produtoLoja -> System.out.println(produtoLoja.getId() + " - " + produtoLoja.getNome() + ": R$ " + produtoLoja.getPreco() + "\nEstoque disponível: " + produtoLoja.getEstoque()));
                    break;
                case 2:
                    if (cliente.getCarrinho().getItens().isEmpty()) {
                        System.out.println("Carrinho vazio.");
                        break;
                    }
                    List<ItemCarrinho> itens = cliente.getCarrinho().getItens();
                    int indice = 0;
                    for (ItemCarrinho item : itens) {
                        System.out.printf(indice + ": %s | Qtd: %d | Preço Un: R$ %.2f | Subtotal: R$ %.2f%n\n", item.getProduto().getNome(), item.getQuantidade(), item.getProduto().getPreco(), item.calcularSubtotal());
                        indice++;
                    }
                    break;
                case 3:
                    System.out.println("Digite o ID do produto: ");
                    id = Long.parseLong(scanner.nextLine());
                    System.out.println("Digite a quantidade desejada: ");
                    int quantidade = Integer.parseInt(scanner.nextLine());
                    produto = clienteFacade.getProduto(id);
                    if (produto != null) {
                        cliente.adicionarProdutoCarrinho(produto, quantidade);
                    }
                    break;
                case 4:
                    if (cliente.getCarrinho().getItens().isEmpty()) {
                        System.out.println("Carrinho vazio.");
                        break;
                    }
                    System.out.println("Digite o índice do produto que deseja remover: ");
                    indice = Integer.parseInt(scanner.nextLine());
                    cliente.removerProdutoCarrinho(indice);
                    break;
                case 5:
                    System.out.println("Escolha o método de pagamento:");
                    System.out.println("  1. PIX.");
                    System.out.println("  2. Cartão.");
                    System.out.println("  3. Boleto.");
                    int metodo = Integer.parseInt(scanner.nextLine());
                    switch (metodo) {
                        case 1:
                            clienteFacade.finalizarCompra(cliente, "PIX");
                            break;
                        case 2:
                            clienteFacade.finalizarCompra(cliente, "Cartão");
                            break;
                        case 3:
                            clienteFacade.finalizarCompra(cliente, "Boleto");
                            break;
                        default:
                            System.out.println("Tipo de pagamento inválido.");
                            break;
                    }
                    break;
                case 6:
                    List<Pedido> pedidos = cliente.getPedidos();
                    pedidos.forEach(pedido -> System.out.printf(pedido.getId() + ": " + pedido.getItens() + "Total: %.2f\n", pedido.getTotal()));
                    break;
                case 7:
                    System.out.println("Digite o ID do produto que deseja acompanhar:");
                    id = Long.parseLong(scanner.nextLine());
                    clienteFacade.acompanharProduto(cliente, id);
                    break;
                case 8:
                    cliente.visualizarNotificacoes();
                    break;
                case 9:
                    cliente.limparNotificacoes();
                    break;
                case 0:
                    clienteFacade.atualizarCliente(cliente);
                    clienteLogado = false;
                    cliente = null;
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    public static void menuAdministrador() {
        System.out.println("\nDigite seu email: ");
        String emailLogin = scanner.nextLine();
        System.out.println("Digite sua senha: ");
        String senhaLogin = scanner.nextLine();
        administrador = administradorFacade.login(emailLogin, senhaLogin);
        if (administrador != null) {
            menuAdministradorLogado();
        }
    }

    public static void menuAdministradorLogado() {
        boolean administradorLogado = true;
        Long id;
        while (administradorLogado) {
            System.out.println("\nOlá, " + administrador.getNome() + ". O que deseja fazer?");
            System.out.println("  1. Visualizar produtos.");
            System.out.println("  2. Adicionar produto.");
            System.out.println("  3. Adicionar combo de produtos.");
            System.out.println("  4. Remover produto.");
            System.out.println("  5. Atualizar estoque.");
            System.out.println("  6. Visualizar pedidos.");
            System.out.println("  7. Gerar relatório de vendas.");
            System.out.println("  0. Logout.");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao){
                case 1:
                    List<ProdutoComponent> produtosLoja = administradorFacade.listarProdutos();
                    produtosLoja.forEach(produtoLoja -> System.out.println(produtoLoja.getId() + " - " + produtoLoja.getNome() + ": R$ " + produtoLoja.getPreco() + "\nEstoque disponível: " + produtoLoja.getEstoque()));
                    break;
                case 2:
                    System.out.println("Digite o nome do produto:");
                    String novoProdutoNome = scanner.nextLine();
                    System.out.println("Digite o preço do produto:");
                    double novoProdutoPreco = Double.parseDouble(scanner.nextLine());
                    System.out.println("Digite o estoque disponível:");
                    int novoProdutoEstoque = Integer.parseInt(scanner.nextLine());
                    ProdutoLeaf novoProduto = new ProdutoLeaf(novoProdutoNome, novoProdutoPreco, novoProdutoEstoque);
                    administradorFacade.adicionarProduto(novoProduto);
                    break;
                case 3:
                    System.out.println("Digite o nome do combo: ");
                    String novoComboNome = scanner.nextLine();
                    ComboProdutoComposite novoCombo = new ComboProdutoComposite(novoComboNome);
                    boolean adicionarProdutos = true;
                    while (adicionarProdutos) {
                        System.out.println("Escolha:");
                        System.out.println("  1. Adicionar novo produto ao combo.");
                        System.out.println("  2. Adicionar novo combo a loja.");
                        int escolha = Integer.parseInt(scanner.nextLine());
                        if (escolha == 1) {
                            System.out.println("Digite o ID do produto que deseja adicionar:");
                            id = Long.parseLong(scanner.nextLine());
                            ProdutoComponent produto = administradorFacade.getProduto(id);
                            novoCombo.adicionarProduto(produto);
                        } else if (escolha == 2) {
                            adicionarProdutos = false;
                        } else {
                            System.out.println("Opção inválida.");
                        }
                    }
                    administradorFacade.adicionarProduto(novoCombo);
                    break;
                case 4:
                    System.out.println("Digite o ID do produto que deseja remover:");
                    id = Long.parseLong(scanner.nextLine());
                    administradorFacade.removerProduto(id);
                    break;
                case 5:
                    System.out.println("Digite o ID do produto que deseja renovar estoque:");
                    id = Long.parseLong(scanner.nextLine());
                    System.out.println("Digite o novo estoque:");
                    int novoEstoque = Integer.parseInt(scanner.nextLine());
                    administradorFacade.atualizarEstoque(id, novoEstoque);
                    break;
                case 6:
                    List<Pedido> pedidos = administradorFacade.visualizarPedidos();
                    pedidos.forEach(pedido -> System.out.printf(pedido.getId() + ": " + pedido.getItens() + "Total: %.2f\n", pedido.getTotal()));
                    break;
                case 7:
                   administradorFacade.gerarRelatorio();
                   break;
                case 0:
                    administradorLogado = false;
                    administrador = null;
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }
}

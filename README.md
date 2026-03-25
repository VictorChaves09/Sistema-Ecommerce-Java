
# Sistema de E-Commerce

Sistema de e-commerce desenvolvido em Java, utilizando JPA/Hibernate para persistência em MariaDB. O projeto implementa padrões de projeto clássicos e uma arquitetura orientada a objetos para gerenciar clientes, produtos (simples e combos), carrinhos e pedidos.

## Atores
- **Cliente:** Usuário que utiliza a plataforma para realizar compras. É capaz de: criar conta, realizar login, visualizar produtos, adicionar produtos ao carrinho, remover produtos do carrinho, finalizar pedido e se cadastrar para acompanhar disponibilidade do produto.
- **Administrador:** Responsável por gerenciar o funcionamento da loja virtual. É capaz de: cadastrar produtos, remover produtos, atualizar estoque, visualizar pedidos e gerar relatórios de vendas.

## Padrões de projeto utilizados
- **Singleton:** Garantir que exista apenas uma instância de conexão com o banco de dados durante toda a execução do sistema. A aplicação precisa acessar o banco de dados frequentemente. Criar várias conexões pode gerar desperdício de recursos e inconsistência. O padrão garante uma única instância compartilhada.
- **Factory:** Criar objetos de pagamento sem que o sistema precise conhecer a implementação concreta. O sistema pode oferecer diferentes formas de pagamento (PIX, cartão, boleto). O padrão permite criar esses objetos dinamicamente.
- **Facade:** Fornecer uma interface simplificada para operações complexas do sistema. Diversas operações do sistema envolvem várias etapas. O padrão encapsula essas etapas em uma única interface.
- **Strategy:** Permitir que o sistema utilize diferentes algoritmos de pagamento de forma intercambiável. Cada tipo de pagamento possui uma lógica diferente. O padrão permite encapsular essas estratégias.
- **Observer:** Permitir que usuários sejam notificados quando um produto voltar ao estoque. Clientes podem demonstrar interesse em produtos indisponíveis. Quando o estoque é atualizado, o sistema deve notificar automaticamente os interessados. O padrão permite essa comunicação desacoplada.
- **Composite:** Permitir que o sistema trate itens individuais e conjuntos de itens (combos de produtos) de forma uniforme. Dessa forma, tanto produtos simples quanto agrupamentos de produtos implementam a mesma interface, possibilitando que o carrinho manipule todos os elementos de forma transparente e simplificando o cálculo do valor total da compra.

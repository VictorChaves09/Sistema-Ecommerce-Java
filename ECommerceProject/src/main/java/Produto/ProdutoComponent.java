package Produto;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ProdutoComponent implements ProdutoSubject{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public abstract String getNome();
    public abstract double getPreco();
    public abstract int getEstoque();
    public abstract void baixarEstoque(int quantidade);
}

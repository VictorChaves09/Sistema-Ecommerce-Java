package Atores;

import jakarta.persistence.*;

@MappedSuperclass
abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    private String senha;

    public Usuario() {}

    public Usuario(String nome, String email, String senha){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public Long getId() {
        return id;
    }
}

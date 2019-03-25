package com.example.intenttest;

public class Usuario {
    private String nome;
    private String email;
    private String telefone;

    Usuario(String n, String e, String t){
        this.nome = n;
        this.email = e;
        this.telefone = t;
    }

    private void setNome(String n){
        this.nome = n;
    }

    private void setEmail(String e){
        this.email = e;
    }

    private void setTelefone(String t){
        this.telefone = t;
    }

    public String getNome(){
        return this.nome;
    }

    public String getEmail(){
        return this.email;
    }

    public String getTelefone(){
        return this.telefone;
    }

}

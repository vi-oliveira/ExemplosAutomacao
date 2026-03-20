package main.filtro;

import java.time.LocalDate;

public class Fatura {
    private String codigo;
    private double valor;
    private LocalDate data;
    private Cliente cliente;

    public Fatura(String codigo, double valor, LocalDate data, Cliente cliente) {
        this.codigo = codigo;
        this.valor = valor;
        this.data = data;
        this.cliente = cliente;
    }

    // Getters
    public String getCodigo() { return codigo; }
    public double getValor() { return valor; }
    public LocalDate getData() { return data; }
    public Cliente getCliente() { return cliente; }
}

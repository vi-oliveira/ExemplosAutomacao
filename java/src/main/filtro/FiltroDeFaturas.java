package main.filtro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiltroDeFaturas {

    public List<Fatura> filtrar(List<Fatura> faturas) {
        List<Fatura> faturasFiltradas = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        
        // Datas de corte para as regras
        LocalDate umMesAtras = hoje.minusMonths(1);
        LocalDate doisMesesAtras = hoje.minusMonths(2);
        
        List<String> estadosSudeste = Arrays.asList("SP", "RJ", "MG", "ES");

        for (Fatura fatura : faturas) {
            double valor = fatura.getValor();
            LocalDate dataFatura = fatura.getData();
            LocalDate dataInclusaoCliente = fatura.getCliente().getDataInclusao();
            String estadoCliente = fatura.getCliente().getEstado();

            // Regra 1: Valor menor que 2000
            if (valor < 2000) {
                continue; // Pula esta fatura, removendo-a do resultado
            }

            // Regra 2: Valor entre 2000 e 2500 e data <= 1 mês atrás
            if (valor >= 2000 && valor <= 2500 && (dataFatura.isBefore(umMesAtras) || dataFatura.isEqual(umMesAtras))) {
                continue;
            }

            // Regra 3: Valor entre 2500 e 3000 e inclusão do cliente <= 2 meses atrás
            if (valor > 2500 && valor <= 3000 && (dataInclusaoCliente.isBefore(doisMesesAtras) || dataInclusaoCliente.isEqual(doisMesesAtras))) {
                continue;
            }

            // Regra 4: Valor maior que 4000 e estado no Sudeste
            if (valor > 4000 && estadosSudeste.contains(estadoCliente.toUpperCase())) {
                continue;
            }

            // Se não caiu em nenhuma regra de exclusão, a fatura é adicionada à lista final
            faturasFiltradas.add(fatura);
        }

        return faturasFiltradas;
    }
}
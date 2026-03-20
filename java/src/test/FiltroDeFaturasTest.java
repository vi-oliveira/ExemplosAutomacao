/*prompt utilizado:

Atue como um Desenvolvedor Java Sênior Especialista em Qualidade de Software.
Tenho uma classe FiltroDeFaturas que possui um método filtrar(List<Fatura> faturas). Esse método exclui faturas de uma lista baseando-se em 4 regras:

    Valor < 2000.

    Valor >= 2000 e <= 2500, com a data da fatura sendo <= 1 mês atrás.
class FiltroDeFaturasTeste
    Valor > 2500 e <= 3000, com a data de inclusão do cliente sendo <= 2 meses atrás.

    Valor > 4000 e cliente pertence ao Sudeste (SP, RJ, MG, ES).

Usando JUnit Jupiter, crie uma classe de testes chamada FiltroDeFaturasTest. Crie métodos de teste separados que exercitem cenários de sucesso e de falha para CADA UMA das 4 regras acima. Use nomes de métodos descritivos (padrão should...when...) e estruture os testes usando o padrão Arrange-Act-Assert (Given-When-Then). Mock as datas usando LocalDate.now() como referência ou crie um construtor que permita injetar um relógio (Clock) para facilitar os testes no tempo.*/

package test;

import org.junit.jupiter.api.Test;

import main.filtro.Cliente;
import main.filtro.Fatura;
import main.filtro.FiltroDeFaturas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FiltroDeFaturasTest {

    // Instância do filtro que será usada em todos os testes
    private final FiltroDeFaturas filtro = new FiltroDeFaturas();
    
    // Data base para criar os testes no tempo corretamente
    private final LocalDate hoje = LocalDate.now();

    // =====================================================================
    // REGRA 1: Valor menor que 2000
    // =====================================================================

    @Test
    void deveRemoverFaturaQuandoValorForMenorQue2000() {
        // Arrange
        Cliente cliente = new Cliente("Ana", hoje, "PR");
        Fatura fatura = new Fatura("F001", 1999.0, hoje, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertTrue(resultado.isEmpty(), "Faturas com valor < 2000 devem ser removidas.");
    }

    @Test
    void deveManterFaturaQuandoValorFor2000OuMaior() {
        // Arrange
        Cliente cliente = new Cliente("Ana", hoje, "PR");
        Fatura fatura = new Fatura("F002", 2000.0, hoje, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertEquals(1, resultado.size(), "Faturas com valor >= 2000 não devem ser removidas por esta regra.");
    }

    // =====================================================================
    // REGRA 2: Valor entre 2000 e 2500 E data <= 1 mês atrás
    // =====================================================================

    @Test
    void deveRemoverFaturaQuandoValorAte2500EDataTem1MesOuMais() {
        // Arrange
        Cliente cliente = new Cliente("Bruno", hoje, "SC");
        LocalDate dataUmMesAtras = hoje.minusMonths(1);
        Fatura fatura = new Fatura("F003", 2200.0, dataUmMesAtras, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertTrue(resultado.isEmpty(), "Faturas entre 2000 e 2500 com data <= 1 mês atrás devem ser removidas.");
    }

    @Test
    void deveManterFaturaQuandoValorAte2500MasDataForRecente() {
        // Arrange
        Cliente cliente = new Cliente("Bruno", hoje, "SC");
        LocalDate dataRecente = hoje.minusDays(15); // Menos de 1 mês
        Fatura fatura = new Fatura("F004", 2200.0, dataRecente, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertEquals(1, resultado.size(), "Faturas entre 2000 e 2500 recentes devem ser mantidas.");
    }

    // =====================================================================
    // REGRA 3: Valor entre 2500 e 3000 E inclusão do cliente <= 2 meses atrás
    // =====================================================================

    @Test
    void deveRemoverFaturaQuandoValorAte3000EClienteInclusoHa2MesesOuMais() {
        // Arrange
        LocalDate inclusaoAntiga = hoje.minusMonths(2);
        Cliente cliente = new Cliente("Carlos", inclusaoAntiga, "RS");
        Fatura fatura = new Fatura("F005", 2800.0, hoje, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertTrue(resultado.isEmpty(), "Faturas entre 2500 e 3000 de clientes antigos devem ser removidas.");
    }

    @Test
    void deveManterFaturaQuandoValorAte3000MasClienteForNovo() {
        // Arrange
        LocalDate inclusaoNova = hoje.minusMonths(1); // Menos de 2 meses
        Cliente cliente = new Cliente("Carlos", inclusaoNova, "RS");
        Fatura fatura = new Fatura("F006", 2800.0, hoje, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertEquals(1, resultado.size(), "Faturas entre 2500 e 3000 de clientes novos devem ser mantidas.");
    }

    // =====================================================================
    // REGRA 4: Valor maior que 4000 E estado no Sudeste (SP, RJ, MG, ES)
    // =====================================================================

    @Test
    void deveRemoverFaturaQuandoValorMaiorQue4000EClienteDoSudeste() {
        // Arrange
        Cliente cliente = new Cliente("Daniela", hoje, "SP"); // Estado do sudeste
        Fatura fatura = new Fatura("F007", 4500.0, hoje, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertTrue(resultado.isEmpty(), "Faturas > 4000 do Sudeste devem ser removidas.");
    }

    @Test
    void deveManterFaturaQuandoValorMaiorQue4000MasClienteForDeOutraRegiao() {
        // Arrange
        Cliente cliente = new Cliente("Daniela", hoje, "BA"); // Fora do sudeste
        Fatura fatura = new Fatura("F008", 4500.0, hoje, cliente);

        // Act
        List<Fatura> resultado = filtro.filtrar(Arrays.asList(fatura));

        // Assert
        assertEquals(1, resultado.size(), "Faturas > 4000 fora do Sudeste devem ser mantidas.");
    }
}
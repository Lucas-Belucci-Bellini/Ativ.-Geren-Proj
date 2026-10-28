import java.util.List;

/** Testes de regressão executáveis com `java -ea` ou por chamada direta de main. */
public class SearchServiceTest {

    public static void main(String[] args) {
        CatalogService catalog = new CatalogService();
        SearchService search = new SearchService(catalog);

        check(catalog.getAllTracks().size() == 8, "catálogo inicial contém 8 faixas");
        check(search.search("bohemian").stream().anyMatch(t -> t.id().equals("t-001")), "busca parcial por título");
        check(search.search("QUEEN").stream().anyMatch(t -> t.id().equals("t-001")), "busca por artista ignora caixa");
        check(search.search("  queen  ").stream().anyMatch(t -> t.id().equals("t-001")), "busca remove espaços externos");
        check(search.search("eEn").stream().anyMatch(t -> t.id().equals("t-001")), "busca parcial case-insensitive");
        check(search.search("imagine").stream().anyMatch(t -> t.id().equals("t-002")), "busca por título completo");
        check(search.search("artista inexistente").isEmpty(), "busca sem correspondência retorna lista vazia");
        check(search.search("").isEmpty(), "termo vazio retorna lista vazia");
        check(search.search("   ").isEmpty(), "termo com espaços retorna lista vazia");
        check(search.search(null).isEmpty(), "termo nulo retorna lista vazia");
        check(search.search("   BOB DYLAN ").stream().anyMatch(t -> t.id().equals("t-008")), "normalização usa Locale.ROOT e trim");

        boolean nullCatalogRejected = false;
        try {
            new SearchService(null);
        } catch (NullPointerException expected) {
            nullCatalogRejected = true;
        }
        check(nullCatalogRejected, "serviço exige catálogo não nulo");
        System.out.println("Resultado: " + passed + "/" + total + " testes passaram.");
        if (passed != total) {
            throw new AssertionError("Existem testes com falha.");
        }
    }

    private static int total;
    private static int passed;

    private static void check(boolean condition, String description) {
        total++;
        if (!condition) {
            System.err.println("FALHOU: " + description);
            return;
        }
        passed++;
        System.out.println("PASSOU: " + description);
    }
}

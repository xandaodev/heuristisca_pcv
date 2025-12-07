
import java.util.ArrayList;
import java.util.List;

public class HeuristicaInsercao {

    public static void main(String[] args) {
        // 1. Configuração
        String arquivo = "LAU15_dist.txt"; // Ou "SGB128_dist.txt"
        
        // Reutilizamos a sua classe Pcv para ler o arquivo (Genial, né?)
        Pcv pcv = new Pcv(0);
        pcv.lerArquivo(arquivo);
        
        int numCidades = pcv.getNumCidades();
        System.out.println("Resolvendo TSP com Heurística de Inserção Mais Próxima");
        System.out.println("Instância: " + arquivo + " (" + numCidades + " cidades)");

        long tempoInicio = System.currentTimeMillis();

        // 2. Execução do Algoritmo
        List<Integer> rota = resolverInsercaoMaisProxima(pcv);

        long tempoFim = System.currentTimeMillis();

        // 3. Cálculo do Custo Final
        double distanciaTotal = calcularCustoRota(rota, pcv);

        // 4. Resultados
        System.out.println("-----------------------------------------");
        System.out.println("Tempo de Execução: " + (tempoFim - tempoInicio) + "ms");
        System.out.println("Distância Final: " + distanciaTotal);
        System.out.println("Rota Encontrada: " + rota.toString());
        System.out.println("-----------------------------------------");
    }

    // Lógica da Heurística de Inserção
    public static List<Integer> resolverInsercaoMaisProxima(Pcv pcv) {
        int n = pcv.getNumCidades();
        List<Integer> rota = new ArrayList<>();
        boolean[] visitados = new boolean[n];

        // Passo 1: Começa com a cidade 0 (sub-rota inicial)
        rota.add(0);
        visitados[0] = true;

        // Enquanto não visitar todas as cidades
        while (rota.size() < n) {
            
            // --- FASE A: SELEÇÃO (Quem entra?) ---
            // Escolhe a cidade não visitada (k) mais próxima de QUALQUER cidade da rota atual
            int cidadeSelecionadaK = -1;
            double menorDistanciaParaRota = Double.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!visitados[i]) { // Para cada cidade fora da rota
                    // Calcula a distância dela para a cidade mais próxima DENTRO da rota
                    double distMinParaI = Double.MAX_VALUE;
                    for (int cidadeNaRota : rota) {
                        double d = pcv.getDistancia(i, cidadeNaRota);
                        if (d < distMinParaI) {
                            distMinParaI = d;
                        }
                    }
                    
                    // Se essa cidade for a mais próxima globalmente da rota, seleciona ela
                    if (distMinParaI < menorDistanciaParaRota) {
                        menorDistanciaParaRota = distMinParaI;
                        cidadeSelecionadaK = i;
                    }
                }
            }

            // --- FASE B: INSERÇÃO (Onde entra?) ---
            // Acha a aresta (i, j) na rota onde inserir K custa menos
            // Custo de inserção = d(i, k) + d(k, j) - d(i, j)
            int melhorPosicaoInsercao = -1;
            double menorAumentoCusto = Double.MAX_VALUE;

            for (int pos = 0; pos < rota.size(); pos++) {
                int cidadeI = rota.get(pos);
                // Pega a próxima cidade (se for a última, a próxima é a 0 - ciclo)
                int cidadeJ = rota.get((pos + 1) % rota.size());

                double custoAtual = pcv.getDistancia(cidadeI, cidadeJ);
                double novoCusto = pcv.getDistancia(cidadeI, cidadeSelecionadaK) 
                                 + pcv.getDistancia(cidadeSelecionadaK, cidadeJ);
                
                double aumento = novoCusto - custoAtual;

                if (aumento < menorAumentoCusto) {
                    menorAumentoCusto = aumento;
                    melhorPosicaoInsercao = pos + 1; // Insere DEPOIS de I
                }
            }

            // Executa a inserção
            rota.add(melhorPosicaoInsercao, cidadeSelecionadaK);
            visitados[cidadeSelecionadaK] = true;
        }

        return rota;
    }

    // Método auxiliar para calcular custo total (igual ao da Formiga)
    public static double calcularCustoRota(List<Integer> rota, Pcv pcv) {
        double dist = 0;
        for (int i = 0; i < rota.size() - 1; i++) {
            dist += pcv.getDistancia(rota.get(i), rota.get(i + 1));
        }
        // Fecha o ciclo (última -> primeira)
        dist += pcv.getDistancia(rota.get(rota.size() - 1), rota.get(0));
        return dist;
    }
}
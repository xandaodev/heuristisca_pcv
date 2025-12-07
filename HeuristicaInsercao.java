import java.util.ArrayList;
import java.util.List;

public class HeuristicaInsercao{
    public static void main(String[] args){
        //utilizei a base do trabalho e a classe Pcv para fazer essa atividade, facilitando assim tambem a comparaçao de resultados

        String arquivo = "SGB128_dist.txt"; 
        //String arquivo = "LAU15_dist.txt"; 
        Pcv pcv = new Pcv(0);
        pcv.lerArquivo(arquivo);
        
        int numCidades = pcv.getNumCidades();
        System.out.println("Heurística para o PCV - Resolvendo com Inserção Mais Próxima");
        System.out.println("Instância: " + arquivo + " (" + numCidades + " cidades)");

        // executa o algoritmo para encontrar a rota
        List<Integer> rota = resolverInsercaoMaisProxima(pcv);

        // calcula o custo total da rota encontrada
        double distanciaTotal = calcularCustoRota(rota, pcv);

        System.out.println("-----------------------------------------");
        System.out.println("Distancia Final: " + distanciaTotal);
        System.out.println("Rota Encontrada: " + rota.toString());
        System.out.println("-----------------------------------------");
    }

    public static List<Integer> resolverInsercaoMaisProxima(Pcv pcv){
        int n = pcv.getNumCidades();
        List<Integer> rota = new ArrayList<>();
        boolean[] visitados = new boolean[n];

        // comeca pela cidade zero
        rota.add(0);
        visitados[0] = true;

        // repete ate visitar todas as cidades
        while(rota.size() <n){
            int cidadeSelecionadaK  = -1;
            double menorDistanciaParaRota = Double.MAX_VALUE;

            // procura a cidade nao visitada mais proxima da rota atual
            for(int i = 0; i < n; i++){
                if(!visitados[i]){ 
                    double distMinParaI = Double.MAX_VALUE;
                    for(int cidadeNaRota : rota){
                        double d = pcv.getDistancia(i, cidadeNaRota);
                        if(d < distMinParaI){
                            distMinParaI = d;
                        }
                    }
                    
                    // atualiza se encontrou uma cidade mais proxima
                    if (distMinParaI < menorDistanciaParaRota){
                        menorDistanciaParaRota = distMinParaI;
                        cidadeSelecionadaK = i;
                    }
                }
            }
            // encontra a melhor posicao para inserir a cidade escolhida
            int melhorPosicaoInsercao = -1;
            double menorAumentoCusto = Double.MAX_VALUE;

            for(int pos =0;pos< rota.size();pos++){
                int cidadeI = rota.get(pos);
                // pega a proxima cidade considerando o ciclo
                int cidadeJ = rota.get((pos + 1) % rota.size());

                double custoAtual= pcv.getDistancia(cidadeI, cidadeJ);
                // calcula o custo se inserir a nova cidade entre as duas atuais
                double novoCusto =pcv.getDistancia(cidadeI, cidadeSelecionadaK)+ pcv.getDistancia(cidadeSelecionadaK, cidadeJ);
                double aumento =novoCusto - custoAtual;
                // verifica se essa psicao gera o menor aumento de custo
                if(aumento < menorAumentoCusto){
                    menorAumentoCusto = aumento;
                    melhorPosicaoInsercao = pos + 1; 
                }
            }
            rota.add(melhorPosicaoInsercao, cidadeSelecionadaK);
            visitados[cidadeSelecionadaK] = true;
        }
        return rota;
    }
    // calcula a distancia total percorrida na rota
    public static double calcularCustoRota(List<Integer> rota, Pcv pcv){
        double dist = 0;
        for(int i = 0; i < rota.size() - 1; i++){
            dist += pcv.getDistancia(rota.get(i), rota.get(i + 1));
        }
        // soma a distancia de retorno para a cidade inicial
        dist +=pcv.getDistancia(rota.get(rota.size()- 1), rota.get(0));
        return dist;
    }
}
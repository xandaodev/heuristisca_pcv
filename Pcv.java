import java.io.*;
import java.util.*;

public class Pcv {
    protected int numCidades;
    protected double [][] matrizDistancias;

    public Pcv(int numCidades){
        this.numCidades = numCidades;
    }

    public void lerArquivo(String caminhoArquivo){
        //descobre o número de cidades
        int linhas = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
            while (br.readLine() != null) {
                linhas++;
            }
            br.close();
        } catch (IOException e){
            System.out.println("Erro ao contar linhas do arquivo.");
            return;
        }
        double[][] matriz_temp = new double[linhas][linhas];
        //preenche a matriz
        try{
            Scanner sc = new Scanner(new File(caminhoArquivo));

            for(int i = 0; i<linhas; i++){
                for(int j = 0; j<linhas; j++){
                    matriz_temp[i][j] = sc.nextDouble();
                }
            }
            sc.close();
        }catch(FileNotFoundException e){
            System.out.println("Erro ao abrir o arquivo !");
        }
        setMatrizDistancias(matriz_temp);
    }

    //getters e setters
    public void setMatrizDistancias(double[][] matriz){
        this.matrizDistancias = matriz;
        this.numCidades = matriz.length;
    }

    public double getDistancia(int cidadeA, int cidadeB){
        return matrizDistancias[cidadeA][cidadeB];
    }

    public int getNumCidades(){
        return numCidades;
    }
    
}

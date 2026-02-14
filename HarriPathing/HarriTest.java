package org.firstinspires.ftc.teamcode.HarriPathing;


public class HarriTest {
    public static void main(String[] args) {
        // Criar uma arena padrão (144 polegadas) com grade 10x10
        HarriGrid grid = new HarriGrid(144, 10);

        System.out.println("Tamanho do quadrado: " + grid.getTileSize() + " polegadas");

        // TESTE 1: O robô está no centro (0,0). Qual quadrado é esse?
        // Numa grade 10x10 (100 quadrados), o centro deve ser perto do 55 ou 56.
        int idCentro = grid.getTileIdAt(0, 0);
        System.out.println("O robô em (0,0) está no Quadrado: " + idCentro);

        // TESTE 2: Quero ir pro Quadrado 1 (Canto inferior esquerdo)
        // Deve retornar algo como X = -65, Y = -65
        HarriGrid.Point alvo = grid.getCenterOfTile(1);
        System.out.println("Alvo Quadrado 1: X=" + alvo.x + ", Y=" + alvo.y);
    }
}

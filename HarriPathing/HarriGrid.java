package org.firstinspires.ftc.teamcode.HarriPathing;

public class HarriGrid {

    // Configurações da Arena
    private double fieldWidth;  // Ex: 144 polegadas
    private double fieldHeight; // Ex: 144 polegadas
    private int rows;           // Quantas linhas de quadrados?
    private int cols;           // Quantas colunas?

    // Cálculos internos
    private double tileWidth;
    private double tileHeight;

    /**
     * Construtor da Grade
     * @param fieldSize Tamanho da arena (Ex: 144 para FTC padrão)
     * @param gridResolution Quantos quadrados por lado (Ex: 10 para 10x10)
     */
    public HarriGrid(double fieldSize, int gridResolution) {
        this.fieldWidth = fieldSize;
        this.fieldHeight = fieldSize;
        this.rows = gridResolution;
        this.cols = gridResolution;

        this.tileWidth = fieldWidth / cols;
        this.tileHeight = fieldHeight / rows;
    }

    /**
     * CONVERSÃO 1: Onde estou?
     * Recebe coordenadas X/Y da Odometria e retorna o ID do quadrado (1 a 100)
     */
    public int getTileIdAt(double x, double y) {
        // 1. Normalizar coordenadas (Mudar o (0,0) do centro para o canto inferior esquerdo)
        double shiftedX = x + (fieldWidth / 2.0);
        double shiftedY = y + (fieldHeight / 2.0);

        // 2. Proteger contra sair da arena (Clamping)
        if (shiftedX < 0) shiftedX = 0;
        if (shiftedX >= fieldWidth) shiftedX = fieldWidth - 0.1;
        if (shiftedY < 0) shiftedY = 0;
        if (shiftedY >= fieldHeight) shiftedY = fieldHeight - 0.1;

        // 3. Calcular coluna e linha
        int col = (int) (shiftedX / tileWidth);
        int row = (int) (shiftedY / tileHeight);

        // 4. Converter para ID único (Ex: Linha 2, Coluna 3 em uma grade 10x10 = ID 23)
        // Fórmula: (Linha * LarguraGrade) + Coluna + 1 (pra começar do 1)
        return (row * cols) + col + 1;
    }

    /**
     * CONVERSÃO 2: Para onde vou?
     * Recebe o ID do quadrado (ex: 50) e retorna o ponto X/Y do CENTRO dele.
     */
    public Point getCenterOfTile(int tileId) {
        // Validar ID
        if (tileId < 1 || tileId > (rows * cols)) {
            return new Point(0, 0); // Retorna centro se ID for inválido
        }

        // Ajustar para índice 0
        int adjustedId = tileId - 1;

        // Recuperar Linha e Coluna
        int row = adjustedId / cols;
        int col = adjustedId % cols;

        // Calcular coordenadas normalizadas (Canto inferior esquerdo é 0,0)
        double centerX_shifted = (col * tileWidth) + (tileWidth / 2.0);
        double centerY_shifted = (row * tileHeight) + (tileHeight / 2.0);

        // Desnormalizar (Voltar o (0,0) para o centro da arena)
        double finalX = centerX_shifted - (fieldWidth / 2.0);
        double finalY = centerY_shifted - (fieldHeight / 2.0);

        return new Point(finalX, finalY);
    }

    // Classe auxiliar simples para guardar X e Y
    public static class Point {
        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    // Getter para debug
    public double getTileSize() {
        return tileWidth;
    }
}

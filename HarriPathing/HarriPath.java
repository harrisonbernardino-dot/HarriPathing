package org.firstinspires.ftc.teamcode.HarriPathing;

import java.util.ArrayList;
import java.util.List;

public class HarriPath {

    // Dependências
    private HarriGrid grid;

    // Lista de passos (IDs dos quadrados)
    private ArrayList<Integer> waypoints;

    // Estado atual
    private int currentStepIndex = 0;
    private boolean isFinished = false;

    // Configurações
    private double arrivalTolerance = 2.0; // O robô precisa chegar a 2 polegadas do centro para considerar "cheguei"

    /**
     * Construtor
     * @param grid A referência da arena (HarriGrid) para ele saber fazer as contas
     */
    public HarriPath(HarriGrid grid) {
        this.grid = grid;
        this.waypoints = new ArrayList<>();
    }

    /**
     * Adiciona um ponto ao caminho.
     * Retorna o próprio objeto para permitir encadeamento (Fluent Interface)
     * Ex: path.add(1).add(5).add(10);
     */
    public HarriPath add(int tileId) {
        waypoints.add(tileId);
        return this; // Permite: path.add(1).add(2);
    }

    /**
     * O CÉREBRO DO CAMINHO
     * Deve ser chamado a cada ciclo do loop.
     * * @param currentX Posição X atual do robô (Odometria)
     * @param currentY Posição Y atual do robô (Odometria)
     * @return O Ponto (X, Y) para onde o robô deve ir AGORA.
     */
    public HarriGrid.Point update(double currentX, double currentY) {
        // 1. Se acabou o caminho, retorna o último ponto para manter a posição (Brake)
        if (currentStepIndex >= waypoints.size()) {
            isFinished = true;
            if (waypoints.isEmpty()) return new HarriGrid.Point(currentX, currentY); // Caminho vazio, fica onde está
            return grid.getCenterOfTile(waypoints.get(waypoints.size() - 1));
        }

        // 2. Descobrir qual o alvo atual
        int targetTileId = waypoints.get(currentStepIndex);
        HarriGrid.Point targetPoint = grid.getCenterOfTile(targetTileId);

        // 3. Calcular distância até o alvo (Pitágoras)
        double dist = Math.hypot(targetPoint.x - currentX, targetPoint.y - currentY);

        // 4. Verificar se chegamos
        // Dica Pro: Se for o último ponto, exigimos precisão maior. Se for ponto intermediário, tolerância maior pra ser fluido.
        double effectiveTolerance = (currentStepIndex == waypoints.size() - 1) ? 1.0 : arrivalTolerance;

        if (dist < effectiveTolerance) {
            // Chegamos! Passa para o próximo.
            currentStepIndex++;

            // Verifica se o caminho acabou logo após incrementar
            if (currentStepIndex >= waypoints.size()) {
                isFinished = true;
                return targetPoint; // Retorna o último ponto válido
            }

            // Se ainda tem caminho, pega o novo alvo imediatamente
            int newTargetId = waypoints.get(currentStepIndex);
            return grid.getCenterOfTile(newTargetId);
        }

        // 5. Se não chegou, continua retornando o alvo atual
        return targetPoint;
    }

    // Métodos úteis para o Telemetry da FTC
    public boolean isFinished() {
        return isFinished;
    }

    public int getCurrentTargetID() {
        if (currentStepIndex >= waypoints.size()) return -1;
        return waypoints.get(currentStepIndex);
    }

    public void setTolerance(double inches) {
        this.arrivalTolerance = inches;
    }
}
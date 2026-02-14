package org.firstinspires.ftc.teamcode.HarriPathing;

import com.qualcomm.robotcore.util.Range;

public class HarriTankDrive {

    // ==========================================
    // AJUSTES DE PID
    // ==========================================
    public double P_DRIVE = 0.05;
    public double P_TURN  = 0.03;
    public double HEADING_THRESHOLD = 15.0; // Graus de erro para forçar giro estático

    // Classe auxiliar para resposta
    public static class TankOutput {
        public double leftPower;
        public double rightPower;
        public String status; // Para telemetria ("GIRANDO" ou "ANDANDO")
    }

    /**
     * LÓGICA TANK DRIVE
     * Decide entre Turn-in-Place e Arcade Drive
     */
    public TankOutput calculate(double currentX, double currentY, double currentHeading,
                                double targetX, double targetY) {

        TankOutput output = new TankOutput();

        // 1. Vetor para o Alvo
        double dx = targetX - currentX;
        double dy = targetY - currentY;
        double dist = Math.hypot(dx, dy);

        // 2. Calcular Ângulo Necessário
        double targetAngle = Math.atan2(dy, dx);
        double errorRad = angleWrap(targetAngle - currentHeading);
        double errorDeg = Math.toDegrees(errorRad);

        // 3. Máquina de Estados (Decisão)
        if (Math.abs(errorDeg) > HEADING_THRESHOLD) {
            // --- MODO GIRO (Turn in Place) ---
            double turn = errorDeg * P_TURN;

            // Feedforward (Força mínima pra vencer atrito do chão)
            if (Math.abs(turn) < 0.18) turn = 0.18 * Math.signum(turn);

            output.leftPower  = -turn;
            output.rightPower = turn;
            output.status = "GIRANDO (Erro: " + String.format("%.1f", errorDeg) + "°)";
        } else {
            // --- MODO NAVEGAÇÃO (Arcade Drive) ---
            double drive = Range.clip(dist * P_DRIVE, -0.8, 0.8);
            double turn  = errorDeg * P_TURN;

            output.leftPower  = drive - turn;
            output.rightPower = drive + turn;
            output.status = "ANDANDO";
        }

        // Clip final de segurança (-1 a 1)
        output.leftPower = Range.clip(output.leftPower, -1.0, 1.0);
        output.rightPower = Range.clip(output.rightPower, -1.0, 1.0);

        return output;
    }

    private double angleWrap(double angle) {
        while (angle > Math.PI)  angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}
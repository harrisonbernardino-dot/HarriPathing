package org.firstinspires.ftc.teamcode.HarriPathing;

public class HarriMecanumDrive {

    // ==========================================
    // AJUSTES DE PID (Calibre aqui ou no OpMode)
    // ==========================================
    public double P_DRIVE = 0.06; // Força por polegada
    public double P_TURN  = 0.02; // Força por grau

    // Classe auxiliar para devolver as potências
    public static class MotorPowers {
        public double fl, fr, bl, br;
    }

    /**
     * CALCULA A FORÇA DOS MOTORES (Field Centric)
     * Não acessa hardware. Apenas matemática.
     * * @param currentX      Posição X atual (Odometria)
     * @param currentY      Posição Y atual (Odometria)
     * @param currentHeading Ângulo atual em RADIANOS (Odometria)
     * @param targetX       Destino X
     * @param targetY       Destino Y
     * @param targetHeading Ângulo final desejado (ex: 0 para manter frente)
     * @return Objeto com as forças dos 4 motores
     */
    public MotorPowers calculate(double currentX, double currentY, double currentHeading,
                                 double targetX, double targetY, double targetHeading) {

        // 1. Calcular Erros Globais
        double errorX = targetX - currentX;
        double errorY = targetY - currentY;
        double errorTurn = angleWrap(Math.toRadians(targetHeading) - currentHeading);

        // 2. Rotacionar para Referencial do Robô (Field Centric Math)
        // Isso resolve o problema de saber qual lado é frente independente da rotação
        double cos = Math.cos(-currentHeading);
        double sin = Math.sin(-currentHeading);

        // Rotação vetorial 2D
        double driveX = errorX * cos - errorY * sin;
        double driveY = errorX * sin + errorY * cos;

        // 3. Aplicar PID (Proporcional)
        // DICA: Se o robô andar de lado quando deveria ir pra frente,
        // troque driveX por driveY aqui embaixo.
        double pForward = driveX * P_DRIVE;
        double pStrafe  = driveY * P_DRIVE * 1.1; // 1.1 é correção de atrito lateral
        double pTurn    = Math.toDegrees(errorTurn) * P_TURN;

        // 4. Calcular Força de Cada Roda (Cinemática Mecanum)
        double denominator = Math.max(Math.abs(pForward) + Math.abs(pStrafe) + Math.abs(pTurn), 1);

        MotorPowers powers = new MotorPowers();
        powers.fl = (pForward - pStrafe - pTurn) / denominator; // Tente inverter sinais aqui se necessário
        powers.bl = (pForward + pStrafe - pTurn) / denominator;
        powers.fr = (pForward + pStrafe + pTurn) / denominator;
        powers.br = (pForward - pStrafe + pTurn) / denominator;

        return powers;
    }

    // Matemática auxiliar
    private double angleWrap(double angle) {
        while (angle > Math.PI)  angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}
# HarriPathing
HarriPathing é uma solução de navegação "Pure Logic" para FTC, criada para simplificar o período autônomo na temporada DECODE. Desenvolvida pela equipe CLUSTERS 16053 (Sesi Senai Parangaba), a biblioteca substitui coordenadas complexas por um sistema de Grade (Grid) intuitivo, permitindo criar rotas precisas para chassis Mecanum e Tank em poucos minutos.

# 📍 HarriPathing Library

> **Uma biblioteca de navegação "Pure Logic" para FIRST Tech Challenge (FTC).**

![Java](https://img.shields.io/badge/Language-Java-orange)
![FTC](https://img.shields.io/badge/Platform-FTC_SDK-blue)
![Season](https://img.shields.io/badge/Season-DECODE_2025%2F2026-purple)

---

## 👨‍💻 Sobre o Projeto

Esta biblioteca foi desenvolvida por **Harrison Matheus Felix Bernardino**.

* **Equipe:** [CLUSTERS #16053](https://instagram.com/sesiclusters)
* **Escola:** Sesi Senai Parangaba (Fortaleza/CE)
* **Temporada:** DECODE (2025/2026)

O objetivo do **HarriPathing** é simplificar a programação autônoma, permitindo que equipes iniciantes e intermediárias utilizem conceitos avançados de **Path Following** (Seguimento de Caminho) através de um sistema de coordenadas intuitivo baseado em grade (Grid), sem a complexidade matemática vetorial direta.

---

## ✨ Funcionalidades Principais

1.  **Sistema de Grid (Grade):** Esqueça coordenadas complexas (ex: `x: 12.4, y: -40.2`). A arena é dividida em quadrados (ex: 1 a 100). Você manda o robô para o **"Quadrado 55"** e a biblioteca calcula o resto.
2.  **Lógica Pura (Hardware Agnostic):** A biblioteca não acessa o hardware (`DcMotor`) diretamente. Ela apenas recebe "Onde estou" e retorna "Força dos Motores". Isso evita conflitos de hardware e facilita testes.
3.  **Suporte Híbrido:** Algoritmos dedicados tanto para **Mecanum Drive** (Holonômico) quanto para **Tank Drive** (Diferencial).
4.  **Tank Inteligente:** O algoritmo de Tank decide automaticamente se o robô deve fazer uma curva suave (Arcade) ou girar no próprio eixo (Point Turn) dependendo do erro angular.

---

## 🚀 Instalação

1.  Baixe a pasta `HarriPathing` deste repositório.
2.  Copie a pasta inteira para dentro do diretório `teamcode` do seu projeto FTC no Android Studio.
    * Caminho: `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/HarriPathing`
3.  Certifique-se de que você possui uma classe de **Odometria** funcionando (que forneça X, Y e Heading do robô).

---

## 🛠️ Como Usar

### 1. Configuração Inicial (Setup)

No seu `LinearOpMode`, instancie a Grade (Grid) e o Caminho (Path).

```java
// Define uma arena de 144 polegadas com resolução 10x10 (100 quadrados)
HarriGrid grid = new HarriGrid(144, 10);
HarriPath path = new HarriPath(grid);

// Adiciona os waypoints (Quadrados por onde o robô vai passar)
path.add(10).add(55).add(82);

````
2. Exemplo: Mecanum Drive
Para robôs com rodas Mecanum (que andam de lado).

```java

HarriMecanumDrive calculator = new HarriMecanumDrive();

waitForStart();

while (opModeIsActive()) {
    // 1. Atualize sua Odometria (Isso vem da sua classe de hardware)
    odometria.update(); 
    
    // 2. Descubra qual o próximo ponto alvo (X, Y)
    HarriGrid.Point target = path.update(odometria.getX(), odometria.getY());

    if (!path.isFinished()) {
        // 3. Calcule as forças (Matemática pura, sem hardware aqui)
        var powers = calculator.calculate(
            odometria.getX(),       // Onde estou X
            odometria.getY(),       // Onde estou Y
            odometria.getHeading(), // Meu ângulo (Radianos)
            target.x,               // Para onde vou X
            target.y,               // Para onde vou Y
            0                       // Ângulo final desejado (0 = frente)
        );

        // 4. Aplique nos motores
        robot.fl.setPower(powers.fl);
        robot.fr.setPower(powers.fr);
        robot.bl.setPower(powers.bl);
        robot.br.setPower(powers.br);
    } else {
        robot.stop();
    }
}
````
3. Exemplo: Tank Drive
Para robôs de tração (2, 4 ou 6 rodas padrão).

```java

HarriTankDrive calculator = new HarriTankDrive();

// Dentro do loop:
var out = calculator.calculate(
    odometria.getX(), 
    odometria.getY(), 
    odometria.getHeading(), 
    target.x, 
    target.y
);

robot.left.setPower(out.left);
robot.right.setPower(out.right);

// Debug: Mostra se ele decidiu girar ou andar
telemetry.addData("Estado", out.status);

````
📐 HarriVisualizer
Este projeto inclui um arquivo visualizer.html. Abra-o no seu navegador para:

Ver a grade da arena.

Clicar nos quadrados para desenhar sua rota.

Gerar o código Java automaticamente com os IDs corretos.

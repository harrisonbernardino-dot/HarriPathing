# HarriPathing

HarriPathing é uma solução de navegação avançada para FTC, criada para simplificar o período autônomo na temporada **DECODE**. Desenvolvida pela equipe **CLUSTERS 16053** (Sesi Senai Parangaba), a biblioteca substitui coordenadas complexas por um sistema de Grade (Grid) inteligente, permitindo criar rotas com desvio de obstáculos e curvas suaves em poucos minutos.

> **Uma biblioteca de navegação "Pure Logic" para FIRST Tech Challenge (FTC).**

![Java](https://img.shields.io/badge/Language-Java-orange)
![FTC](https://img.shields.io/badge/Platform-FTC_SDK-blue)
![Season](https://img.shields.io/badge/Season-DECODE_2025%2F2026-purple)
![Version](https://img.shields.io/badge/Version-3.0_Pathfinder-green)

---

## 👨‍💻 Sobre o Projeto

Esta biblioteca foi desenvolvida por **Harrison Matheus Felix Bernardino**.

* **Equipe:** [CLUSTERS #16053](https://instagram.com/clusters_ftc)
* **Escola:** Sesi Senai Parangaba (Fortaleza/CE)
* **Temporada:** DECODE (2025/2026)

O objetivo do **HarriPathing** é democratizar a programação autônoma. Ele permite que equipes utilizem conceitos avançados como **Pathfinding A*** (Busca de Caminho) e **Curvas Geométricas** sem precisar lidar com a complexidade da matemática vetorial pura.

---

## ✨ Funcionalidades Principais (V3.0)

1.  **📍 Sistema de Grid (Grade):** A arena é mapeada em IDs simples (ex: 1 a 144). Você comanda: *"Vá para o quadrado 55"* e a biblioteca resolve a posição real.
2.  **🚧 Desvio de Obstáculos (Pathfinding):** Defina quais quadrados estão bloqueados (paredes, robôs aliados, elementos de jogo). O algoritmo **A*** calculará automaticamente a rota mais curta desviando dos bloqueios.
3.  **↪️ Gerador de Curvas:** Crie movimentos circulares perfeitos. Defina o raio, ângulo inicial e final, e o HarriPath gera os *waypoints* para uma curva suave.
4.  **🖥️ HarriVisualizer:** Uma ferramenta HTML/JS inclusa que permite desenhar sua rota clicando na tela e **gera o código Java automaticamente**.
5.  **🤖 Suporte Híbrido:** Algoritmos dedicados tanto para **Mecanum Drive** (Holonômico) quanto para **Tank Drive** (Diferencial Inteligente).

---

## 🚀 Instalação

1.  Baixe a pasta `HarriPathing` deste repositório.
2.  Copie a pasta inteira para dentro do diretório `teamcode` do seu projeto FTC no Android Studio.
    * Caminho: `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/HarriPathing`
3.  Certifique-se de que você possui uma classe de **Odometria** funcionando (que forneça X, Y e Heading do robô).

---

## 🛠️ Como Usar

### 1. Configuração Inicial e Obstáculos

No seu `LinearOpMode`, instancie a Grade e defina o que deve ser evitado.

```java
// 1. Cria o Grid (Arena de 144 polegadas, resolução 12x12)
HarriGrid grid = new HarriGrid(144, 12);
HarriPath path = new HarriPath(grid);

// 2. (Opcional) Adiciona Obstáculos - O robô nunca passará por aqui
grid.addObstacle(45);
grid.addObstacle(46);
grid.addObstacle(58); // Ex: Uma barreira no meio do campo

2. Criando Rotas (3 Métodos)
Você pode misturar comandos manuais, curvas e busca automática.

````
## 2. Criando Rotas (3 Métodos)
Você pode misturar comandos manuais, curvas e busca automática.

```java
// A. Adicionar pontos manualmente
path.add(10); 
path.add(22);

// B. Adicionar uma Curva (Centro X, Y, Raio, Ang Inicial, Ang Final, Passo)
// Faz uma curva de 90 graus ao redor do centro (0,0) com raio de 24 polegadas
path.addCurve(0, 0, 24, 0, 90, 5);

// C. Pathfinding Automático (A*)
// "Encontre o caminho do quadrado atual até o 140, desviando dos obstáculos"
path.makePath(path.getLastID(), 140);
````

## 3. Executando no Loop (TeleOp ou Autônomo)
```java

// No seu loop while(opModeIsActive()):

// 1. Pegue sua posição da Odometria (ex: Pinpoint, RoadRunner odo, etc)
double x = odo.getX();
double y = odo.getY();
double h = odo.getHeading();

// 2. Atualize o Path
HarriGrid.Point target = path.update(x, y);

if (!path.isFinished()) {
    // 3. Calcule a força (Mecanum ou Tank)
    // Exemplo Mecanum:
    var powers = mecanumDrive.calculate(x, y, h, target.x, target.y, 0);
    robot.setPowers(powers);
} else {
    robot.stop();
}
````

# 🎨 HarriVisualizer
O projeto inclui o arquivo visualizer.html.

Abra o arquivo visualizer.html em qualquer navegador (Chrome, Edge).

Desenhe sua missão:

Clique com botão direito para definir o START.

Use a ferramenta Obstáculo para bloquear áreas.

Use a ferramenta Curva para criar arcos perfeitos.

Use Pathfind para ligar pontos automaticamente.

Copie o código Java gerado na barra lateral e cole no seu Android Studio.

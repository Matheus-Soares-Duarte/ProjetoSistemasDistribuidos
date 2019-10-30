# Projeto Sistemas Distribuidos
<i>Neste projeto da disciplina de Sistemas Distribuidos será entregue uma implementação do jogo chamado 21. Segue no texto abaixo a explicação do objetivo do projeto e o planejamento do seu desenvolvimento.</i>

## O Jogo Vinte e Um:
Emplementarei um jogo de 21 simples, sem apostas, onde o jogo terá entre 2 e 5 jogadores e será feito com uso de apenas um baralho (52 cartas, sem coringas), nesse jogo o naipe das cartas não interferem no jogo.<br><br>
O objetivo do jogo é completar o valor de 21 pontos, sendo que quem o fizer será o vencedor da rodada, em caso de empate todos que completem 21 pontos na rodada serão considerados vencedores e receberam o ponto de rodada.<br><br>
No inicio da partida o Dealer (que nessa aplicação será o servidor e não participará da partida como jogador) entregará duas cartas de forma aleatória para cada jogador (maquina cliente que conectou na sala), sendo a ordem de entrega a ordem de chegada na sala.

## Ações:
Cada jogador terá duas ações possíveis em sua vez, elas são:<br>
* <b>Comprar:</b> onde o Dealer entrega mais uma carta para o jogador, essa ação é possível até que ele passe a vez ou o mesmo estoure, ou seja, valor passe de 21 pontos e nesse caso a vez é passada automaticamente.<br>
* <b>Passar:</b> essa ação implica em dar seguência no jogo, possibilitando ação aos próximos jogadores, seguindo a sequência de entrada na sala, sendo que quando não haja próximo jogador a rodada é encerrada.<br>

Além disso ele terá uma ação possível a todo momento na partida, sendo ela:<br>
* <b>Sair do Jogo:</b> ao seleciona lá o jogador sai tanto da rodada em que está, quanto da sala e do próprio aplicativo.<br>

## Valores das cartas:
   * Os Ás podem valer 1 ou 11 pontos;<br>
   * Cartas com números de 2 a 10 equivalem a seus números em pontos;<br>
   * As cartas figuradas (rei, rainha e valete, respectivamente K, Q e J) possuem valor 10.<br>

## Componentes:
A pretenção é realizar o desenvolvimento deste jogo em forma de aplicativo na linguagem de programação java. E para seu desenvolvimento serão necessários:<br>
   * 1 servidor, que será o Dealer fixo da partida, ou seja, responsável pela distribuição aleatória das cartas, contagem dos pontos e julgamento do(s) vencedor(es) da rodada.<br>
   * 2 a 5 clientes, que atuarão como jogadores, ou seja, terão as ações de compra e passe e poderão ganhar a rodada e ganhar pontos de rodada.<br>
   * 1 banco de dados, controlado pelo servidor, que fará o salvamento dos dados da partida, para garantir a confiabilidade da partida e salvamento do estado da mesma.<br>

## Lista de Testes:<br>
* <b>Teste de Concorrência:</b> serão feito acessos com com 2, 3, 4 e 5 pessoas, mostrando o funcionamento do aplicativo com diferentes quantidades de pessoas, demostrando que diversas pessoas podem entrar na mesma partida, sempre que alguem entre na partida ele se tornará o último da mesa a receber cartas e tomar ações.<br>

* <b>Teste de Recuperação de Falhas:</b><br>
   * reiniciaremos o servidor para demonstrar que ao ligar novamente a partida que estava sendo executada estará no mesmo estado antes do desligamento, caso todos os jogadores ainda estiverem online.<br>
   * reiniciaremos a internet de um jogador, para desmontrar que o servidor esperará por 20 segundos antes de considerar que o jogador abandonou a partida, caso ele entre novamente na sala antes do termino do tempo, suas cartas, pontos de rodada e posição na mesa ainda estaram salvos como antes do deligamento.<br>

* <b>Demonstração de Funcionalidades:</b> durante a execução da partida mostraremos o banco de dados para que seja possível visualizar que o mesmo está constantemente sendo alimentado pelo servidor, por informações tanto das cartas dos jogadores(seus pontos na rodada), seus pontos de rodada(rodadas ganhas pelo jogador), posição dos jogadores na mesa e de qual jogador e a vez de selecionar uma ação.<br>

## Instalações e execução do jogo Vinte e Um

* Primeiramente clone o repositório, certifique-se de possuir a [JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html) com o [maven](https://maven.apache.org/download.cgi) instalado e com as váriáveis de ambiente devidamente setadas (JAVA_HOME e M2_HOME).

* Na raíz do diretório clonado execute:

    `mvn clean install`

* Em seguida, entre na pasta 'target' gerada após o comando anterior ser executado e rode o seguinte comando:

    `java -jar Servidor-Executavel.jar`

* E o cliente:
    
    `java -jar Cliente-Executavel.jar`


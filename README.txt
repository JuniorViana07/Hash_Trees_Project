Documentação do Projeto: HashTabela Híbrida com Migração Dinâmica

-> Descrição do projeto

Este projeto implementa uma estrutura de dados híbrida para indexação e busca eficiente de transações financeiras. O sistema utiliza uma combinação de:

Tabela Hash com encadeamento por listas para indexação por id.

Tabela Hash com sondagem quadrática e migração dinâmica entre Listas, Árvore AVL e Árvore Rubro-Negra (RB) para indexação por origem.

O projeto visa lidar de forma eficiente com dados em diferentes volumes, adaptando a estrutura conforme o crescimento dos dados.

-> Estruturas utilizadas

- Tabela Hash (por ID)

Usa encadeamento por listas para resolver colisões.

Rápida para busca e inserção quando há poucas colisões.

- Tabela Hash com sondagem quadrática (por Origem)

Evita clustering primário.

O valor pode ser:

List<Registro>: usado quando poucos registros por origem.

TreeAVL<Registro>: migração ocorre quando lista > 3 elementos.

TreeRB<Registro>: migração ocorre quando AVL atinge altura > 10.

- Árvore AVL

Árvore binária de busca balanceada com rotação para manter altura mínima.

Complexidade: O(log n) para busca, inserção e remoção.

- Árvore Rubro-Negra (RB)

Árvore binária balanceada mais flexível (menos rotações que AVL em média).

Usada quando volume de dados por origem é grande.

Complexidade: O(log n) para busca, inserção e remoção.

Motivação: garantir eficiência ao longo do crescimento dos dados, com buscas e inserções rápidas independentemente do volume.

-> Principais métodos

inserirPorId(Registro registro)

Calcula hash e insere o registro na lista do índice correspondente.

buscarPorId(String id)

Recupera lista no índice hash e filtra registros pelo id.

inserirPorOrigem(Registro registro)

Insere em lista, AVL ou RB, migrando conforme necessário.

buscarPorOrigem(String origem)

Busca registros pela origem (independente da estrutura).

buscarPorOrigemEIntervalo(String origem, String tsInicio, String tsFim)

Recupera registros da origem e filtra pelo intervalo de timestamp.

-> Coleta de estatísticas

Durante as operações, o sistema registra:

Número de comparações.

Número de atribuições.

Tempo gasto (nanosegundos).

Migrações para AVL.

Migrações para RB.

Estes dados são usados para geração de relatório de desempenho.

-> Relatório CSV

O AppMain gera um arquivo CSV com o relatório usando as estatísticas citadas acima.


Como executar o projeto

1- Compile o projeto com seu IDE (foi usado IntelliJ no desenvolvimento)
2- Execute a classe DatasetGenerator para gerar o arquivo transacoes.csv
3- Execute a AppMain e o sistema irá ler o arquivo de registros, realizar as operações e gerar o relatório 'relatorio.csv' na pasta do projeto.

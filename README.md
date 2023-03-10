# **Estudos Java Multithread Paralelismo e Concorrência**
## **Fonte de estudos: [RinaldoDev - Multithread, Paralelismo e Concorrência](https://www.youtube.com/playlist?list=PLuYctAHjg89YNXAXhgUt6ogMyPphlTVQG)**
### **Execução Monothread**
Execução seqüêncial das linhas do código. Ou seja, se tivermos duas execuções de programas diferentes, um processo suportará somente um programa em seu espaço de endereçamento.
### **Execução Multithread**
Execução em paralelo das linhas do código. Neste caso, com o mesmo exemplo acima, ocorrerá a execução de parte do código do programa 1, depois de um tempo executará parte do código do programa 2, deixando o programa 1 em suspensão, em seguida o cenário troca para o programa 2 em suspensão e execução de parte do programa 1, e assim sucessivamente até que todas as execuções seja concluídas.
### **Paralelismo**
Execução de vários trechos de código no mesmo instante. Ou seja, são trechos de código executando em núcleos diferentes do processador. Paralelismo só pode ocorrer em processadores com dois núcleos ou mais.
### **Concorrência**
Várias execuções de código concorrendo pelo mesmo recurso. O recurso pode ser uma variável, um acesso ao banco de dados, uma chamada ao SO, um acesso a uma API externa, um acesso a uma lista ou mapa, ou até mesmo um acesso a um string builder,  então, se houver dois processos diferentes tentando acessar esse recurso ao mesmo tempo, ocorre a concorrência.
### **Aplicações Web**
Spring, Quarkus, JBoss, Wildfly, etc.

Muitas vezes, esses frameworks possuem formas próprias implementadas para lidar com multithread, paralelismo e concorrência. Precisa verificar como eles tratam esses assuntos.
### **Quando usar?**
- Processos batch/em lote - grande carga de dados com um processamento pesado e depois gerar um resultado; oriunda de um banco de dados, arquivo, requisição, etc.; paralelismo é usado para processar essa grande quantidade de dados
- Aplicações que executam no cliente - mobile, desktop; 

## **Aula 01 - Múltiplas Threads em Java - Thread e Runnable**
### **Descobrindo a thread atual**
Um getName() sobre um objeto.
### **Criando um objeto que representa uma nova thread**
**Forma mais simples:** instânciando um objeto Thread passando uma classe que implementa a interface Runnable. Essa classe precisa implementar o método run() da interface Runnable.

Entretanto, dessa forma não criamos exatamente uma nova thread, o que fazemos é criar um objeto de thread e mandamos executar na thread main.

**Método start():** o método start instrui a JVM a chamar o método run dessa thread assim que for possível. Ou seja, passa a decisão de quando chamar o método run para a JVM, ao invés de mandar para a thread main. Com isso a execução do método run será executado em uma thread diferente da main.

**Implementando Runnable com uma função lambda:** instânciamos um objeto Thread e ao invés de passar uma classe que implementa a interface Runnable, passamos uma função lambda com o que queremos fazer.

**Exception - IllegalThreadStateException:** Não podemos mandar executar a mesma thread mais de uma vez pois gerará a exception em negrito. A thread tem um sistema de controle interno o qual gerencia ela própria para saber se está executando, esperando, se foi interrompida, se está parada, etc. Esses vários estados da thread é um controle interno que impede iniciar a mesma thread mais de uma vez. Para executar o mesmo tipo de processo que uma thread executa mais de uma vez, precisamos criar uma nova thread.

**Várias Threads:** Como visto acima, não podemos chamar o método start mais de uma vez para uma thread, mas podemos executar o mesmo Runnable mais de uma vez. Ou seja, criamos threads diferentes que usam a mesma implementação de Runnable. Há alguns problemas de concorrência, mas mesmo assim, pode-se instânciar um Runnable e implementar o mesmo Runnable para threads diferentes.

Quando criamos várias threads, estamos criando novas linhas de execução paralelas dentro do programa. As threads podem executar em diversas ordens.

## **Aula 02 - Concorrência em Java - Sincronizar Recursos**
### **Concorrência**
No exemplo, temos uma variável global que é acessada por 5 threads diferentes, todas elas estão com a mesma instância de Runnable e executando todas elas, ou seja, o método run está sendo executado em paralelo por 5 threads diferentes, 5 vezes. E cada thread está incrementando a variável global.

Pensemos agora que a variável global é um recurso compartilhado sendo acessado por várias threads. Como resultado, não temos garantia de qual thread executará 1º, resultando em valores diferentes e iguais para cada nova execução desse programa.

Um dos resultados possíveis:
```
Thread-0: 1
Thread-1: 1
Thread-2: 2
Thread-4: 4
Thread-3: 3
```

### **Sincronização**
#### **syncronized no método run**
Com a palavra reservada *_syncronized_* evitamos a saída desordenada demonstrada acima. A ordem da saída pode não ser organizada, mas o resultado será organizado:
```
Thread-0: 0
Thread-4: 1
Thread-3: 2
Thread-2: 3
Thread-1: 4
```
Essa palavra reservada faz com que somente uma thread execute o método run dentro da instância de Runnable, ou seja, será executada uma de cada vez. Independente de qual thread chegar 1º para executar o método run, as outros que chegarem depois só serão executadas após a finalização da anterior. 

Neste caso, não temos mais o paralelismo, mas conseguimos resolver o problema de concorrência sobre o recurso.

#### **syncronized em um bloco de código**
Podemos criar um bloco de código com a palavra reservada syncronized passando a palavra reservada this, e dentro do bloco de código colocamos o código que queremos executar sincronizado. Fora do bloco também podemos alocar mais código, se necessário. Desta forma executará igual ao modo descrito acima, a diferença é que quando usamos um bloco sincronizado, precisamos informar em qual objeto faremos a sincronia (palavra reservada this). Isso porque, podemos ter vários blocos diferentes sincronizados, mas não queremos que a sincronia se dê no mesmo objeto. 

Exemplo de saída de dois blocos sincronizando o mesmo objeto:
```
Thread-0-Sincronizado em bloco: 0
Thread-0-Sincronizado em bloco: 1
Thread-4-Sincronizado em bloco: 2
Thread-4-Sincronizado em bloco: 3
Thread-3-Sincronizado em bloco: 4
Thread-3-Sincronizado em bloco: 5
Thread-2-Sincronizado em bloco: 6
Thread-2-Sincronizado em bloco: 7
Thread-1-Sincronizado em bloco: 8
Thread-1-Sincronizado em bloco: 9
```

Ou seja, voltamos para a concorrência. Para resolver isso, criamos dois objetos estáticos que servirão para regular essa sincronia:

```
Thread-0-Sincronizado em bloco: 0
Thread-4-Sincronizado em bloco: 2
Thread-3-Sincronizado em bloco: 3
Thread-2-Sincronizado em bloco: 4
Thread-0-Sincronizado em bloco: 1
Thread-1-Sincronizado em bloco: 5
Thread-2-Sincronizado em bloco: 6
Thread-3-Sincronizado em bloco: 7
Thread-4-Sincronizado em bloco: 8
Thread-1-Sincronizado em bloco: 9
```
#### **syncronized em um método estático**
Podemos alocar a palavra reservada syncronized na assinatura do método estático, funcionará normalmente. Mas se queremos sincronizar um bloco de código dentro do método estático, não podemos usar a palavra this, pois dentro de trecho estáticos não existe objeto para referenciar com this, não existe instância de objeto. Para resolver isso temos duas saídas:

1ª - Criar objeto estático (como acima) para servirem de lock.

2ª - Ou podemos fazer o lock na própria classe (nome_da_classe.class)

Mas dessa forma, estamos sincronizando toda a classe e, basicamente, somente uma thread pode executar o bloco de código na JVM inteira.
#### **Desvantagens de sincronização**
**syncronized no método run:** o método só é executado em uma thread de cada vez. Isson acaba com o paralelismo. Se o objetivo é usar da vantagem de criar threads e paralelismo, é melhor não usar esse método, pois é mais vantajoso usar os métodos convencionais. Ou mesmo usar um for e jogar o método run dentro dele, do que criar várias threads e sincronizá-las.
#### **Exemplo mais próximo do real**
No código de exemplo, isolamos o uso do bloco syncronized apenas para quando existe a concorrência. No caso, quando será acessada a variável global. Se o recurso está sendo concorrido, usamos o syncronized, do contrário, o restante do código não precisa de syncronized.
> código que não ocorre a concorrência:
```
public void run() {
            int j;
```
> recurso que ocorre a concorrência (variavelGlobal):
```
            synchronized(this){
                variavelGlobal++;
                j = variavelGlobal * 2;
            }
```            
> código que não ocorre a concorrência:
```
            double jElevadoA10 = Math.pow(j, 100);
            double sqrt = Math.sqrt(jElevadoA10);
            System.out.println(sqrt);
        }
```

## **Aula 03 - Concorrência em Java - Não usar Syncronized com List ou Map - Parte 1**
### **Método .sleep()**
> `Thread.sleep(milissegundos);`

Temos a thread principal, que no caso é a main e se criarmos várias threads, teremos a thread main mais essas outras threads criadas. Entretanto, pode ocorrer que todas elas ainda estão sendo processadas e o programa continue o código, ou seja, a thread main continue a execução, resultando em uma saída sem todos os resultados das outras threads. Neste caso, usamos o método .sleep() para que a thread main aguarde um determinado tempo, permitindo assim que as outras threads sejam concluídas.

Porém, quando usamos List, mesmo usando o método .sleep(), pode ocorrer de que o resultado de todas as threads sejam adicionadas na List e a saída ainda esteja faltando algum resultado. Isso é um erro que ocorre na List, sendo uma Collection a não ser usada com Threads. Para resolver isso sincronizamos coleções.

### **Sincronizando coleções**
Sobreescrevendo a lista com uma versão sincronizada:
```
List<String> lista = new ArrayList<>();
lista = Collections.synchronizedList(lista);
```
ou podemos usar:
```
List<String> lista = Collections.synchronizedList(new ArrayList<>());
```
Com isso, a lista será acessada em algumas operações por apenas uma única thread.

Também temos várias outras versões de syncronized:
- synchronizedList
- synchronizedCollection
- synchronizedMap
- synchronizedSet

## **Aula 03 - Concorrência em Java - Coleções para concorrência - Parte 2**
### **Coleções para concorrência**
**CopyOnWriteArrayList**

Outra forma de sincronizar a lista:
> `private static List<String> lista = new CopyOnWriteArrayList<>();`

Classe CopyOnWriteArrayList<>(); é thread safe. É uma classe segura  para cenários multithread, com várias threads acessando a coleção, sem causar um efeito colateral. Entretanto, essa é uma classe pesada, pois sempre que estivermos modificando a lista, a classe faz uma cópia do array inteiro, com todos os elementos da lista. Isso não é performático, mas é uma classe boa pois é thread safe. Se houver muita alteração na lista, como .add() ou .remove(), é melhor não usar essa classe. É ideal para leitura, como .get() e .indexOf().

**ConcurrentHashMap**

Semelhante ao problema descrito no início da aula, com Map nós temos o mesmo problema, então para contornar isso, usamos a classe ConcurrentHashMap:
> `private static Map<Integer, String> mapa = new ConcurrentHashMap<>();`

Ela é thread safe e se comporta da forma ideal para não gerar erros. Sua desvantagem é que é um pouco mais lenta pois ela é sincronizada internamente.

**BlockingQueue e LinkedBlockingQueue**

> `private static BlockingQueue<String> fila = new LinkedBlockingQueue<>();`

Uma boa alternativa em comparação com a solução para lista, pois essas classes são thread safe. Um outro ponto é com relação ao uso da lista. Se acaso estivermos usando operações que inserem e retiram elementos do coleção constantemente, usar uma fila pode ser uma solução mais interessante, ao invés de usar uma lista. Afinal, uma fila adiciona elementos ao final da fila e remove elementos do início da fila. Também podemos limitar o tamanho da fila.

**LinkedBlockingDeque**

Também temos a classe LinkedBlockingDeque, uma fila que permite adicionar ou remover elementos tanto no final, quanto no início da fila. Classe thread safe.

## **Aula 03 - Concorrência em Java - Classes Atômicas - Parte 3**
### **Classes Atômicas**

Com a classe **_AtomicInteger_**, não precisamos sincronizar o método run, pois ela executa métodos atômicos, ou seja, os seus métodos executaram de uma vez só, não há como uma outra atrapalhar a 1ª thread durante sua execução. Não é uma operação sincronizada, mas opera como. Temos outras classes atômicas:
- AtomicLong
- AtomicDoble
- AtomicBoolean
- AtomicReference

## **Aula 04 - Volatile e Yield**
### **Método .yield()**
> `Thread.yield();`
- método que avisa ao processador que não há trabalho a ser feito naquele momento, logo o processador pausa a execução da thread e diponibiliza o tempo de processador dessa thread para outra. Depois o processador pode retornar para essa thread que verificar se há a ser feito. A thread não para, somente pausa.

### **Método .getState()**
> `thread.getState();`
- método que retorna o estado atual da thread. Possíveis estados:
    - NEW - ainda não iniciou seu estado
    - RUNNABLE - está executando na JVM
    - BLOCKED - está esperando um monitor lock
    - WAITING - esperando indefinidamente outra thread executar uma ação
    - TIMED_WAITING - esperando por um tempo determinado outra thread executar uma ação
    - TERMINATED - uma que terminou sua execução
### **Enum State.< Estado >**
> `State.TERMINATED;`
- enum que retorna algum dos 6 estados citados acima. 
- é interessante usar o .getState() junto com State.< Estado > para fazer alguma verificação booleana

### [Explicação da execução do código da classe Aula04YieldEVolatileParteDois sem a palavra reservada volatile - até 10:35](https://youtu.be/4bH-XilmJoI?list=PLuYctAHjg89YNXAXhgUt6ogMyPphlTVQG&t=542)

### **Palavra reservada volatile**
- indica para o programa não confiar no que está no cachê local dentro do processador. Escreva e leia esse dado sempre direto da memória ram. Não use o cachê local para acessar esse valor. Isso fará que ao ler a variável, sempre será lido o valor mais recente
- em geral não precisamos usar essa palavra reservada
- se usarmos ela, estaremos deixando ler o valor que está em cachê no processador para acessar a memória ram, logo, o programa se torna mais lento
- mas podemos também verificar que podemos estar perdendo um pouco de performance, mas ao mesmo tempo, como no exemplo, estamos usando paralelismo, o que incrementa a performance muito mais que a perca por usar volatile

### [Explicação da palavra reservada volatile - até 16:20](https://youtu.be/4bH-XilmJoI?list=PLuYctAHjg89YNXAXhgUt6ogMyPphlTVQG&t=680)



# plataformaAulas

 Resolução do Desafio FullStack criado com JHipster e JDL-Studio [https://www.jhipster.tech/documentation-archive/v8.8.0](https://www.jhipster.tech/documentation-archive/v8.8.0).

## Estrutura do projeto

Node é necessário para geração e recomendado para desenvolvimento. O `package.json` é sempre gerado para uma melhor experiência de desenvolvimento com o prettier, commit hooks, scripts e etc.

Na raiz do projeto, o JHipster gera arquivos de configuração para ferramentas como git, prettier, eslint, husky, e outras que são bem conhecidas e você pode encontrar referências na web.

A estrutura `/src/*` segue a estrutura padrão do Java.
- `.yo-rc.json` - Arquivo de configuração do Yeoman
  A configuração do JHipster é armazenada neste ficheiro na chave `generator-jhipster`. Você pode encontrar `generator-jhipster-*` para configurações específicas de blueprints.
- `.yo-resolve` (opcional) - Resolvedor de conflitos Yeoman
  Permite usar uma ação específica quando conflitos são encontrados, ignorando prompts para arquivos que combinam com um padrão. Cada linha deve corresponder a `[pattern] [action]` com pattern sendo um padrão [Minimatch](https://github.com/isaacs/minimatch#minimatch) e action sendo uma das opções skip (padrão se omitido) ou force. Linhas começando com `#` são consideradas comentários e são ignoradas.
- `.jhipster/*.json` - arquivos de configuração de entidades JHipster

- `npmw` - wrapper para usar o npm instalado localmente.
  O JHipster instala o Node e o npm localmente usando a ferramenta de compilação por padrão. Este wrapper garante que o npm está instalado localmente e o utiliza evitando algumas diferenças que versões diferentes podem causar. Ao utilizar `./npmw` ao invés do tradicional `npm` é possível configurar um ambiente sem Node para desenvolver ou testar sua aplicação.
- `/src/main/docker` - Configurações do Docker para a aplicação e serviços dos quais a aplicação depende


### Docker Compose support (IMPORTANTE ANTES DE INICIAR A APLICAÇÃO)

O JHipster gera vários arquivos de configuração do Docker Compose na pasta [src/main/docker/](src/main/docker/) para iniciar os serviços de terceiros necessários.

Por exemplo, para iniciar os serviços necessários em contêineres do Docker, execute:

```
docker compose -f src/main/docker/services.yml up -d
```

Para parar e remover os contêineres, execute:

```
docker compose -f src/main/docker/services.yml down
```

A [Integração do Spring Docker Compose] (https://docs.spring.io/spring-boot/reference/features/dev-services.html) está ativada por padrão. É possível desativá-la no application.yml:

```yaml
spring:
  ...
  docker:
    compose:
      enabled: false
```

Você também pode dockerizar totalmente seu aplicativo e todos os serviços dos quais ele depende.
Para isso, primeiro crie uma imagem do Docker do seu aplicativo executando:

```sh
npm run java:docker
```

Ou crie uma imagem Docker arm64 ao usar um sistema operacional com processador arm64, como o MacOS com família de processadores M1, executando:

```sh
npm run java:docker:arm64
```

Em seguida, execute:

```sh
docker compose -f src/main/docker/app.yml up -d
```

Para obter mais informações, consulte [Usando o Docker e o Docker-Compose][], essa página também contém informações sobre o subgerador do Docker Compose (`jhipster docker-compose`), que é capaz de gerar configurações do Docker para um ou vários aplicativos JHipster.
## Desenvolvimento

O sistema de compilação instalará automaticamente a versão recomendada do Node e do npm.

Nós fornecemos um wrapper para iniciar o npm.
Você só precisará executar este comando quando as dependências mudarem em [package.json](package.json).

```
./npmw install
```

Nós usamos scripts npm e [Angular CLI][] com [Webpack][] como nosso sistema de build.

Execute os seguintes comandos em dois terminais separados para criar uma experiência de desenvolvimento feliz onde seu navegador
se atualiza automaticamente quando os arquivos mudam no seu disco rígido.

```
./mvnw
./npmw start
```
O Npm também é utilizado para gerir as dependências de CSS e JavaScript utilizadas nesta aplicação. Você pode atualizar as dependências
especificando uma versão mais recente em [package.json](package.json). Você também pode executar `./npmw update` e `./npmw install` para gerenciar as dependências.
Adicione o sinalizador `help` em qualquer comando para ver como você pode usá-lo. Por exemplo, `./npmw help update`.

O comando `./npmw run` listará todos os scripts disponíveis para execução neste projeto.

### Suporte a PWA

O JHipster vem com suporte a PWA (Progressive Web App), e está desligado por padrão. Um dos principais componentes de um PWA é um service worker.

O código de inicialização do service worker está desativado por padrão. Para habilitá-lo, descomente o seguinte código em `src/main/webapp/app/app.config.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Gerenciando dependências

Por exemplo, para adicionar a biblioteca [Leaflet][] como uma dependência de tempo de execução do seu aplicativo, você deve executar o seguinte comando:

```
./npmw install --save --save-exact leaflet
```

Para se beneficiar das definições de tipo TypeScript do repositório [DefinitelyTyped][] no desenvolvimento, você deve executar o seguinte comando:

```
./npmw install --save-dev --save-exact @types/leaflet
```

Em seguida, importaria os arquivos JS e CSS especificados nas instruções de instalação da biblioteca para que o [Webpack][] saiba sobre eles:
Editar o ficheiro [src/main/webapp/app/app.config.ts](src/main/webapp/app/app.config.ts):

```
importar 'leaflet/dist/leaflet.js';
```

Editar o ficheiro [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss):

```
@importar 'leaflet/dist/leaflet.css';
```

Nota: Ainda há algumas outras coisas a serem feitas para o Leaflet que não detalharemos aqui.

Para mais instruções sobre como desenvolver com o JHipster, dê uma olhada em [Using JHipster in development][].

### Usando Angular CLI

Você também pode usar [Angular CLI][] para gerar algum código de cliente personalizado.

Por exemplo, o seguinte comando:

```
ng generate component meu-componente
```

irá gerar alguns arquivos:

```
create src/main/webapp/app/meu-componente/meu-componente.component.html
criar src/main/webapp/app/my-component/my-component.component.ts
atualizar src/main/webapp/app/app.config.ts
```

## Construindo para produção

### Empacotando como jar

Para construir o jar final e otimizar a aplicação plataformaAulas para produção, execute:

```
./mvnw -Pprod clean verify
```

Isso irá concatenar e reduzir os arquivos CSS e JavaScript do cliente. Ele também modificará o `index.html` para que ele faça referência a esses novos arquivos.
Para garantir que tudo funcionou, execute:

```
java -jar target/*.jar
```

Em seguida, navegue até [http://localhost:8080](http://localhost:8080) no seu navegador.

Consulte [Usando o JHipster em produção][] para obter mais detalhes.
### Empacotamento como WAR

Para empacotar seu aplicativo como uma WAR a fim de implantá-lo em um servidor de aplicativos, execute:

```
./mvnw -Pprod,war clean verify
```

### Centro de controle JHipster

O JHipster Control Center pode ajudá-lo a gerenciar e controlar seus aplicativos. Você pode iniciar um servidor local do centro de controle (acessível em http://localhost:7419) com:

```
docker compose -f src/main/docker/jhipster-control-center.yml up
```

## Testes

### Testes do Spring Boot

Para iniciar os testes do seu aplicativo, execute:

```
./mvnw verify
```

### Testes de cliente

Os testes de unidade são executados pelo [Jest][]. Eles estão localizados em [src/test/javascript/](src/test/javascript/) e podem ser executados com:

```
./npmw test
```
## Continuous Integration | Integração Contínua (optional)
Para configurar a CI para o seu projeto, execute o subgerador ci-cd (`jhipster ci-cd`), que permitirá que você gere arquivos de configuração para vários sistemas de integração contínua. Consulte a página [Setting up Continuous Integration][] para obter mais informações.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 8.8.0 archive]: https://www.jhipster.tech/documentation-archive/v8.8.0
[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v8.8.0/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v8.8.0/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v8.8.0/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v8.8.0/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v8.8.0/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v8.8.0/setting-up-ci/
[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[BrowserSync]: https://www.browsersync.io/
[Jest]: https://facebook.github.io/jest/
[Leaflet]: https://leafletjs.com/
[DefinitelyTyped]: https://definitelytyped.org/
[Angular CLI]: https://cli.angular.io/

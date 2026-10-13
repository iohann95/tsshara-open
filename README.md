# tsshara-open

Ferramenta de linha de comando para Nobreak **TS Shara UPS 2200** via comunicação serial.
Possivelmente funciona em outros modelos.

Esta ferramenta foi criada devido a integração com o mobile do aplicativo oficial não funcionar.
Inclui app Android que se comunica com a versão CLI através de uma API REST.

## Estrutura do projeto

```
tsshara-open/
├── tsshara-cli.py              # CLI/monitor/API — arquivo único de propósito (deploy simples via NSSM)
├── requirements.txt            # Dependências Python (pyserial, colorama)
├── install-windows-service.bat # Instalador de serviço Windows (NSSM)
└── android-app/                # App Android (Kotlin + Jetpack Compose)
```

## Requisitos

- Python 3.9+
- `pip install -r requirements.txt`

## Uso rápido

```
python tsshara-cli.py                # menu interativo
python tsshara-cli.py monitor        # monitoramento contínuo (+ API, se habilitada)
python tsshara-cli.py read           # ler status/configuração do nobreak
python tsshara-cli.py test 10s       # teste de bateria de 10 segundos
python tsshara-cli.py init-config    # gerar config.ini padrão
```

Configurações (serial, e-mail, API, logging) ficam no `config.ini`
(gere com `init-config` e edite, ou use a opção 7 do menu interativo).

## Serviço Windows

Use `install-windows-service.bat` (requer [NSSM](https://nssm.cc/download)) para
rodar o monitor como serviço. O CLI detecta quando o serviço está rodando e
roteia comandos pela API para evitar conflito na porta COM.

## API HTTP (para o app Android)

Habilite na seção `[api]` do `config.ini`. Endpoints:
`/api/status`, `/api/health`, `/api/info`, `/api/read`, `/api/write`, `/api/test/<tipo>`.
Suporta Basic Auth opcional.

## App Android

Em `android-app/` — requer Android 15+ (minSdk 35). Build:

```
cd android-app
gradlew.bat assembleDebug
```

Veja `android-app/README.md` para detalhes.

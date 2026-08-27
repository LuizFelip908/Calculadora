# 📱 Calculadora

Aplicativo de calculadora para Android, desenvolvido em **Kotlin**, com interface simples e intuitiva em tema escuro.

## 📊 Progresso do Projeto

**Status geral: ✅ 100% concluído**

`██████████████████████████████` 100%

| # | Etapa | Status |
|---|-------|--------|
| 1 | Configuração inicial do projeto (Gradle + Kotlin) | ✅ Concluído |
| 2 | Estrutura de telas e tema escuro | ✅ Concluído |
| 3 | Layout do teclado e área de visor | ✅ Concluído |
| 4 | Entrada de números e vírgula decimal | ✅ Concluído |
| 5 | Operações básicas: soma, subtração, multiplicação e divisão | ✅ Concluído |
| 6 | Precedência de operadores (× e ÷ antes de + e −) | ✅ Concluído |
| 7 | Porcentagem (%) | ✅ Concluído |
| 8 | Alteração de sinal (+/−) | ✅ Concluído |
| 9 | Botão de resolver (=) com histórico do cálculo no visor | ✅ Concluído |
| 10 | Limpar (AC) e apagar dígito (⌫) | ✅ Concluído |
| 11 | Tratamento de erros (divisão por zero) | ✅ Concluído |

## ✨ Funcionalidades

- ➕ **Soma (+)**
- ➖ **Subtração (−)**
- ✖️ **Multiplicação (×)**
- ➗ **Divisão (÷)** — com tratamento de divisão por zero
- 💯 **Porcentagem (%)** — inteligente: `200 + 10%` resolve como `200 + 20`
- 🔁 **Alteração de sinal (+/−)**
- 🟰 **Resolver (=)**
- 🧮 **Cálculo exibido na área superior** do visor enquanto você digita
- 🔆 **Resultado em destaque**, com fonte grande e ajuste automático de tamanho
- ⌫ Apagar último dígito e **AC** para limpar tudo
- 🔢 Suporte a números decimais com vírgula (padrão brasileiro)

## 🖥️ Interface

- Tema escuro moderno com botões arredondados
- Visor dividido em duas áreas:
  - **Expressão** (parte superior, em cinza): mostra o cálculo completo
  - **Resultado** (em destaque, fonte grande e em negrito)
- Teclado em grade 4×5:

```
┌──────┬──────┬──────┬──────┐
│  AC  │ +/−  │  %   │  ÷   │
├──────┼──────┼──────┼──────┤
│  7   │  8   │  9   │  ×   │
├──────┼──────┼──────┼──────┤
│  4   │  5   │  6   │  −   │
├──────┼──────┼──────┼──────┤
│  1   │  2   │  3   │  +   │
├──────┼──────┼──────┼──────┤
│  ⌫   │  0   │  ,   │  =   │
└──────┴──────┴──────┴──────┘
```

## 🛠️ Tecnologias

- **Kotlin** 1.9
- **Android SDK** — minSdk 26 / targetSdk 34
- **ViewBinding**
- **Material 3** (tema escuro)
- **BigDecimal** para cálculos precisos (sem erros de ponto flutuante)

## 🚀 Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/LuizFelip908/Calculadora.git
   ```
2. Abra o projeto no **Android Studio**
3. Aguarde a sincronização do Gradle
4. Execute em um emulador ou dispositivo físico (Android 8.0+)

Ou pelo terminal:

```bash
./gradlew assembleDebug
```

## 📂 Estrutura do projeto

```
app/src/main/
├── java/com/luizfelipe/calculadora/
│   ├── MainActivity.kt        # Tela principal e interação com o usuário
│   └── CalculatorEngine.kt    # Motor de cálculo (precedência, BigDecimal)
└── res/
    ├── layout/activity_main.xml   # Visor + teclado em grade
    ├── values/                    # Cores, strings e temas
    └── drawable/                  # Fundos arredondados dos botões
```

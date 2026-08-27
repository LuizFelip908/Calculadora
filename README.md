# 📱 Calculadora

Aplicativo de calculadora para **Android**, desenvolvido em **Kotlin** com **Jetpack Compose**, com interface inspirada em um design moderno de teclas arredondadas e coluna de operadores em laranja.

## ✨ Funcionalidades

| Tecla | Função |
|-------|--------|
| `+`   | Soma |
| `−`   | Subtração |
| `×`   | Multiplicação |
| `÷`   | Divisão (com tratamento de divisão por zero → "Erro") |
| `%`   | Porcentagem |
| `+/−` | Alteração de sinal |
| `=`   | Resolver |
| `C`   | Limpar tudo |

- ✅ O **cálculo realizado** aparece na **área superior** do visor (ex.: `4,900 + 15,910`)
- ✅ O **resultado** é exibido **em destaque**, com fonte grande (ex.: `20,810`)
- ✅ Números formatados com separador de milhar
- ✅ Encadeamento de operações (`2 + 3 + 4 =` → `9`)
- ✅ **Tema escuro e claro**, seguindo o tema do sistema (como os dois modelos do design de referência)

## 🎨 Design

A interface segue o design de referência:

- Fundo escuro `#17181D` (ou claro `#EDEFF4`)
- Teclas de função (`C`, `+/−`, `%`) em cinza destacado
- Coluna de operadores (`÷ × − +` e `=`) em **laranja** `#F7941D`
- Teclas com cantos bem arredondados e tecla `0` larga
- Visor com a expressão em cinza discreto e o resultado grande em destaque

## 🏗️ Estrutura do projeto

```
Calculadora/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/luizfelipe/calculadora/
│       │   │   ├── MainActivity.kt          # Tela e teclado (Jetpack Compose)
│       │   │   ├── CalculatorViewModel.kt   # Estado da UI (MVVM)
│       │   │   ├── CalculatorEngine.kt      # Lógica de cálculo (Kotlin puro)
│       │   │   └── ui/theme/                # Cores e tema (dark/light)
│       │   ├── res/                         # Recursos (strings, ícones, temas)
│       │   └── AndroidManifest.xml
│       └── test/
│           └── .../CalculatorEngineTest.kt  # Testes unitários da lógica
├── build.gradle.kts
└── settings.gradle.kts
```

A lógica da calculadora (`CalculatorEngine`) é **Kotlin puro**, separada da interface, o que permite testá-la com testes unitários simples (JUnit).

## 🚀 Como executar

1. Abra o projeto no **Android Studio** (Hedgehog ou mais recente)
2. Aguarde a sincronização do Gradle (o Android Studio baixa tudo automaticamente)
3. Execute em um emulador ou dispositivo com **Android 7.0 (API 24)** ou superior

Para rodar os testes unitários:

```bash
./gradlew :app:testDebugUnitTest
```

> 💡 Se o wrapper do Gradle ainda não tiver sido gerado na sua máquina, o próprio Android Studio cuida disso na primeira sincronização (ou rode `gradle wrapper --gradle-version 8.9`).

## 🛠️ Tecnologias

- [Kotlin](https://kotlinlang.org/) 2.0
- [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- Arquitetura **MVVM** com `ViewModel`
- JUnit 4 para testes unitários

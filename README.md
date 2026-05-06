# Scoreboard Radar – Live matches score collector app

This app is created to capture live matches score and display them in the highest total score order.

## 📍Intentional choices

* Avoid using third party libraries as much as possible
* Keep the smallest possible abstraction while keeping the code readable and testable
* Avoid mocks and other test doubles they are not necessary
* Focus strictly on the TDD process
* Use java.util.logging (JUL) for logging to maintain simplicity and avoid external dependencies like SLF4J or Logback.
* Solution will be single threaded at first glance

## ❔Considerations
* Should we implement team-based validation guards? Like can the same team play two matches at the same time?

## 📐 Design choices

## 🏔️ Limitations

## ▶️ How to run the app?

On **Windows**:

```shell
mvn clean compile dependency:copy-dependencies
java -cp "target/classes;target/dependency/*" io.github.grano22.ScoreboardRadarCliApp
```

On **Linux/Mac**:

```shell
mvn clean compile dependency:copy-dependencies
java -cp "target/classes:target/dependency/*" io.github.grano22.ScoreboardRadarCliApp
```

## 🧪 How to run the tests?

``mvn clean test``

## 🪪 License

Distributed under the Unlicense License. See LICENSE.txt for more information.